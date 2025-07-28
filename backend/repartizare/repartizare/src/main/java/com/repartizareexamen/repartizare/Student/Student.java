package com.repartizareexamen.repartizare.Student;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Student {

    @Id
    private String matricol;
    private String nume;
    private String prenume;
    private String email;
    private String grupa;
    private Integer varsta;

    public Student() {
    }

    public Student(String matricol, String nume, String prenume, String email, String grupa, Integer varsta) {
        this.matricol = matricol;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.grupa = grupa;
        this.varsta = varsta;
    }

    public String getMatricol() {
        return matricol;
    }

    public void setMatricol(String matricol) {
        this.matricol = matricol;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public Integer getVarsta() {
        return varsta;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }

    @Override
    public String toString() {
        return matricol;
    }
}
