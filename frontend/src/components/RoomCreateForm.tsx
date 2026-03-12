import { useState } from "react";
import type { RoomRequest } from "../types/RoomRequest";
import type { RoomResponse } from "../types/RoomResponse";
import Button from "./Button";
import Notification from "./Notification";
import { useStomp } from "./StompProvider";

function RoomCreateForm({ setRoom }: { setRoom: (room: RoomResponse) => void }) {
    const { connect } = useStomp();
    const [topic, setTopic] = useState("");
    const [hostName, setHostName] = useState("");
    const [maxPlayers, setMaxPlayers] = useState(0);
    const [error, setError] = useState<string | null>(null);

    async function handleSubmit(e: React.SubmitEvent<HTMLFormElement>) {
        e.preventDefault();
        setError(null);
        if (!hostName || !topic || !maxPlayers) {
            setError("Please fill in all fields");
            return;
        }
        try {
            const room: RoomRequest = { hostName, topic, maxPlayers };
            const response = await fetch("/api/rooms", {
                method: "POST",
                body: JSON.stringify(room),
                headers: { "Content-Type": "application/json" },
            });
            if (!response.ok) {
                const data = await response.json().catch(() => null);
                setError(data?.message ?? "Failed to create room");
                return;
            }
            const data = await response.json();
            setRoom(data);
            connect();
        } catch {
            setError("Network error. Please check your connection.");
        }
    }

    return (
        <div className="card">
            {error && <Notification message={error} variant="error" onDismiss={() => setError(null)} />}
            <h2 className="card__title">Create a New Room</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="hostName">Host Name</label>
                    <input
                        type="text"
                        id="hostName"
                        name="hostName"
                        placeholder="Your display name"
                        value={hostName}
                        onChange={(e) => setHostName(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="topic">Topic</label>
                    <input
                        type="text"
                        id="topic"
                        name="topic"
                        placeholder="e.g. Science, History..."
                        value={topic}
                        onChange={(e) => setTopic(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="maxPlayers">Max Players</label>
                    <input
                        type="number"
                        id="maxPlayers"
                        name="maxPlayers"
                        value={maxPlayers}
                        onChange={(e) => setMaxPlayers(parseInt(e.target.value))}
                        min={2}
                        max={10}
                    />
                </div>
                <Button type="submit" fullWidth>Create Room</Button>
            </form>
        </div>
    );
}

export default RoomCreateForm;
