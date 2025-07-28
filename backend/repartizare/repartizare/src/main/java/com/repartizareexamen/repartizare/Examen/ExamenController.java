package com.repartizareexamen.repartizare.Examen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(path = "api/examen")
public class ExamenController {

    private final ExamenService examenService;

    @Autowired
    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @GetMapping
    public List<Examen> getAllExamene() {
        return examenService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addNewExamen(@RequestBody Examen examen) {
        try {
            examenService.addNewExamen(examen);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public void deleteExamen(@PathVariable Integer id) {
        examenService.deleteExamen(id);
    }

    @PutMapping(path = "{id}")
    public void updateExamen(@PathVariable Integer id, @RequestBody Examen examen) {
        examenService.updateExamen(id, examen);
    }

    @PutMapping("/{id}/sala/{salaId}")
    public ResponseEntity<?> seteazaSala(@PathVariable Integer id, @PathVariable Integer salaId) {
        try {
            examenService.seteazaSala(id, salaId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/ora")
    public ResponseEntity<?> seteazaOra(@PathVariable Integer id,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime ora) {
        try {
            examenService.seteazaOraExamen(id, ora);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
