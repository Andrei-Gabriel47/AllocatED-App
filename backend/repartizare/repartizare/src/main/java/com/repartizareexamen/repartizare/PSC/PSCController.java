package com.repartizareexamen.repartizare.PSC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/psc")
public class PSCController {

    private final PSCService pscService;

    @Autowired
    public PSCController(PSCService pscService) {
        this.pscService = pscService;
    }

    @GetMapping
    public List<PSC> findAll() {
        return pscService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addNewPSC(@RequestBody PSC psc) {
        try {
            pscService.addNewPSC(psc);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> addMultiplePSC(@RequestBody List<PSC> relatii) {
        try {
            pscService.addMultiplePSC(relatii);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @DeleteMapping(path = "{cursId}/{profesorId}/{studentMatricol}")
    public ResponseEntity<?> deletePSC(@PathVariable Integer cursId, @PathVariable Integer profesorId, @PathVariable String studentMatricol) {
        try {
            pscService.deletePSC(new PSCId(cursId, profesorId, studentMatricol));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(path = "{cursId}/{profesorId}/{studentMatricol}")
    public ResponseEntity<?> updatePSC(@PathVariable Integer cursId, @PathVariable Integer profesorId, @PathVariable String studentMatricol, @RequestBody PSC psc) {
        try {
            pscService.updatePSC(new PSCId(cursId, profesorId, studentMatricol), psc);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}