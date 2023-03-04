package com.poc.project.controller;

import com.poc.project.entity.StudentReactive;
import com.poc.project.exception.StudentAlreadyExistsException;
import com.poc.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.*;

@RestController
@RequestMapping("/api/v1")
@Validated
public class Controller {

    @Autowired
    StudentService studentService;

    @GetMapping
    Flux<StudentReactive> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("student/{studentEmail}")
    Mono<ResponseEntity<StudentReactive>> getStudentByEmail(@Email @PathVariable String studentEmail){
        return studentService.getStudentByEmail(studentEmail.toLowerCase()).map(s -> ResponseEntity.ok(s));
    }

    @PostMapping("/student")
    Mono<ResponseEntity<Object>> addStudentByEmail(@Valid @RequestBody StudentReactive student){
            return studentService.addStudent(student)
                    .map((s)->ResponseEntity.accepted().body(s));

    }

    @PutMapping("/student/{studentEmail}")
    Mono<ResponseEntity<StudentReactive>> updateStudent(@Valid @RequestBody StudentReactive student,@Email @PathVariable String studentEmail){

        return studentService.updateStudent(student,studentEmail)
                    .map(s -> ResponseEntity.accepted().body(s));

    }

    

    @DeleteMapping("/student/{studentEmail}")
    Mono<ResponseEntity<Void>> deleteEmployeeByEmail(@Email @PathVariable String studentEmail){
        return studentService.deleteStudentByEmail(studentEmail)
                .map(r->ResponseEntity.ok().<Void>build());
    }




}
