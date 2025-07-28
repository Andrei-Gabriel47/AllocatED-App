package com.repartizareexamen.repartizare.Curs;

import com.repartizareexamen.repartizare.Profesor.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/curs")
public class CursController {

    private final CursService cursService;

    @Autowired
    public CursController(CursService cursService) {
        this.cursService = cursService;
    }

    @GetMapping
    public List<Curs> findAll(){
        return cursService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addNewCurs(@RequestBody Curs curs) {
        try {
            cursService.addNewCurs(curs);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public void deleteCurs(@PathVariable Integer id){
        cursService.deleteCurs(id);
    }

    @PutMapping(path = "{id}")
    public void updateCurs(@PathVariable Integer id, @RequestBody Curs curs){
        cursService.updateCurs(id,curs);
    }

}
