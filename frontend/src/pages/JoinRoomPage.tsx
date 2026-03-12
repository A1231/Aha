import { useEffect, useState } from "react";
import Header from "../components/Header";
import JoinRoomForm from "../components/JoinRoomForm";
import PlayerList from "../components/PlayerList";
import type { RoomResponse } from "../types/RoomResponse";
import { useStomp } from "../components/StompProvider";
import { useNavigate } from "react-router-dom";

function JoinRoomPage() {
    const [room, setRoom] = useState<RoomResponse | null>(null);
    const [players, setPlayers] = useState<string[]>([]);
    const { connected, subscribe } = useStomp();
    const navigate = useNavigate();

    useEffect(() => {
        if (!connected || !room) return;
        const unsub1 = subscribe(`/topic/room/${room.roomId}`, (msg) => {
            const data = JSON.parse(msg.body);
            if (Array.isArray(data.players)) {
                setPlayers(data.players);
            }
        });

        const unsub2 = subscribe(`/topic/room/${room.roomId}/game-started`, (msg) => {
            if (JSON.parse(msg.body).message === "Game started") {
                navigate(`/game-started/${room.roomId}`);
            }
        });

        return () => { unsub1?.(); unsub2?.(); };
    }, [connected, room, subscribe, navigate]);

    return (
        <>
            <Header />
            <div className="page page--center">
                <div className="page__content">
                    {room ? (
                        <>
                            <div className="card text-center">
                                <h2 className="card__title">You're in!</h2>
                                <p className="text-muted">
                                    The host will start the game shortly. Sit tight!
                                </p>
                            </div>
                            <PlayerList players={players} />
                        </>
                    ) : (
                        <JoinRoomForm setRoom={setRoom} />
                    )}
                </div>
            </div>
        </>
    );
}

export default JoinRoomPage;
