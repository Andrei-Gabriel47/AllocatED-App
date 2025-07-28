package com.repartizareexamen.repartizare.Student;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudenti(){
    return  studentService.getAllStudenti();
    }

    @PostMapping
    public ResponseEntity<?> addNewStudent(@RequestBody Student student) {
        try {
            studentService.addNewStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> addMultipleStudents(@RequestBody List<Student> studenti) {
        try {
            studentService.addMultipleStudents(studenti);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @DeleteMapping(path = "{matricol}")
    public void deleteStudent(@PathVariable("matricol") String matricol) {
        studentService.deleteStudent(matricol);
    }

    @PutMapping(path = "{matricol}")
    public void updateStudent(@PathVariable("matricol") String matricol, @RequestBody Student student) {
        studentService.updateStudent(matricol, student);
    }

}