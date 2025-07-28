import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import ExamenPage from './pages/ExamenPage';
import RepartizarePage from './pages/RepartizarePage';
import CursuriPage from './pages/CursuriPage';
import ProfesoriPage from './pages/ProfesoriPage';
import StudentiPage from './pages/StudentiPage';
import AsocieriPage from './pages/AsocieriPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/examen" element={<ExamenPage />} />
        <Route path="/repartizare" element={<RepartizarePage />} />
        <Route path="/cursuri" element={<CursuriPage />} />
        <Route path="/profesori" element={<ProfesoriPage />} />
        <Route path="/studenti" element={<StudentiPage />} />
        <Route path="/asocieri" element={<AsocieriPage />} />
      </Routes>
    </Router>
  );
}

export default App;
