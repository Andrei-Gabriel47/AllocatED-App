package com.repartizareexamen.repartizare.Repartizare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repartizare")
public class RepartizareController {

    private final RepartizareService repartizareService;

    @Autowired
    public RepartizareController(RepartizareService repartizareService) {
        this.repartizareService = repartizareService;
    }

    @PutMapping("/clasica/{examenId}")
    public ResponseEntity<?> repartizareClasica(@PathVariable Integer examenId) {
        try {
            repartizareService.genereazaRepartizareClasica(examenId);
            return ResponseEntity.ok("Repartizare clasică realizată cu succes.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/doua-ture/{examenId}")
    public ResponseEntity<?> repartizareDouaTure(@PathVariable Integer examenId) {
        try {
            repartizareService.genereazaRepartizareDouaTure(examenId);
            return ResponseEntity.ok("Repartizare în două ture realizată cu succes.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/sali-multiple/{examenId}")
    public ResponseEntity<?> repartizareSaliMultiple(@PathVariable Integer examenId) {
        try {
            repartizareService.genereazaRepartizareSaliMultiple(examenId);
            return ResponseEntity.ok("Repartizare pe săli multiple realizată cu succes.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/clasica/{examenId}")
    public ResponseEntity<Void> genereazaRepartizareClasica(@PathVariable Integer examenId) {
        repartizareService.genereazaRepartizareClasica(examenId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/douature/{examenId}")
    public ResponseEntity<Void> genereazaRepartizareDouaTure(@PathVariable Integer examenId) {
        repartizareService.genereazaRepartizareDouaTure(examenId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/salimultiple/{examenId}")
    public ResponseEntity<Void> genereazaRepartizareSaliMultiple(@PathVariable Integer examenId) {
        repartizareService.genereazaRepartizareSaliMultiple(examenId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/examen/{examenId}")
    public ResponseEntity<List<Repartizare>> obtineRepartizareExamen(@PathVariable Integer examenId) {
        return ResponseEntity.ok(repartizareService.gasesteRepartizareDupaExamen(examenId));
    }
}
