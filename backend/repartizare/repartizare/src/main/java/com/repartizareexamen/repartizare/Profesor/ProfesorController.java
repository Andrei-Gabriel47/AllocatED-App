package com.repartizareexamen.repartizare.Profesor;

import com.repartizareexamen.repartizare.Student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/profesor")
public class ProfesorController {

    private final ProfesorService profesorService;

    @Autowired
    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }


    @GetMapping
    public List<Profesor> findAll(){
        return profesorService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addNewProfesor(@RequestBody Profesor profesor) {
        try {
            profesorService.addNewProfesor(profesor);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> addMultipleProfesori(@RequestBody List<Profesor> profesori) {
        try {
            profesorService.addMultipleProfesori(profesori);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @DeleteMapping(path = "{id}")
    public void deleteProfesor(@PathVariable Integer id){
        profesorService.deleteProfesor(id);
    }

    @PutMapping(path = "{id}")
    public void updateProfesor(@PathVariable Integer id, @RequestBody Profesor profesor){
        profesorService.updateProfesor(id,profesor);
    }
}
