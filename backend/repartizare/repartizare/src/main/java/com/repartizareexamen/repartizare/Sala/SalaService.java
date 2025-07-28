package com.repartizareexamen.repartizare.Sala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaService {

    private static final Logger logger = LoggerFactory.getLogger(SalaService.class);

    private final SalaRepository salaRepository;

    @Autowired
    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    public void addNewSala(Sala sala) {
        if (salaRepository.existsById(sala.getId())) {
            logger.warn("Sala există deja: {}", sala.getId());
            throw new RuntimeException("Sala există deja. Nu se poate adăuga.");
        } else {
            logger.info("Adăugăm sala cu ID-ul {} având {} rânduri și {} locuri pe rând",
                    sala.getId(), sala.getNumarRanduri(), sala.getLocuriPerRand());
            salaRepository.save(sala);
        }
    }

    public void deleteSala(String id) {
        salaRepository.deleteById(id);
    }

    public void updateSala(String id, Sala sala) {
        Optional<Sala> salaVechi = salaRepository.findById(id);
        if (salaVechi.isPresent()) {
            Sala salaNoua = salaVechi.get();
            salaNoua.setNumarRanduri(sala.getNumarRanduri());
            salaNoua.setLocuriPerRand(sala.getLocuriPerRand());
            salaRepository.save(salaNoua);
        } else {
            throw new RuntimeException("Sala cu ID-ul " + id + " nu există.");
        }
    }
}
