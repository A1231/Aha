import { useState } from "react";
import { useStomp } from "./StompProvider";
import Button from "./Button";
import Notification from "./Notification";
import type { RoomResponse } from "../types/RoomResponse";

export default function JoinRoomForm({ setRoom }: { setRoom: (room: RoomResponse) => void }) {
    const { connect } = useStomp();
    const [roomId, setRoomId] = useState("");
    const [playerName, setPlayerName] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        if (!roomId || !playerName || !password) {
            setError("Please fill in all fields");
            return;
        }
        try {
            const response = await fetch("/api/rooms/join", {
                method: "POST",
                body: JSON.stringify({ roomId, password, playerName }),
                headers: { "Content-Type": "application/json" },
            });
            if (!response.ok) {
                const data = await response.json().catch(() => null);
                setError(data?.message ?? "Failed to join room");
                return;
            }
            const data: RoomResponse = await response.json();
            setRoom(data);
            connect();
        } catch {
            setError("Network error. Please check your connection.");
        }
    };

    return (
        <div className="card">
            {error && <Notification message={error} variant="error" onDismiss={() => setError(null)} />}
            <h2 className="card__title">Join a Room</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="roomId">Room ID</label>
                    <input
                        type="text"
                        id="roomId"
                        name="roomId"
                        placeholder="Paste the room ID"
                        value={roomId}
                        onChange={(e) => setRoomId(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="playerName">Player Name</label>
                    <input
                        type="text"
                        id="playerName"
                        name="playerName"
                        placeholder="Your display name"
                        value={playerName}
                        onChange={(e) => setPlayerName(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Room password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <Button type="submit" fullWidth>Join Room</Button>
            </form>
        </div>
    );
}
