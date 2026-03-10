
import { useNavigate } from "react-router-dom";
import Button from "../components/Button";
import Header from "../components/Header";

function LandingPage() {
    const navigate = useNavigate();
    return (
        <div>
            <Header />

            <Button type="button" onClick={() => navigate("/room-creation")}>Create Room</Button>
            <Button type="button" onClick={() => navigate("/join-room")}>Join Room</Button>
        </div>
    )
}

export default LandingPage;
