import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/CursuriPage.css';

const CursuriPage = () => {
  const [cursuri, setCursuri] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [numeCurs, setNumeCurs] = useState('');
  const [numarCredite, setNumarCredite] = useState('');
  const [mesaj, setMesaj] = useState('');

  useEffect(() => {
    fetchCursuri();
  }, []);

  const fetchCursuri = () => {
    axios.get('http://localhost:8080/api/curs')
      .then(res => setCursuri(res.data))
      .catch(err => console.error('Eroare la încărcarea cursurilor:', err));
  };

  const handleAdaugaCurs = () => {
    if (!numeCurs || !numarCredite) {
      setMesaj('Completează toate câmpurile.');
      return;
    }

    axios.post('http://localhost:8080/api/curs', {
      nume: numeCurs,
      numarCredite: parseInt(numarCredite)
    })
      .then(() => {
        setMesaj('Curs adăugat cu succes!');
        fetchCursuri();
        setNumeCurs('');
        setNumarCredite('');
        setShowModal(false);
      })
      .catch(err => {
        console.error('Eroare la adăugare curs:', err);
        setMesaj('Eroare la adăugare.');
      });
  };

  return (
    <div className="cursuri-container">
      <header className="cursuri-header">
        <h1>Cursuri</h1>
        <button className="add-button" onClick={() => setShowModal(true)}>Adaugă Curs</button>
      </header>

      {mesaj && <div className="mesaj">{mesaj}</div>}

      <div className="cursuri-grid">
        {cursuri.map((curs) => (
          <div key={curs.id} className="curs-card">
            <h3>{curs.nume}</h3>
            <p>{curs.numarCredite} credite</p>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Adaugă Curs Nou</h2>
            <input
              type="text"
              placeholder="Nume curs"
              value={numeCurs}
              onChange={(e) => setNumeCurs(e.target.value)}
            />
            <input
              type="number"
              placeholder="Număr credite"
              value={numarCredite}
              onChange={(e) => setNumarCredite(e.target.value)}
              min="1"
              max="30"
            />
            <div className="modal-buttons">
              <button onClick={handleAdaugaCurs} className="save-button">Salvează</button>
              <button onClick={() => setShowModal(false)} className="cancel-button">Renunță</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CursuriPage;
