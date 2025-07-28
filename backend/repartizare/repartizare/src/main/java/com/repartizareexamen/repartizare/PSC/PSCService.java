package com.repartizareexamen.repartizare.PSC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PSCService {

    private static final Logger logger = LoggerFactory.getLogger(PSCService.class);

    private final PSCRepository pscRepository;

    @Autowired
    public PSCService(PSCRepository pscRepository) {
        this.pscRepository = pscRepository;
    }

    public List<PSC> findAll() {
        return pscRepository.findAll();
    }

    public void addNewPSC(PSC psc) {
        if (pscRepository.existsById(psc.getId())) {
            logger.warn("Relația PSC există deja: {}", psc.getId());
            throw new RuntimeException("Această relație PSC există deja. Nu se poate adăuga.");
        } else {
            logger.info("Adăugăm relația PSC pentru curs {} profesor {} student {}",
                    psc.getId().getCursId(), psc.getId().getProfesorId(), psc.getId().getStudentMatricol());
            pscRepository.save(psc);
        }
    }

    public void addMultiplePSC(List<PSC> relatii) {
        for (PSC psc : relatii) {
            if (!pscRepository.existsById(psc.getId())) {
                logger.info("Adăugăm relația PSC: curs {} - profesor {} - student {}",
                        psc.getId().getCursId(),
                        psc.getId().getProfesorId(),
                        psc.getId().getStudentMatricol());
                pscRepository.save(psc);
            } else {
                logger.warn("Relația PSC există deja: {}", psc.getId());
            }
        }
    }

    public void deletePSC(PSCId id) {
        if (pscRepository.existsById(id)) {
            pscRepository.deleteById(id);
            logger.info("Șters relația PSC pentru curs {} profesor {} student {}", id.getCursId(), id.getProfesorId(), id.getStudentMatricol());
        } else {
            throw new RuntimeException("Relația PSC cu acest ID nu există.");
        }
    }

    public void updatePSC(PSCId id, PSC psc) {
        Optional<PSC> existingPSC = pscRepository.findById(id);
        if (existingPSC.isPresent()) {
            PSC updatedPSC = existingPSC.get();
            updatedPSC.setSituatieStudent(psc.getSituatieStudent());
            pscRepository.save(updatedPSC);
            logger.info("Actualizată situația studentului pentru relația PSC curs {} profesor {} student {}", id.getCursId(), id.getProfesorId(), id.getStudentMatricol());
        } else {
            throw new RuntimeException("Relația PSC cu acest ID nu există.");
        }
    }
}
