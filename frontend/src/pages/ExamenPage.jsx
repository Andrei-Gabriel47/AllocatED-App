import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import '../styles/ExamenPage.css';

const ExamenPage = () => {
  const [examene, setExamene] = useState([]);
  const [cursuri, setCursuri] = useState([]);
  const [profesori, setProfesori] = useState([]);
  const [showPopup, setShowPopup] = useState(false);
  const [formData, setFormData] = useState({
    cursId: "",
    profesorId: "",
    data: "",
    durataInMinute: ""
  });

  const navigate = useNavigate();

  useEffect(() => {
    axios.get("http://localhost:8080/api/examen").then((res) => setExamene(res.data));
    axios.get("http://localhost:8080/api/curs").then((res) => setCursuri(res.data));
    axios.get("http://localhost:8080/api/profesor").then((res) => setProfesori(res.data));
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const examen = {
      curs: { id: parseInt(formData.cursId) },
      profesor: { id: parseInt(formData.profesorId) },
      data: formData.data,
      durataInMinute: parseInt(formData.durataInMinute)
    };
    try {
      await axios.post("http://localhost:8080/api/examen", examen);
      setShowPopup(false);
      setFormData({ cursId: "", profesorId: "", data: "", durataInMinute: "" });
      const res = await axios.get("http://localhost:8080/api/examen");
      setExamene(res.data);
    } catch (err) {
      console.error("Eroare la adăugare examen:", err);
    }
  };

  return (
    <div className="examen-page">
      {/* MENIU DE NAVIGARE */}
      <div className="profesori-header">
        <h1>Examene programate</h1>
        <nav className="nav-menu">
          <button onClick={() => navigate('/examen')} className="nav-button">Creare Examen</button>
          <button onClick={() => navigate('/repartizare')} className="nav-button">Repartizare Studenți</button>
          <button onClick={() => navigate('/cursuri')} className="nav-button">Adaugă Curs</button>
          <button onClick={() => navigate('/profesori')} className="nav-button">Adaugă Profesor</button>
          <button onClick={() => navigate('/studenti')} className="nav-button">Adaugă Student</button>
          <button onClick={() => navigate('/asocieri')} className="nav-button">Asocieri</button>
        </nav>
      </div>

      {/* TABEL CU EXAMENE */}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Curs</th>
            <th>Profesor</th>
            <th>Data</th>
            <th>Durata (minute)</th>
            <th>Sala</th>
            <th>Ora</th>
          </tr>
        </thead>
        <tbody>
          {examene.map((ex) => (
            <tr key={ex.id}>
              <td>{ex.id}</td>
              <td>{ex.curs?.nume}</td>
              <td>{ex.profesor?.nume} {ex.profesor?.prenume}</td>
              <td>{ex.data}</td>
              <td>{ex.durataInMinute}</td>
              <td>{ex.sala?.id || "-"}</td>
              <td>{ex.ora || "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* BUTON PENTRU ADAUGARE */}
      <button onClick={() => setShowPopup(true)}>Crează Examen</button>

      {/* POPUP ADAUGARE */}
      {showPopup && (
        <div className="popup">
          <div className="popup-content">
            <h2>Adaugă Examen</h2>
            <form onSubmit={handleSubmit}>
              <label>Curs:</label>
              <select name="cursId" value={formData.cursId} onChange={handleChange} required>
                <option value="">Selectează curs</option>
                {cursuri.map((c) => (
                  <option key={c.id} value={c.id}>{c.nume}</option>
                ))}
              </select>

              <label>Profesor:</label>
              <select name="profesorId" value={formData.profesorId} onChange={handleChange} required>
                <option value="">Selectează profesor</option>
                {profesori.map((p) => (
                  <option key={p.id} value={p.id}>{p.nume} {p.prenume}</option>
                ))}
              </select>

              <label>Data:</label>
              <input type="date" name="data" value={formData.data} onChange={handleChange} required />

              <label>Durata (minute):</label>
              <input type="number" name="durataInMinute" value={formData.durataInMinute} onChange={handleChange} required />

              <div className="popup-buttons">
                <button type="submit">Salvează</button>
                <button type="button" onClick={() => setShowPopup(false)}>Renunță</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default ExamenPage;
