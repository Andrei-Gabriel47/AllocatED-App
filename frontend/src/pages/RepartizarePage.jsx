import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import '../styles/RepartizarePage.css';

const RepartizarePage = () => {
  const [examene, setExamene] = useState([]);
  const [examenSelectat, setExamenSelectat] = useState('');
  const [modRepartizare, setModRepartizare] = useState('');
  const [grupa, setGrupa] = useState('');
  const [repartizari, setRepartizari] = useState([]);
  const [mesaj, setMesaj] = useState('');
  const [eroare, setEroare] = useState('');
  const [examenSelectatInfo, setExamenSelectatInfo] = useState(null);
  const [toastMessage, setToastMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('http://localhost:8080/api/examen')
      .then((res) => {
        const exameneNeRepartizate = res.data.filter(ex => !ex.repartizat);
        setExamene(exameneNeRepartizate);
      })
      .catch((err) => console.error(err));
  }, []);

  const handleExamenChange = (e) => {
    const selectedId = e.target.value;
    setExamenSelectat(selectedId);
    setRepartizari([]);

    const examenInfo = examene.find((ex) => ex.id.toString() === selectedId);
    setExamenSelectatInfo(examenInfo || null);
  };

  const handleRepartizare = async () => {
    if (!examenSelectat || !modRepartizare) return;

    try {
      await axios.post(`http://localhost:8080/api/repartizare/${modRepartizare}/${examenSelectat}`, {
        grupa: grupa || null
      });

      const response = await axios.get(`http://localhost:8080/api/repartizare/examen/${examenSelectat}`);
      setRepartizari(response.data);
      setMesaj('Repartizare realizată cu succes!');
      setEroare('');

      setExamene(prev => prev.filter(ex => ex.id.toString() !== examenSelectat));
      setExamenSelectat('');
    } catch (error) {
      console.error('Eroare la repartizare:', error);
      setEroare('Eroare la repartizare. Încearcă din nou.');
      setMesaj('');
    }
  };

  const exportToPDF = () => {
    const doc = new jsPDF();
    const now = new Date();
    const dataGenerare = `${now.getDate().toString().padStart(2, '0')}.${(now.getMonth()+1).toString().padStart(2, '0')}.${now.getFullYear()} ${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`;

    doc.setFont('times', 'bold');
    doc.setFontSize(22);
    doc.text('Repartizare Examen', doc.internal.pageSize.getWidth() / 2, 20, { align: 'center' });

    if (examenSelectatInfo) {
      doc.setFont('times', 'normal');
      doc.setFontSize(13);
      doc.text(`Materia: ${examenSelectatInfo.curs?.nume || '-'}`, 20, 40);
      doc.text(`Profesor: ${examenSelectatInfo.profesor?.nume || '-'} ${examenSelectatInfo.profesor?.prenume || ''}`, 20, 50);
      doc.text(`Data examenului: ${examenSelectatInfo.data || '-'}`, 20, 60);
      doc.text(`Sala: ${examenSelectatInfo.sala?.id || '-'}`, 20, 80);
      doc.text(`Data generare PDF : ${dataGenerare}`, 20, 90);
    } else {
      doc.text('Datele despre examen nu sunt disponibile.', 20, 40);
    }

    autoTable(doc, {
      startY: 100,
      head: [['Student', 'Rand', 'Loc', 'Parte', 'Sala', 'Ora Examen']],
      body: repartizari.map((r) => [
        `${r.student.nume} ${r.student.prenume}`,
        r.rand,
        r.loc,
        r.parteAmfiteatru,
        r.sala?.id || '-',
        r.ora || '-'
      ]),
      theme: 'striped',
      headStyles: {
        fillColor: [0, 102, 204],
        textColor: 255,
        fontStyle: 'bold',
      },
      bodyStyles: {
        fontSize: 10,
      },
      alternateRowStyles: {
        fillColor: [230, 240, 255],
      },
    });

    doc.save(`repartizare_${examenSelectatInfo?.curs?.nume || 'examen'}.pdf`);
    setToastMessage('✅ PDF exportat cu succes!');
  };

  useEffect(() => {
    if (toastMessage) {
      const timer = setTimeout(() => {
        setToastMessage('');
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [toastMessage]);

  return (
    <div className="repartizare-container">
      <div className="profesori-header">
        <h1>Repartizare Studenți</h1>
        <nav className="nav-menu">
          <button onClick={() => navigate('/examen')} className="nav-button">Creare Examen</button>
          <button onClick={() => navigate('/repartizare')} className="nav-button">Repartizare Studenți</button>
          <button onClick={() => navigate('/cursuri')} className="nav-button">Adaugă Curs</button>
          <button onClick={() => navigate('/profesori')} className="nav-button">Adaugă Profesor</button>
          <button onClick={() => navigate('/studenti')} className="nav-button">Adaugă Student</button>
          <button onClick={() => navigate('/asocieri')} className="nav-button">Asocieri</button>
        </nav>
      </div>

      {toastMessage && <div className="toast-notification">{toastMessage}</div>}

      <div className="form-section">
        <label>Alege examenul:</label>
        <select value={examenSelectat} onChange={handleExamenChange}>
          <option value="">-- Selectează --</option>
          {examene.map((ex) => (
            <option key={ex.id} value={ex.id}>
              {ex.curs?.nume} - {ex.profesor?.nume} {ex.profesor?.prenume} ({ex.data})
            </option>
          ))}
        </select>

        <label>Alege modul de repartizare:</label>
        <div className="radio-group">
          <label>
            <input
              type="radio"
              value="clasica"
              checked={modRepartizare === 'clasica'}
              onChange={(e) => setModRepartizare(e.target.value)}
            />
            Repartizare clasică
          </label>
          <label>
            <input
              type="radio"
              value="douature"
              checked={modRepartizare === 'douature'}
              onChange={(e) => setModRepartizare(e.target.value)}
            />
            Repartizare în două ture
          </label>
          <label>
            <input
              type="radio"
              value="salimultiple"
              checked={modRepartizare === 'salimultiple'}
              onChange={(e) => setModRepartizare(e.target.value)}
            />
            Repartizare în săli multiple
          </label>
        </div>

        <label>Grupa studenților (opțional):</label>
        <input
          type="text"
          value={grupa}
          onChange={(e) => setGrupa(e.target.value)}
          placeholder="Ex: IE34"
        />

        <button onClick={handleRepartizare} className="repartizare-button">
          Generează Repartizare
        </button>
      </div>

      {mesaj && <div className="success-message">{mesaj}</div>}
      {eroare && <div className="error-message">{eroare}</div>}

      {repartizari.length > 0 && (
        <div className="tabel-section">
          <h2>Rezultatul repartizării</h2>
          <table>
            <thead>
              <tr>
                <th>Student</th>
                <th>Rand</th>
                <th>Loc</th>
                <th>Parte</th>
                <th>Sala</th>
                <th>Ora Examen</th>
              </tr>
            </thead>
            <tbody>
              {repartizari.map((r, idx) => (
                <tr key={idx}>
                  <td>{r.student.nume} {r.student.prenume}</td>
                  <td>{r.rand}</td>
                  <td>{r.loc}</td>
                  <td>{r.parteAmfiteatru}</td>
                  <td>{r.sala?.id || '-'}</td>
                  <td>{r.ora || '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <button onClick={exportToPDF} className="export-button">
            Exportă PDF
          </button>
        </div>
      )}
    </div>
  );
};

export default RepartizarePage;
