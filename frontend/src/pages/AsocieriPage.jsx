import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/AsocieriPage.css';

const AsocieriPage = () => {
  const [asocieri, setAsocieri] = useState([]);
  const [cursuri, setCursuri] = useState([]);
  const [profesori, setProfesori] = useState([]);
  const [studenti, setStudenti] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const [cursId, setCursId] = useState('');
  const [profesorId, setProfesorId] = useState('');
  const [studentMatricol, setStudentMatricol] = useState('');
  const [situatieStudent, setSituatieStudent] = useState('');
  const [mesaj, setMesaj] = useState('');

  useEffect(() => {
    fetchAsocieri();
    fetchCursuri();
    fetchProfesori();
    fetchStudenti();
  }, []);

  const fetchAsocieri = () => {
    axios.get('http://localhost:8080/api/psc')
      .then(res => setAsocieri(res.data))
      .catch(err => console.error('Eroare la încărcare asocieri:', err));
  };

  const fetchCursuri = () => {
    axios.get('http://localhost:8080/api/curs')
      .then(res => setCursuri(res.data))
      .catch(err => console.error('Eroare la încărcare cursuri:', err));
  };

  const fetchProfesori = () => {
    axios.get('http://localhost:8080/api/profesor')
      .then(res => setProfesori(res.data))
      .catch(err => console.error('Eroare la încărcare profesori:', err));
  };

  const fetchStudenti = () => {
    axios.get('http://localhost:8080/api/student')
      .then(res => setStudenti(res.data))
      .catch(err => console.error('Eroare la încărcare studenți:', err));
  };

  const handleAdaugaAsociere = () => {
    if (!cursId || !profesorId || !studentMatricol || !situatieStudent) {
      setMesaj('Completează toate câmpurile.');
      return;
    }

    const asociere = {
      id: {
        cursId: parseInt(cursId),
        profesorId: parseInt(profesorId),
        studentMatricol: studentMatricol
      },
      curs: { id: parseInt(cursId) },
      profesor: { id: parseInt(profesorId) },
      student: { matricol: studentMatricol },
      situatieStudent: situatieStudent
    };

    axios.post('http://localhost:8080/api/psc', asociere)
      .then(() => {
        setMesaj('Asociere adăugată cu succes!');
        fetchAsocieri();
        setShowModal(false);
        resetForm();
      })
      .catch(err => {
        console.error('Eroare la adăugare asociere:', err);
        setMesaj('Eroare la adăugare.');
      });
  };

  const resetForm = () => {
    setCursId('');
    setProfesorId('');
    setStudentMatricol('');
    setSituatieStudent('');
  };

  return (
    <div className="asocieri-container">
      <header className="asocieri-header">
        <h1>Asocieri Curs – Profesor – Student</h1>
        <button className="add-button" onClick={() => setShowModal(true)}>Adaugă Asociere</button>
      </header>

      {mesaj && <div className="mesaj">{mesaj}</div>}

      <div className="asocieri-grid">
        {asocieri.map((psc, idx) => (
          <div key={idx} className="asociere-card">
            <h3>Curs: {psc.curs?.nume || '-'}</h3>
            <p>Profesor: {psc.profesor?.nume} {psc.profesor?.prenume}</p>
            <p>Student: {psc.student?.nume} {psc.student?.prenume} ({psc.student?.matricol})</p>
            <p>Situatie: {psc.situatieStudent}</p>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Adaugă Asociere Nouă</h2>

            <select value={cursId} onChange={(e) => setCursId(e.target.value)}>
              <option value="">Selectează curs</option>
              {cursuri.map(c => (
                <option key={c.id} value={c.id}>{c.nume}</option>
              ))}
            </select>

            <select value={profesorId} onChange={(e) => setProfesorId(e.target.value)}>
              <option value="">Selectează profesor</option>
              {profesori.map(p => (
                <option key={p.id} value={p.id}>{p.nume} {p.prenume}</option>
              ))}
            </select>

            <select value={studentMatricol} onChange={(e) => setStudentMatricol(e.target.value)}>
              <option value="">Selectează student</option>
              {studenti.map(s => (
                <option key={s.matricol} value={s.matricol}>{s.nume} {s.prenume} ({s.matricol})</option>
              ))}
            </select>

            <input
              type="text"
              placeholder="Situație student (ex: prezent, absent)"
              value={situatieStudent}
              onChange={(e) => setSituatieStudent(e.target.value)}
            />

            <div className="modal-buttons">
              <button onClick={handleAdaugaAsociere} className="save-button">Salvează</button>
              <button onClick={() => { setShowModal(false); resetForm(); }} className="cancel-button">Renunță</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AsocieriPage;
