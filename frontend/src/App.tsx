import { BrowserRouter, Route, Routes } from "react-router-dom"
import LandingPage from "./pages/LandingPage"
import RoomCreationPage from "./pages/RoomCreationPage"
import GameStarted from "./pages/GameStarted"
import GameEnded from "./pages/GameEnded"
import JoinRoom from "./pages/JoinRoom"
import NotFound from "./pages/NotFound"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/room-creation" element={<RoomCreationPage />} />
        <Route path="/game-started" element={<GameStarted />} />
        <Route path="/game-ended" element={<GameEnded />} />
        <Route path='/join-room' element={<JoinRoom/>} />
        <Route path='*' element={<NotFound />} />

      </Routes>
    </BrowserRouter>
  )
}

export default App
