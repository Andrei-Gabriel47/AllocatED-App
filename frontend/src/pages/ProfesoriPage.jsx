import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/ProfesoriPage.css'; // Îl facem imediat și pe acesta

const ProfesoriPage = () => {
  const [profesori, setProfesori] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [nume, setNume] = useState('');
  const [prenume, setPrenume] = useState('');
  const [email, setEmail] = useState('');
  const [mesaj, setMesaj] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchProfesori();
  }, []);

  const fetchProfesori = () => {
    axios.get('http://localhost:8080/api/profesor')
      .then(res => setProfesori(res.data))
      .catch(err => console.error('Eroare la încărcare profesori:', err));
  };

  const handleAdaugaProfesor = () => {
    if (!nume || !prenume || !email) {
      setMesaj('Completează toate câmpurile.');
      return;
    }

    axios.post('http://localhost:8080/api/profesor', {
      nume: nume,
      prenume: prenume,
      email: email
    })
      .then(() => {
        setMesaj('Profesor adăugat cu succes!');
        fetchProfesori();
        setNume('');
        setPrenume('');
        setEmail('');
        setShowModal(false);
      })
      .catch(err => {
        console.error('Eroare la adăugare profesor:', err);
        setMesaj('Eroare la adăugare.');
      });
  };

  return (
    <div className="profesori-container">
      <header className="profesori-header">
        <h1>Profesori</h1>
        <nav className="nav-menu">
          <button onClick={() => navigate('/examen')} className="nav-button">Creare Examen</button>
          <button onClick={() => navigate('/repartizare')} className="nav-button">Repartizare Studenți</button>
          <button onClick={() => navigate('/cursuri')} className="nav-button">Adaugă Curs</button>
          <button onClick={() => navigate('/profesori')} className="nav-button">Adaugă Profesor</button>
          <button onClick={() => navigate('/studenti')} className="nav-button">Adaugă Student</button>
          <button onClick={() => navigate('/asocieri')} className="nav-button">Asocieri</button>
        </nav>
        <button className="add-button" onClick={() => setShowModal(true)}>Adaugă Profesor</button>
      </header>

      {mesaj && <div className="mesaj">{mesaj}</div>}

      <div className="profesori-grid">
        {profesori.map((prof) => (
          <div key={prof.id} className="profesor-card">
            <h3>{prof.nume} {prof.prenume}</h3>
            <p>{prof.email}</p>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Adaugă Profesor Nou</h2>
            <input
              type="text"
              placeholder="Nume"
              value={nume}
              onChange={(e) => setNume(e.target.value)}
            />
            <input
              type="text"
              placeholder="Prenume"
              value={prenume}
              onChange={(e) => setPrenume(e.target.value)}
            />
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <div className="modal-buttons">
              <button onClick={handleAdaugaProfesor} className="save-button">Salvează</button>
              <button onClick={() => setShowModal(false)} className="cancel-button">Renunță</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfesoriPage;
