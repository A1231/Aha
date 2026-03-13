import Header from "../components/Header";
import RoomCreateForm from "../components/RoomCreateForm";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import type { RoomResponse } from "../types/RoomResponse";
import RoomDetails from "../components/RoomDetails";
import AddQuestionSet from "../components/AddQuestionSet";
import PlayerList from "../components/PlayerList";
import Button from "../components/Button";
import Notification from "../components/Notification";
import { useStomp } from "../components/StompProvider";
import { API_BASE_URL } from "../config";

function RoomCreationPage() {
    const [room, setRoom] = useState<RoomResponse | null>(null);
    const [submitted, setSubmitted] = useState(false);
    const [players, setPlayers] = useState<string[]>([]);
    const [error, setError] = useState<string | null>(null);
    const { connected, subscribe } = useStomp();
    const navigate = useNavigate();

    const handleStartGame = async () => {
        setError(null);
        try {
            const response = await fetch(`${API_BASE_URL}/api/rooms/start`, { method: "POST", credentials: "include" });
            if (!response.ok) {
                const data = await response.json().catch(() => null);
                setError(data?.message ?? "Failed to start game");
                return;
            }
            navigate(`/game-started/${room!.roomId}`);
        } catch {
            setError("Network error. Please check your connection.");
        }
    };

    useEffect(() => {
        if (!connected || !room) return;
        const unsub1 = subscribe(`/topic/room/${room.roomId}`, (msg) => {
            const data = JSON.parse(msg.body);
            if (Array.isArray(data.players)) {
                setPlayers(data.players);
            }
        });

        return () => { unsub1?.(); };
    }, [connected, room, subscribe]);

    return (
        <>
            <Header />
            {error && <Notification message={error} variant="error" onDismiss={() => setError(null)} />}
            <div className="page">
                <div className="page__content">
                    {room ? <RoomDetails room={room} /> : <RoomCreateForm setRoom={setRoom} />}
                    {room && <PlayerList players={players} />}
                    {room && !submitted && <AddQuestionSet onSubmitted={() => setSubmitted(true)} />}
                    {submitted && (
                        <>
                            <div className="success-msg mt-2">
                                Questions submitted successfully!
                            </div>
                            <Button type="button" variant="success" size="lg" fullWidth onClick={handleStartGame}>
                                Start Game
                            </Button>
                        </>
                    )}
                </div>
            </div>
        </>
    );
}

export default RoomCreationPage;
