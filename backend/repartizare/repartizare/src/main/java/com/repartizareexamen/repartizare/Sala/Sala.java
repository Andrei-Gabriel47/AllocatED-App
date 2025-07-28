package com.repartizareexamen.repartizare.Sala;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Sala {
    @Id
   private String id;
   private Integer numarRanduri;
   private Integer locuriPerRand;

    public Sala() {
    }

    public Sala(String id, Integer numarRanduri, Integer locuriPerRand) {
        this.id = id;
        this.numarRanduri = numarRanduri;
        this.locuriPerRand = locuriPerRand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumarRanduri() {
        return numarRanduri;
    }

    public void setNumarRanduri(Integer numarRanduri) {
        this.numarRanduri = numarRanduri;
    }

    public Integer getLocuriPerRand() {
        return locuriPerRand;
    }

    public void setLocuriPerRand(Integer locuriPerRand) {
        this.locuriPerRand = locuriPerRand;
    }

    public int getCapacitateTotala() {
        return numarRanduri * locuriPerRand;
    }


    @Override
    public String toString() {
        return id;
    }
}
