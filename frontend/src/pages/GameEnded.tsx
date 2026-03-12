import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import Button from "../components/Button";

function GameEnded() {
    const navigate = useNavigate();

    return (
        <>
            <Header />
            <div className="game-ended">
                <div className="game-ended__icon">🏆</div>
                <h1 className="game-ended__title">Game Over!</h1>
                <p className="game-ended__subtitle">
                    Great game! Thanks for playing.
                </p>
                <Button type="button" size="lg" onClick={() => navigate("/")}>
                    Back to Home
                </Button>
            </div>
        </>
    );
}

export default GameEnded;
