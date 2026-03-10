import Header from "../components/Header";
import RoomCreateForm from "../components/RoomCreateForm";
import { useState } from "react";
import type { RoomResponse } from "../types/RoomResponse";
import RoomDetails from "../components/RoomDetails";
import AddQuestionSet from "../components/AddQuestionSet";


function RoomCreationPage() {
    const [room, setRoom] = useState<RoomResponse | null>(null);
    return (
        <>
        <Header />
        
        {room ? <RoomDetails room={room} /> : <RoomCreateForm setRoom={setRoom}/>}
        {room && <AddQuestionSet  />}
        </>
       
            
        
    )
}


export default RoomCreationPage;

