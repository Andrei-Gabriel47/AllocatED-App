package com.repartizareexamen.repartizare.Curs;

import jakarta.persistence.*;

@Entity
@Table
public class Curs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nume;
    private Integer numarCredite;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getNumarCredite() {
        return numarCredite;
    }

    public void setNumarCredite(Integer numarCredite) {
        this.numarCredite = numarCredite;
    }

    public Curs(Integer id, String nume, Integer numarCredite) {
        this.id = id;
        this.nume = nume;
        this.numarCredite = numarCredite;
    }

    public Curs() {
    }

    @Override
    public String toString() {
        return nume;
    }
}
