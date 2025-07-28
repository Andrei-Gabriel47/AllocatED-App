package com.repartizareexamen.repartizare.Repartizare;

import com.repartizareexamen.repartizare.Examen.Examen;
import com.repartizareexamen.repartizare.Examen.ExamenRepository;
import com.repartizareexamen.repartizare.PSC.PSC;
import com.repartizareexamen.repartizare.PSC.PSCRepository;
import com.repartizareexamen.repartizare.Sala.Sala;
import com.repartizareexamen.repartizare.Sala.SalaRepository;
import com.repartizareexamen.repartizare.Student.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepartizareService {

    private static final Logger logger = LoggerFactory.getLogger(RepartizareService.class);

    private final RepartizareRepository repartizareRepository;
    private final ExamenRepository examenRepository;
    private final SalaRepository salaRepository;
    private final PSCRepository pscRepository;

    @Autowired
    public RepartizareService(RepartizareRepository repartizareRepository,
                              ExamenRepository examenRepository,
                              SalaRepository salaRepository,
                              PSCRepository pscRepository) {
        this.repartizareRepository = repartizareRepository;
        this.examenRepository = examenRepository;
        this.salaRepository = salaRepository;
        this.pscRepository = pscRepository;
    }

    private int calculeazaCapacitateSala(Sala sala) {
        return sala.getNumarRanduri() * sala.getLocuriPerRand() * 2;
    }

    private Sala gasesteSalaPotrivita(List<Sala> saliDisponibile, int numarStudenti, LocalDate dataExamen, int durataExamen) {
        for (Sala sala : saliDisponibile) {
            int capacitateSala = calculeazaCapacitateSala(sala);
            if (capacitateSala >= numarStudenti) {
                // Caută dacă există un interval orar disponibil în ziua respectivă
                Optional<LocalTime> oraDisponibila = gasesteOraDisponibilaSala(sala, durataExamen, dataExamen);
                if (oraDisponibila.isPresent()) {
                    return sala;
                }
            }
        }
        return null; // Nu am găsit nicio sală potrivită
    }

    private Optional<LocalTime> gasesteOraDisponibilaSala(Sala sala, int durataExamen, LocalDate data) {
        LocalTime oraStart = LocalTime.of(8, 0);
        LocalTime oraSfarsit = LocalTime.of(21, 0);

        List<Examen> exameneInSala = examenRepository.findAll().stream()
                .filter(e -> e.getSala() != null && e.getSala().getId().equals(sala.getId()))
                .filter(e -> e.getData().equals(data))
                .filter(e -> e.getOra() != null)
                .collect(Collectors.toList());

        while (oraStart.plusMinutes(durataExamen).isBefore(oraSfarsit.plusMinutes(1))) {
            final LocalTime oraCurenta = oraStart;  // 👈 Copiem oraStart în oraCurenta (final)

            boolean suprapune = exameneInSala.stream()
                    .anyMatch(e -> seSuprapune(e.getOra(), e.getDurataInMinute() , oraCurenta, durataExamen));

            if (!suprapune) {
                return Optional.of(oraCurenta);
            }

            oraStart = oraStart.plusMinutes(15);
        }

        return Optional.empty();
    }



    public void genereazaRepartizareClasica(Integer examenId) {
        Examen examen = examenRepository.findById(examenId).orElseThrow(() -> new RuntimeException("Examenul nu există."));
        List<Student> studenti = pscRepository.findAll().stream()
                .filter(psc -> psc.getCurs().getId().equals(examen.getCurs().getId()))
                .map(PSC::getStudent).distinct()
                .sorted(Comparator.comparing(Student::getNume).thenComparing(Student::getPrenume)).collect(Collectors.toList());

        if (studenti.isEmpty())
            throw new RuntimeException("Nu există studenți înscriși pentru acest examen.");
        List<Sala> saliDisponibile = salaRepository.findAll().stream()
                .sorted(Comparator.comparingInt(this::calculeazaCapacitateSala)).collect(Collectors.toList());

        final int numarStudenti = studenti.size();
        final int durataExamen = examen.getDurataInMinute() ;
        LocalDate dataExamen = examen.getData();
        Sala salaAleasa = null; LocalTime oraAleasa = null;

        for (Sala sala : saliDisponibile) {
            if (calculeazaCapacitateSala(sala) >= numarStudenti) {
                Optional<LocalTime> oraDisponibila = gasesteOraDisponibilaSala(sala, durataExamen, dataExamen);
                if (oraDisponibila.isPresent()) {
                    salaAleasa = sala; oraAleasa = oraDisponibila.get(); break;
                }
            }
        }
        if (salaAleasa == null)
            throw new RuntimeException("Nu se poate organiza examenul în această zi.");
        examen.setSala(salaAleasa); examen.setData(dataExamen); examen.setOra(oraAleasa); examenRepository.save(examen);
        repartizeazaStudenti(studenti, salaAleasa, examen, oraAleasa);
        logger.info("Repartizare clasică completă: {} studenți în sala {} la ora {}.", studenti.size(), salaAleasa.getId(), oraAleasa);
    }

    private Optional<LocalTime> gasesteOraDisponibilaSalaDouaTure(Sala sala, int durataExamen, LocalDate data) {
        LocalTime oraStart = LocalTime.of(8, 0);
        LocalTime oraSfarsit = LocalTime.of(21, 0);

        List<Examen> exameneInSala = examenRepository.findAll().stream()
                .filter(e -> e.getSala() != null && e.getSala().getId().equals(sala.getId()))
                .filter(e -> e.getData().equals(data))
                .filter(e -> e.getOra() != null)
                .collect(Collectors.toList());

        while (oraStart.plusMinutes(durataExamen * 2).isBefore(oraSfarsit.plusMinutes(1))) {
            final LocalTime oraCurenta = oraStart;

            boolean suprapune = exameneInSala.stream()
                    .anyMatch(e ->
                            seSuprapune(e.getOra(), e.getDurataInMinute() +15, oraCurenta, durataExamen) || // suprapunere prima tură
                                    seSuprapune(e.getOra(), e.getDurataInMinute() , oraCurenta.plusMinutes(durataExamen), durataExamen) // suprapunere a doua tură
                    );

            if (!suprapune) {
                return Optional.of(oraCurenta);
            }

            oraStart = oraStart.plusMinutes(15);
        }

        return Optional.empty();
    }

    private void repartizeazaStudenti(List<Student> studenti, Sala sala, Examen examen, LocalTime oraTura) {
        int randuri = sala.getNumarRanduri();
        int locuriPeRand = sala.getLocuriPerRand();
        int indexStudent = 0;
        for (int rand = 1; rand <= randuri && indexStudent < studenti.size(); rand++) {
            for (int loc = 1; loc <= locuriPeRand && indexStudent < studenti.size(); loc++) {
                Student student = studenti.get(indexStudent++);
                Repartizare r = new Repartizare();
                r.setId(null);
                r.setExamen(examen);
                r.setStudent(student);
                r.setRand("R" + rand);
                r.setLoc(loc);
                r.setParteAmfiteatru("stanga");
                r.setSala(sala);
                r.setOra(oraTura);
                repartizareRepository.save(r);
            }
            for (int loc = 1; loc <= locuriPeRand && indexStudent < studenti.size(); loc++) {
                Student student = studenti.get(indexStudent++);
                Repartizare r = new Repartizare();
                r.setId(null);
                r.setExamen(examen);
                r.setStudent(student);
                r.setRand("R" + rand);
                r.setLoc(loc);
                r.setParteAmfiteatru("dreapta");
                r.setSala(sala);
                r.setOra(oraTura);
                repartizareRepository.save(r);
            }
        }
    }


    public void genereazaRepartizareDouaTure(Integer examenId) {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examenul nu există."));

        // 1. Găsim și sortăm studenții
        List<Student> studenti = pscRepository.findAll().stream()
                .filter(psc -> psc.getCurs().getId().equals(examen.getCurs().getId()))
                .map(PSC::getStudent)
                .distinct()
                .sorted(Comparator.comparing(Student::getNume).thenComparing(Student::getPrenume))
                .collect(Collectors.toList());

        if (studenti.isEmpty()) {
            throw new RuntimeException("Nu există studenți înscriși pentru acest examen.");
        }

        // 2. Împărțim în două ture
        final int jumatate = (int) Math.ceil(studenti.size() / 2.0);
        List<Student> primaTura = studenti.subList(0, jumatate);
        List<Student> aDouaTura = studenti.subList(jumatate, studenti.size());

        // 3. Pregătim datele examenului
        final int durataExamen = examen.getDurataInMinute() ; // durata examen + pauză
        LocalDate dataExamen = examen.getData();

        // 4. Căutăm o sală pentru o singură tură
        List<Sala> saliDisponibile = salaRepository.findAll().stream()
                .sorted(Comparator.comparingInt(this::calculeazaCapacitateSala))
                .collect(Collectors.toList());

        Sala salaAleasa = null;
        LocalTime oraStartPrimaTura = null;

        outerLoop:
        for (Sala sala : saliDisponibile) {
            if (calculeazaCapacitateSala(sala) >= primaTura.size()) {
                // căutăm ora pentru prima tură
                Optional<LocalTime> oraDisponibila = gasesteOraDisponibilaSalaDouaTure(sala, durataExamen, dataExamen);
                if (oraDisponibila.isPresent()) {
                    salaAleasa = sala;
                    oraStartPrimaTura = oraDisponibila.get();
                    break outerLoop;
                }
            }
        }

        if (salaAleasa == null) {
            throw new RuntimeException("Nu se poate organiza examenul în această zi pentru două ture. Încercați o altă dată.");
        }

        // 5. Salvăm datele examenului
        examen.setSala(salaAleasa);
        examen.setData(dataExamen);
        examen.setOra(oraStartPrimaTura);
        examenRepository.save(examen);

        // 6. Repartizăm studenții
        repartizeazaStudenti(primaTura, salaAleasa, examen, oraStartPrimaTura);
        repartizeazaStudenti(aDouaTura, salaAleasa, examen, oraStartPrimaTura.plusMinutes(durataExamen));

        logger.info("Repartizare în două ture completă: {} studenți în sala {} la ora {}.", studenti.size(), salaAleasa.getId(), oraStartPrimaTura);
    }
    private Optional<LocalTime> gasesteOraDisponibilaPentruSaliMultiple(List<Sala> sali, int durataExamen, LocalDate data) {
        LocalTime oraStart = LocalTime.of(8, 0);
        LocalTime oraSfarsit = LocalTime.of(21, 0);

        // Corect aici: Map<String, List<Examen>>
        Map<String, List<Examen>> examenePeSali = examenRepository.findAll().stream()
                .filter(e -> e.getSala() != null)
                .filter(e -> e.getData().equals(data))
                .collect(Collectors.groupingBy(e -> e.getSala().getId()));

        while (oraStart.plusMinutes(durataExamen).isBefore(oraSfarsit.plusMinutes(1))) {
            final LocalTime oraCurenta = oraStart;

            boolean toateSuntLibere = sali.stream()
                    .allMatch(sala -> {
                        List<Examen> exameneInSala = examenePeSali.getOrDefault(sala.getId(), Collections.emptyList());
                        return exameneInSala.stream()
                                .noneMatch(e -> seSuprapune(e.getOra(), e.getDurataInMinute() , oraCurenta, durataExamen));
                    });

            if (toateSuntLibere) {
                return Optional.of(oraCurenta);
            }

            oraStart = oraStart.plusMinutes(15);
        }

        return Optional.empty();
    }


    private void repartizeazaStudentiPeSali(List<Student> studenti, List<Sala> sali, Examen examen, LocalTime ora) {
        int indexStudent = 0;

        for (Sala sala : sali) {
            int capacitateSala = calculeazaCapacitateSala(sala);
            int randuri = sala.getNumarRanduri();
            int locuriPeRand = sala.getLocuriPerRand();

            for (int rand = 1; rand <= randuri && indexStudent < studenti.size(); rand++) {
                // Stânga
                for (int loc = 1; loc <= locuriPeRand && indexStudent < studenti.size(); loc++) {
                    Student student = studenti.get(indexStudent++);
                    Repartizare r = new Repartizare();
                    r.setId(null);
                    r.setExamen(examen);
                    r.setStudent(student);
                    r.setRand("R" + rand);
                    r.setLoc(loc);
                    r.setParteAmfiteatru("stanga");
                    r.setSala(sala);
                    r.setOra(ora);
                    repartizareRepository.save(r);
                }
                // Dreapta
                for (int loc = 1; loc <= locuriPeRand && indexStudent < studenti.size(); loc++) {
                    Student student = studenti.get(indexStudent++);
                    Repartizare r = new Repartizare();
                    r.setId(null);
                    r.setExamen(examen);
                    r.setStudent(student);
                    r.setRand("R" + rand);
                    r.setLoc(loc);
                    r.setParteAmfiteatru("dreapta");
                    r.setSala(sala);
                    r.setOra(ora);
                    repartizareRepository.save(r);
                }
            }

            if (indexStudent >= studenti.size()) {
                break; // toți studenții au fost repartizați
            }
        }
    }


    public void genereazaRepartizareSaliMultiple(Integer examenId) {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examenul nu există."));

        // 1. Găsim și sortăm studenții
        List<Student> studenti = pscRepository.findAll().stream()
                .filter(psc -> psc.getCurs().getId().equals(examen.getCurs().getId()))
                .map(PSC::getStudent)
                .distinct()
                .sorted(Comparator.comparing(Student::getNume).thenComparing(Student::getPrenume))
                .collect(Collectors.toList());

        if (studenti.isEmpty()) {
            throw new RuntimeException("Nu există studenți înscriși pentru acest examen.");
        }

        // 2. Luăm sălile disponibile, sortate crescător
        List<Sala> saliDisponibile = salaRepository.findAll().stream()
                .sorted(Comparator.comparingInt(this::calculeazaCapacitateSala))
                .collect(Collectors.toList());

        final int durataExamen = examen.getDurataInMinute() ;
        LocalDate dataExamen = examen.getData();

        // 3. Căutăm o oră de început validă comună
        LocalTime oraStart = gasesteOraDisponibilaPentruSaliMultiple(saliDisponibile, durataExamen, dataExamen)
                .orElseThrow(() -> new RuntimeException("Nu există suficiente săli disponibile pentru această zi. Încercați altă dată."));

        // 4. Setăm examenul
        examen.setData(dataExamen);
        examen.setOra(oraStart);
        examenRepository.save(examen);

        // 5. Repartizăm studenții în săli
        repartizeazaStudentiPeSali(studenti, saliDisponibile, examen, oraStart);

        logger.info("Repartizare pe săli multiple completă: {} studenți în {} săli la ora {}.", studenti.size(), saliDisponibile.size(), oraStart);
    }

    private Examen gasesteExamenSauArunca(Integer examenId) {
        return examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examenul nu există."));
    }

    private List<Student> gasesteStudentiPentruExamen(Examen examen) {
        return pscRepository.findAll().stream()
                .filter(psc -> psc.getCurs().getId().equals(examen.getCurs().getId()))
                .map(PSC::getStudent)
                .distinct()
                .sorted(Comparator.comparing(Student::getNume).thenComparing(Student::getPrenume))
                .collect(Collectors.toList());
    }

    private Sala gasesteSalaDisponibila(int numarStudenti, int durata, LocalDate data, LocalTime ora) {
        List<Sala> saliDisponibile = salaRepository.findAll();
        saliDisponibile.sort(Comparator.comparingInt(Sala::getCapacitateTotala));

        for (Sala sala : saliDisponibile) {
            if (sala.getCapacitateTotala() >= numarStudenti && esteSalaDisponibila(sala, durata, data, ora)) {
                return sala;
            }
        }
        return null;
    }

    private boolean esteSalaDisponibila(Sala sala, int durata, LocalDate data, LocalTime ora) {
        return examenRepository.findAll().stream()
                .filter(e -> e.getSala() != null && e.getSala().getId().equals(sala.getId()))
                .filter(e -> e.getData().equals(data))
                .noneMatch(e -> seSuprapune(e.getOra(), e.getDurataInMinute(), ora, durata));
    }

    private boolean seSuprapune(LocalTime ora1, int durata1, LocalTime ora2, int durata2) {
        if (ora1 == null || ora2 == null) {
            return false; // Dacă vreuna dintre ore e null, nu avem suprapunere
        }
        LocalTime sfarsit1 = ora1.plusMinutes(durata1);
        LocalTime sfarsit2 = ora2.plusMinutes(durata2);
        return !(sfarsit1.isBefore(ora2) || sfarsit2.isBefore(ora1));
    }




    public List<Repartizare> gasesteRepartizareDupaExamen(Integer examenId) {
        return repartizareRepository.findAll()
                .stream()
                .filter(r -> r.getExamen().getId().equals(examenId))
                .collect(Collectors.toList());
    }
}
