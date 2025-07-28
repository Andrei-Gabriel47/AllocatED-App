import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/HomePage.css';

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="homepage-container">
      <header className="homepage-header">
        <h1 className="app-title">AllocatED</h1>
        <nav className="nav-menu">
          <button onClick={() => navigate('/examen')} className="nav-button">Creare Examen</button>
          <button onClick={() => navigate('/repartizare')} className="nav-button">Repartizare Studenți</button>
          <button onClick={() => navigate('/cursuri')} className="nav-button">Adaugă Curs</button>
          <button onClick={() => navigate('/profesori')} className="nav-button">Adaugă Profesor</button>
          <button onClick={() => navigate('/studenti')} className="nav-button">Adaugă Student</button>
          <button onClick={() => navigate('/asocieri')} className="nav-button">Asocieri Curs-Student</button>
        </nav>
      </header>

      <main className="homepage-content">
        <div className="welcome-box">
          <h2>Bun venit în aplicația <span className="highlight">AllocatED</span></h2>
          <p className="homepage-text">
            Administrează ușor cursurile, profesorii, studenții și planifică automat repartizarea lor la examene.
          </p>
        </div>
      </main>

      <footer className="homepage-footer">
        <p>© {new Date().getFullYear()} AlocatED • Toate drepturile rezervate</p>
      </footer>
    </div>
  );
};

export default HomePage;
