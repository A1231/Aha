import { BrowserRouter, Route, Routes } from "react-router-dom"
import LandingPage from "./pages/LandingPage"
import RoomCreationPage from "./pages/RoomCreationPage"
import GameStarted from "./pages/GameStarted"
import GameEnded from "./pages/GameEnded"
import JoinRoom from "./pages/JoinRoomPage"
import NotFound from "./pages/NotFound"
import StompProvider from "./components/StompProvider"




function App() {
  return (
    
    <BrowserRouter>
      <StompProvider>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/room-creation" element={<RoomCreationPage />} />
          <Route path="/game-started/:roomId" element={<GameStarted />} />
          <Route path="/game-ended/:roomId" element={<GameEnded />} />
          <Route path="/join-room" element={<JoinRoom />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </StompProvider>
    </BrowserRouter>
  );
}

export default App;
