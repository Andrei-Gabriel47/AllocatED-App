package com.repartizareexamen.repartizare.PSC;

import com.repartizareexamen.repartizare.Curs.Curs;
import com.repartizareexamen.repartizare.Profesor.Profesor;
import com.repartizareexamen.repartizare.Student.Student;
import jakarta.persistence.*;

@Entity
@Table
public class PSC {

    @Id
    private PSCId id;

    @ManyToOne
    @MapsId("cursId")
    @JoinColumn(name = "curs_Id", nullable = false)
    private Curs curs;

    @ManyToOne
    @MapsId("profesorId")
    @JoinColumn(name = "profesor_Id", nullable = false)
    private Profesor profesor;

    @ManyToOne
    @MapsId("studentMatricol")
    @JoinColumn(name = "student_Matricol", nullable = false)
    private Student student;

    private String situatieStudent;

    public PSC() {
    }

    public PSC(PSCId id, Curs curs, Profesor profesor, Student student, String situatieStudent) {
        this.id = id;
        this.curs = curs;
        this.profesor = profesor;
        this.student = student;
        this.situatieStudent = situatieStudent;
    }

    public PSCId getId() {
        return id;
    }

    public void setId(PSCId id) {
        this.id = id;
    }

    public Curs getCurs() {
        return curs;
    }

    public void setCurs(Curs curs) {
        this.curs = curs;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSituatieStudent() {
        return situatieStudent;
    }

    public void setSituatieStudent(String situatieStudent) {
        this.situatieStudent = situatieStudent;
    }
}
