package com.poc.project.controller;

import com.poc.project.entity.StudentReactive;
import com.poc.project.exception.StudentAlreadyExistsException;
import com.poc.project.exception.StudentNotFoundException;
import com.poc.project.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebFluxTest
public class ControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private Controller controller;

    private StudentReactive student;

    @BeforeEach
    void setUp(){
        student = new StudentReactive(1, "abc@gmail.com", "Dharu", "ABC", "1212", "Ajmer");
    }

    @AfterEach
    void tearDown(){
        student = null;
    }

    @Test
    void getAllStudentsWithStatusIsOk(){
        when(studentService.getAllStudents()).thenReturn(Flux.just(student,student));
        Flux<StudentReactive> responseBody = webTestClient.get().uri("/api/v1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StudentReactive.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(student)
                .expectNext(student)
                .verifyComplete();
    }

    @Test
    void getAllStudentsIfNotExistsThenThrowException(){
        when(studentService.getAllStudents()).thenThrow(new StudentNotFoundException("No data found."));
        webTestClient.get().uri("/api/v1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void givenStudentEmailToGetStudentDetails(){
        when(studentService.getStudentByEmail("abc@gmail.com")).thenReturn(Mono.just(student));
        Flux<StudentReactive> responseBody = webTestClient.get().uri("/api/v1/student/{studentEmail}", "abc@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StudentReactive.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(student)
                .verifyComplete();

    }

    @Test
    void givenStudentDetailsToAddStudent() throws StudentAlreadyExistsException {
        when(studentService.addStudent(student)).thenReturn(Mono.just(student));
        webTestClient.post().uri("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student),StudentReactive.class)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.studentName").isEqualTo("Dharu");

    }

    @Test
    void givenStudentDetailsAndStudentEmailToUpdate(){
        when(studentService.updateStudent(student,"abc@gmail.com")).thenReturn(Mono.just(student));
        webTestClient.put().uri("/api/v1/student/{studentEmail}","abc@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student),StudentReactive.class)
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    void givenStudentEmailAndStudentDetailsToUpdateAreNotPresentThenReturnStatusBadRequest(){
        when(studentService.updateStudent(student,"abc@gmail.com")).thenThrow(new StudentNotFoundException("Student is not present."));
        webTestClient.put().uri("/api/v1/student/{studentEmail}","abc@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student),StudentReactive.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void givenStudentEmailToDeleteExistingStudent(){
        when(studentService.deleteStudentByEmail("abc@gmail.com")).thenReturn(Mono.just(student));
        webTestClient.delete().uri("/api/v1/student/{studentEmail}","abc@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void givenNotExistingStudentEmailToDeleteThenReturnNotFoundStatus(){
        when(studentService.deleteStudentByEmail("abc@gmail.com")).thenThrow(new StudentNotFoundException("Not found"));
        webTestClient.delete().uri("/api/v1/student/{studentEmail}","abc@gmail.com")
                .exchange()
                .expectStatus().isNotFound();
    }
}
