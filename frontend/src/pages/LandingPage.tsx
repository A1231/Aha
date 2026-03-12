import { useNavigate } from "react-router-dom";
import Button from "../components/Button";
import Header from "../components/Header";

function LandingPage() {
    const navigate = useNavigate();
    return (
        <>
            <Header />
            <div className="landing">
                <div className="landing__hero">
                    <p className="landing__tagline">
                        Create a room, add your questions, and challenge your friends
                        in real-time. Fast, fun, and competitive!
                    </p>
                    <div className="landing__actions">
                        <Button type="button" size="lg" onClick={() => navigate("/room-creation")}>
                            Create Room
                        </Button>
                        <Button type="button" variant="secondary" size="lg" onClick={() => navigate("/join-room")}>
                            Join Room
                        </Button>
                    </div>
                </div>
            </div>
        </>
    );
}

export default LandingPage;
