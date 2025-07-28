import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/StudentiPage.css'; // Îl facem imediat după

const StudentiPage = () => {
  const [studenti, setStudenti] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [nume, setNume] = useState('');
  const [prenume, setPrenume] = useState('');
  const [matricol, setMatricol] = useState('');
  const [varsta, setVarsta] = useState('');
  const [grupa, setGrupa] = useState('');
  const [mesaj, setMesaj] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchStudenti();
  }, []);

  const fetchStudenti = () => {
    axios.get('http://localhost:8080/api/student')
      .then(res => setStudenti(res.data))
      .catch(err => console.error('Eroare la încărcarea studenților:', err));
  };

  const handleAdaugaStudent = () => {
    if (!nume || !prenume || !matricol || !varsta || !grupa) {
      setMesaj('Completează toate câmpurile.');
      return;
    }

    axios.post('http://localhost:8080/api/student', {
      matricol: matricol,
      nume: nume,
      prenume: prenume,
      varsta: parseInt(varsta),
      grupa: grupa
    })
      .then(() => {
        setMesaj('Student adăugat cu succes!');
        fetchStudenti();
        setNume('');
        setPrenume('');
        setMatricol('');
        setVarsta('');
        setGrupa('');
        setShowModal(false);
      })
      .catch(err => {
        console.error('Eroare la adăugare student:', err);
        setMesaj('Eroare la adăugare.');
      });
  };

  return (
    <div className="studenti-container">
      <header className="studenti-header">
        <h1>Studenți</h1>
        <nav className="nav-menu">
          <button onClick={() => navigate('/examen')} className="nav-button">Creare Examen</button>
          <button onClick={() => navigate('/repartizare')} className="nav-button">Repartizare Studenți</button>
          <button onClick={() => navigate('/cursuri')} className="nav-button">Adaugă Curs</button>
          <button onClick={() => navigate('/profesori')} className="nav-button">Adaugă Profesor</button>
          <button onClick={() => navigate('/studenti')} className="nav-button">Adaugă Student</button>
          <button onClick={() => navigate('/asocieri')} className="nav-button">Asocieri</button>
        </nav>
        <button className="add-button" onClick={() => setShowModal(true)}>Adaugă Student</button>
      </header>

      {mesaj && <div className="mesaj">{mesaj}</div>}

      <div className="studenti-grid">
        {studenti.map((stud) => (
          <div key={stud.matricol} className="student-card">
            <h3>{stud.nume} {stud.prenume}</h3>
            <p>Matricol: {stud.matricol}</p>
            <p>Vârstă: {stud.varsta}</p>
            <p>Grupa: {stud.grupa}</p>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Adaugă Student Nou</h2>
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
              type="text"
              placeholder="Matricol"
              value={matricol}
              onChange={(e) => setMatricol(e.target.value)}
            />
            <input
              type="number"
              placeholder="Vârstă"
              value={varsta}
              onChange={(e) => setVarsta(e.target.value)}
            />
            <input
              type="text"
              placeholder="Grupă"
              value={grupa}
              onChange={(e) => setGrupa(e.target.value)}
            />
            <div className="modal-buttons">
              <button onClick={handleAdaugaStudent} className="save-button">Salvează</button>
              <button onClick={() => setShowModal(false)} className="cancel-button">Renunță</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default StudentiPage;
