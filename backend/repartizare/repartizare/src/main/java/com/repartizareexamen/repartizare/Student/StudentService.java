package com.repartizareexamen.repartizare.Student;

import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudenti(){
        return  studentRepository.findAll();
    }

    public void addNewStudent(Student student) {

        if(studentRepository.existsById(student.getMatricol())){
            logger.warn("Studentul există deja cu matricolul: {}", student.getMatricol());
            throw new RuntimeException("Studentul există deja. Nu se poate adăuga.");
        }
        else
        {
            logger.info("Adăugăm studentul cu matricolul: {}", student.getMatricol());
            studentRepository.save(student);
        }


    }

    public void addMultipleStudents(List<Student> studenti) {
        for (Student student : studenti) {
            if (studentRepository.existsById(student.getMatricol())) {
                logger.warn("Studentul {} există deja. Se sare peste adăugare.", student.getMatricol());
                continue; // sau poți arunca excepție dacă preferi
            }
            studentRepository.save(student);
            logger.info("Studentul {} a fost adăugat.", student.getMatricol());
        }
    }


    public void updateStudent(String matricol, Student student) {
        Optional<Student> studentVechi = studentRepository.findById(matricol);
        if(studentVechi.isPresent()){
            Student studentNou = studentVechi.get();
            studentNou.setNume(student.getNume());
            studentNou.setPrenume(student.getPrenume());
            studentNou.setEmail(student.getEmail());
            studentNou.setGrupa(student.getGrupa());
            studentNou.setVarsta(student.getVarsta());
            studentRepository.save(studentNou);
        }
        else
            throw new RuntimeException("Studentul cu matricolul " + matricol + " nu există.");

    }

    public void deleteStudent(String matricol) {
    studentRepository.deleteById(matricol);
    }
}
