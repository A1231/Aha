import { useNavigate } from "react-router-dom";
import Button from "../components/Button";
import Header from "../components/Header";
import { useEffect, useState } from "react";
import { API_BASE_URL } from "../config";
import LoadingSpinner from "../components/LoadingSpinner";
import type { HealthResponse } from "../types/HealthResponse";   

function LandingPage() {
    const navigate = useNavigate();
    const [health, setHealth] = useState<HealthResponse | null>(null);

    useEffect(() => {
        const fetchHealth = async () => {
            try {
                const response = await fetch(`${API_BASE_URL || ""}/api/health`);
                const data = await response.json();
                setHealth(response.ok ? data : null);
            } catch {
                setHealth(null);
            }
        };
        fetchHealth();
    }, []);

    return (
        <>
            <Header />
            {health?.status !== "healthy" && <LoadingSpinner message="Starting the server… please wait a few seconds before creating or joining a room." />}
            {health?.status === "healthy" && <div className="landing">
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
            </div>}
        </>
    );
}

export default LandingPage;
