import type { RoomResponse } from "../types/RoomResponse";

function RoomDetails({ room }: { room: RoomResponse }) {
    return (
        <div>
            <h1>{room.topic}</h1>
            <p>Max Players: {room.maxPlayers}</p>
            <p>Password: {room.password}</p>
            <p>Host Id: {room.hostId}</p>
            <p>Room Id: {room.roomId}</p>
        </div>
    )
}

export default RoomDetails;