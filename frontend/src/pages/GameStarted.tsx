import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useStomp } from "../components/StompProvider";
import Header from "../components/Header";
import type { QuestionResponse } from "../types/QuestionResponse";
import type { LeaderboardResponse } from "../types/LeaderboardResponse";
import type { AnswerRequest } from "../types/AnswerRequest";
import type { AnswerResponse } from "../types/AnswerResponse";

function GameStarted() {
    const { connected, subscribe, publish } = useStomp();
    const navigate = useNavigate();
    const { roomId } = useParams<{ roomId: string }>();

    const [question, setQuestion] = useState<QuestionResponse | null>(null);
    const [answerResult, setAnswerResult] = useState<AnswerResponse | null>(null);
    const [leaderboard, setLeaderboard] = useState<LeaderboardResponse[]>([]);
    const [selectedIndex, setSelectedIndex] = useState<number | null>(null);

    useEffect(() => {
        if (!connected || !roomId) return;

        const unsub1 = subscribe(`/topic/room/${roomId}/game-started`, (msg) => {
            console.log("Game started:", JSON.parse(msg.body));
        });

        const unsub2 = subscribe(`/topic/room/${roomId}/question`, (msg) => {
            setQuestion(JSON.parse(msg.body));
            setAnswerResult(null);
            setSelectedIndex(null);
        });

        const unsub3 = subscribe(`/topic/room/${roomId}/game-ended`, () => {
            navigate(`/game-ended/${roomId}`);
        });

        const unsub4 = subscribe(`/user/queue/room/${roomId}/answer`, (msg) => {
            setAnswerResult(JSON.parse(msg.body) as AnswerResponse);
        });

        const unsub5 = subscribe(`/topic/room/${roomId}/leaderboard`, (msg) => {
            setLeaderboard(JSON.parse(msg.body));
        });

        return () => {
            unsub1?.();
            unsub2?.();
            unsub3?.();
            unsub4?.();
            unsub5?.();
        };
    }, [connected, roomId, subscribe, navigate]);

    const submitAnswer = (answerIndex: number) => {
        if (!question || selectedIndex !== null) return;
        setSelectedIndex(answerIndex);
        const request: AnswerRequest = {
            answerIndex,
            questionIndex: question.questionIndex,
        };
        publish(`/app/room/${roomId}/question`, request);
    };

    if (!roomId) return <div className="page page--center"><p>No room found</p></div>;

    const optionLabels = ["A", "B", "C", "D"];

    const getOptionClass = (idx: number) => {
        const classes = ["option-btn"];

        if (answerResult) {
            if (idx === answerResult.correctAnswerIndex) {
                classes.push("option-btn--correct");
            } else if (selectedIndex === idx && !answerResult.correct) {
                classes.push("option-btn--wrong");
            } else if (idx !== answerResult.correctAnswerIndex && idx !== selectedIndex) {
                classes.push("option-btn--dimmed");
            }
        } else if (selectedIndex === idx) {
            classes.push("option-btn--selected");
        }

        return classes.join(" ");
    };

    return (
        <>
            <Header />
            <div className="game-layout">
                <div className="game-main">
                    <h1 className="game-main__heading">Live Game</h1>

                    {question ? (
                        <div className="question-card">
                            <p className="question-card__number">
                                Question {question.questionIndex + 1}
                            </p>
                            <h2 className="question-card__text">{question.questionText}</h2>

                            <div className="question-card__options">
                                {question.options.map((option, idx) => (
                                    <button
                                        key={idx}
                                        className={getOptionClass(idx)}
                                        onClick={() => submitAnswer(idx)}
                                        disabled={selectedIndex !== null}
                                    >
                                        <span className="option-btn__label">{optionLabels[idx]}</span>
                                        <span>{option}</span>
                                    </button>
                                ))}
                            </div>

                            {answerResult && (
                                <div
                                    className={`result-banner ${
                                        answerResult.correct
                                            ? "result-banner--correct"
                                            : "result-banner--wrong"
                                    }`}
                                >
                                    {answerResult.correct ? "Correct!" : "Incorrect"}
                                </div>
                            )}
                        </div>
                    ) : (
                        <p className="waiting-text">Waiting for the next question...</p>
                    )}
                </div>

                <aside className="sidebar">
                    <h2 className="sidebar__heading">Leaderboard</h2>
                    {leaderboard.length === 0 ? (
                        <p className="no-data">No scores yet</p>
                    ) : (
                        <ol className="leaderboard">
                            {leaderboard.map((entry, idx) => (
                                <li key={entry.playerId} className="leaderboard__item">
                                    <span className="leaderboard__rank">#{idx + 1}</span>
                                    <span className="leaderboard__name">{entry.playerName}</span>
                                    <span className="leaderboard__score">{entry.score}</span>
                                </li>
                            ))}
                        </ol>
                    )}
                </aside>
            </div>
        </>
    );
}

export default GameStarted;
