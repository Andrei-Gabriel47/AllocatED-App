package com.repartizareexamen.repartizare.Repartizare;

import com.repartizareexamen.repartizare.Examen.Examen;
import com.repartizareexamen.repartizare.Sala.Sala;
import com.repartizareexamen.repartizare.Student.Student;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table
public class Repartizare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "examen_id", nullable = false)
    private Examen examen;
    private String rand;
    private Integer loc;
    private String parteAmfiteatru;
    @ManyToOne
    @JoinColumn(name="sala_id")
    private Sala sala;
    private LocalTime ora;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public Examen getExamen() {
        return examen;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public java.lang.String getRand() {
        return rand;
    }

    public void setRand(java.lang.String rand) {
        this.rand = rand;
    }

    public Integer getLoc() {
        return loc;
    }

    public void setLoc(Integer loc) {
        this.loc = loc;
    }

    public java.lang.String getParteAmfiteatru() {
        return parteAmfiteatru;
    }

    public void setParteAmfiteatru(java.lang.String parteAmfiteatru) {
        this.parteAmfiteatru = parteAmfiteatru;
    }

    public Repartizare() {
    }

    public Repartizare(Integer id, Student student, Examen examen, java.lang.String rand, Integer loc, java.lang.String parteAmfiteatru, Sala sala, LocalTime ora) {
        this.id = id;
        this.student = student;
        this.examen = examen;
        this.rand = rand;
        this.loc = loc;
        this.parteAmfiteatru = parteAmfiteatru;
        this.sala = sala;
        this.ora=ora;
    }


}
