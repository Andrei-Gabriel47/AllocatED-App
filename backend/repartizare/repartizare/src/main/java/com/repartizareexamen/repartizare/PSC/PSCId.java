package com.repartizareexamen.repartizare.PSC;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PSCId implements Serializable {

    private Integer cursId;
    private Integer profesorId;
    private String studentMatricol;

    public PSCId() {
    }

    public PSCId(Integer cursId, Integer profesorId, String studentMatricol) {
        this.cursId = cursId;
        this.profesorId = profesorId;
        this.studentMatricol = studentMatricol;
    }

    public Integer getCursId() {
        return cursId;
    }

    public void setCursId(Integer cursId) {
        this.cursId = cursId;
    }

    public Integer getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Integer profesorId) {
        this.profesorId = profesorId;
    }

    public String getStudentMatricol() {
        return studentMatricol;
    }

    public void setStudentMatricol(String studentMatricol) {
        this.studentMatricol = studentMatricol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PSCId pscId = (PSCId) o;
        return Objects.equals(cursId, pscId.cursId) && Objects.equals(profesorId, pscId.profesorId) && Objects.equals(studentMatricol, pscId.studentMatricol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cursId, profesorId, studentMatricol);
    }
}
