package com.repartizareexamen.repartizare.Examen;

import com.repartizareexamen.repartizare.Sala.Sala;
import com.repartizareexamen.repartizare.Sala.SalaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExamenService {

    private static final Logger logger = LoggerFactory.getLogger(ExamenService.class);

    private final ExamenRepository examenRepository;
    private final SalaRepository salaRepository;

    @Autowired
    public ExamenService(ExamenRepository examenRepository, SalaRepository salaRepository) {
        this.examenRepository = examenRepository;
        this.salaRepository = salaRepository;
    }

    public List<Examen> findAll() {
        return examenRepository.findAll();
    }

    public void addNewExamen(Examen examen) {

        logger.info("Adăugăm examenul cu ID-ul {}", examen.getId());
        examenRepository.save(examen);
    }

    public void deleteExamen(Integer id) {
        if (!examenRepository.existsById(id)) {
            throw new RuntimeException("Examenul cu ID-ul " + id + " nu există.");
        }
        examenRepository.deleteById(id);
    }

    public void updateExamen(Integer id, Examen examen) {
        Optional<Examen> examenVechi = examenRepository.findById(id);
        if (examenVechi.isPresent()) {
            Examen examenActualizat = examenVechi.get();
            examenActualizat.setCurs(examen.getCurs());
            examenActualizat.setProfesor(examen.getProfesor());
            examenActualizat.setData(examen.getData());
            examenActualizat.setOra(examen.getOra());
            examenActualizat.setSala(examen.getSala());
            examenActualizat.setDurataInMinute(examen.getDurataInMinute());
            examenActualizat.setRepartizat(examen.getRepartizat());
            examenRepository.save(examenActualizat);
        } else {
            throw new RuntimeException("Examenul cu ID-ul " + id + " nu există.");
        }
    }

    public void seteazaSala(Integer examenId, Integer salaId) {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examenul nu există"));
        Sala sala = salaRepository.findById(String.valueOf(salaId))
                .orElseThrow(() -> new RuntimeException("Sala nu există"));
        examen.setSala(sala);
        examenRepository.save(examen);
    }

    public void seteazaOraExamen(Integer examenId, LocalTime ora) {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examenul nu există"));
        examen.setOra(ora);
        examenRepository.save(examen);
    }
}
