package com.repartizareexamen.repartizare.Examen;

import com.repartizareexamen.repartizare.Curs.Curs;
import com.repartizareexamen.repartizare.Profesor.Profesor;
import com.repartizareexamen.repartizare.Sala.Sala;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "curs_id")
    private Curs curs;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    private LocalDate data;
    private LocalTime ora;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = true)
    private Sala sala;

    private Integer durataInMinute;

    private Boolean repartizat;

    public Boolean getRepartizat() {
        return repartizat;
    }

    public void setRepartizat(Boolean repartizat) {
        this.repartizat = repartizat;
    }

    // Constructori
    public Examen() {}

    public Examen(Integer id, Curs curs, Profesor profesor, LocalDate data, LocalTime ora, Sala sala, Integer durataInMinute, Boolean repartizat) {
        this.id = id;
        this.curs = curs;
        this.profesor = profesor;
        this.data = data;
        this.ora = ora;
        this.sala = sala;
        this.durataInMinute = durataInMinute;
        this.repartizat= repartizat;
    }

    // Getteri și setteri
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Curs getCurs() { return curs; }
    public void setCurs(Curs curs) { this.curs = curs; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getOra() { return ora; }
    public void setOra(LocalTime ora) { this.ora = ora; }

    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }

    public Integer getDurataInMinute() { return durataInMinute; }
    public void setDurataInMinute(Integer durataInMinute) { this.durataInMinute = durataInMinute; }
}
