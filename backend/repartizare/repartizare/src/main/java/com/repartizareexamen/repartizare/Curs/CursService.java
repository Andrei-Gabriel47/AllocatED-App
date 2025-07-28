package com.repartizareexamen.repartizare.Curs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursService {
    private static final Logger logger = LoggerFactory.getLogger(CursService.class);

    private final CursRepository cursRepository;

    @Autowired
    public CursService(CursRepository cursRepository) {
        this.cursRepository = cursRepository;
    }

    public List<Curs> findAll() {
        return cursRepository.findAll();
    }

    public void deleteCurs(Integer id) {
        cursRepository.deleteById(id);
    }

    public void addNewCurs(Curs curs) {

            logger.info("Adaugam curusl "+ curs.getId() + " cu numele " + curs.getNume());
            cursRepository.save(curs);

    }

    public void updateCurs(Integer id, Curs curs) {
        Optional<Curs> cursVechi = cursRepository.findById(id);
        if(cursVechi.isPresent()){
            Curs cursNou = cursVechi.get();
            cursNou.setNume(curs.getNume());
            cursNou.setNumarCredite(curs.getNumarCredite());
            cursRepository.save(cursNou);
        }
        else{
            throw new RuntimeException("Cursul cu ID-ul " + id + " nu există.");
        }
    }
}
