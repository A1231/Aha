import { useState } from "react";
import type { RoomResponse } from "../types/RoomResponse";

function RoomDetails({ room }: { room: RoomResponse }) {
    const [copied, setCopied] = useState(false);

    const handleCopy = async () => {
        const text = `Room ID: ${room.roomId}\nPassword: ${room.password}`;
        await navigator.clipboard.writeText(text);
        setCopied(true);
        setTimeout(() => setCopied(false), 1500);
    };

    return (
        <div className="card mb-2">
            <h2 className="card__title">Room Created</h2>
            <div className="room-details">
                <div className="room-details__item">
                    <span className="room-details__label">Topic</span>
                    <span className="room-details__value">{room.topic}</span>
                </div>
                <div className="room-details__item">
                    <span className="room-details__label">Max Players</span>
                    <span className="room-details__value">{room.maxPlayers}</span>
                </div>
                <div className="room-details__item">
                    <span className="room-details__label">Room ID</span>
                    <span className="room-details__value room-details__value--mono">{room.roomId}</span>
                </div>
                <div className="room-details__item">
                    <span className="room-details__label">Password</span>
                    <span className="room-details__value room-details__value--mono">{room.password}</span>
                </div>
            </div>
            <button className={`copy-btn copy-btn--block ${copied ? "copy-btn--copied" : ""}`} onClick={handleCopy}>
                {copied ? "Copied!" : "Copy Room ID & Password"}
            </button>
        </div>
    );
}

export default RoomDetails;
