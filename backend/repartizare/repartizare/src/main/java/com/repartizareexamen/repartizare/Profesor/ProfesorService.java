package com.repartizareexamen.repartizare.Profesor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorService {

    private static final Logger logger = LoggerFactory.getLogger(ProfesorService.class);

    private final ProfesorRepository profesorRepository;

    @Autowired
    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    public void addNewProfesor(Profesor profesor) {

            logger.info("Adaugam profesorul "+ profesor.getId() + " cu numele " + profesor.getNume() + " " + profesor.getPrenume());
            profesorRepository.save(profesor);

    }

    public void addMultipleProfesori(List<Profesor> profesori) {
        for (Profesor profesor : profesori) {
            logger.info("Adăugăm profesorul: {} {}", profesor.getNume(), profesor.getPrenume());

            // Poți face verificare dacă vrei să eviți duplicate:
            // if (!profesorRepository.existsById(profesor.getId())) {
            profesorRepository.save(profesor);
            // }
        }
    }


    public void deleteProfesor(Integer id) {
        profesorRepository.deleteById(id);
    }

    public void updateProfesor(Integer id, Profesor profesor) {
        Optional<Profesor> profVechi = profesorRepository.findById(id);
        if(profVechi.isPresent()){
            Profesor profNou = profVechi.get();
            profNou.setNume(profesor.getNume());
            profNou.setPrenume(profesor.getPrenume());
            profNou.setEmail(profesor.getEmail());

            profesorRepository.save(profNou);
        }
        else
            throw new RuntimeException("Profesorul cu ID-ul " + id + " nu există.");
    }
}
