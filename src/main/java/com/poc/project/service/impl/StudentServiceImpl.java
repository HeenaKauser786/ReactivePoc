package com.poc.project.service.impl;

import com.poc.project.entity.StudentReactive;
import com.poc.project.exception.StudentAlreadyExistsException;
import com.poc.project.exception.StudentNotFoundException;
import com.poc.project.repository.StudentRepo;
import com.poc.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepo studentRepo;


    @Override
    public Flux<StudentReactive> getAllStudents() {
        return studentRepo.findAll().switchIfEmpty(Mono.error(new StudentNotFoundException("No data found.")));
    }

    @Override
    public Mono<StudentReactive> getStudentByEmail(String studentEmail) {
        return studentRepo.findByStudentEmail(studentEmail).switchIfEmpty(Mono.error(new StudentNotFoundException("No student detail found with given email.")));
    }

    @Override
    public Mono<Object> addStudent(StudentReactive student){
        student.setStudentEmail(student.getStudentEmail().toLowerCase());
        return studentRepo.findByStudentEmail(student.getStudentEmail())
                .flatMap(s->Mono.error(new StudentAlreadyExistsException("Email is already present")))
                .switchIfEmpty(studentRepo.save(student));

    }

    @Override
    public Mono<StudentReactive> updateStudent(StudentReactive student, String studentEmail) {

        return studentRepo.findByStudentEmail(studentEmail)
                .flatMap(s->{
                    s.setStudentBatch(student.getStudentBatch());
                    s.setStudentAddress(student.getStudentAddress());
                    s.setStudentName(student.getStudentName());
                    s.setStudentRollNum(student.getStudentRollNum());
                    return studentRepo.save(s);
                }).switchIfEmpty(Mono.error(new StudentNotFoundException("Student details are not present to update.")));
    }

    @Override
    public Mono<StudentReactive> deleteStudentByEmail(String studentEmail) {
        return studentRepo.findByStudentEmail(studentEmail).switchIfEmpty(Mono.error(new StudentNotFoundException("Student is not available"))).flatMap(s->studentRepo.delete(s).then(Mono.just(s)));
    }
}
