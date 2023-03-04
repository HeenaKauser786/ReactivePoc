package com.poc.project.service;

import com.poc.project.entity.StudentReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

    Flux<StudentReactive> getAllStudents();
    Mono<StudentReactive> getStudentByEmail(String studentEmail);
    Mono<Object> addStudent(StudentReactive student);
    Mono<StudentReactive> updateStudent(StudentReactive student, String studentEmail);
    Mono<StudentReactive> deleteStudentByEmail(String studentEmail);

}
