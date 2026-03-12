import { useNavigate } from "react-router-dom";
import Button from "../components/Button";

function NotFound() {
    const navigate = useNavigate();

    return (
        <div className="not-found">
            <div className="not-found__code">404</div>
            <p className="not-found__text">
                Oops! The page you're looking for doesn't exist.
            </p>
            <Button type="button" size="lg" onClick={() => navigate("/")}>
                Go Home
            </Button>
        </div>
    );
}

export default NotFound;
