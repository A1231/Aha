import { useState } from "react";
import type { RoomRequest } from "../types/RoomRequest";
import type { RoomResponse } from "../types/RoomResponse";
import Button from "./Button";

function RoomCreateForm({ setRoom }: { setRoom: (room: RoomResponse) => void }) {

    const [topic, setTopic] = useState("");
    const [hostName, setHostName] = useState("");
    const [maxPlayers, setMaxPlayers] = useState(0);

    async function handleSubmit(e: React.SubmitEvent<HTMLFormElement>){
        e.preventDefault();
        if (!hostName || !topic || !maxPlayers){
            alert("Please fill in all fields");
            return;
        }
        const room: RoomRequest = {
            hostName,
            topic,
            maxPlayers
        }
        const response = await fetch("http://localhost:8080/api/rooms", {
            method: "POST",
            body: JSON.stringify(room),
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include"
        })
        if (!response.ok){
            alert("Failed to create room");
            return;
        }
        const data = await response.json();
        setRoom(data);
        console.log(data);

       
    }
    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="hostName">Host Name</label>
            <input type="text" id="hostName" name="hostName" value={hostName} onChange={(e) => setHostName(e.target.value)} />
            <label htmlFor="topic">Topic</label>
            <input type="text" id="topic" name="topic" value={topic} onChange={(e) => setTopic(e.target.value)} />
            <label htmlFor="maxPlayers">Max Players</label>
            <input type="number" id="maxPlayers" name="maxPlayers" value={maxPlayers} 
            onChange={(e) => setMaxPlayers(parseInt(e.target.value))} min={2} max={10}/>
            <Button type="submit"> Create Room </Button>
        </form>
    )
}

export default RoomCreateForm;