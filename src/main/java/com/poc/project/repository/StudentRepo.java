package com.poc.project.repository;

import com.poc.project.entity.StudentReactive;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StudentRepo extends ReactiveCrudRepository<StudentReactive,String> {

    Mono<StudentReactive> findByStudentEmail(String studentEmail);
    boolean existsByStudentEmail(String studentEmail);
}
