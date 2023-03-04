package com.poc.project.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentReactive {

    @Id
    private int studentId;
    @NotNull
    @NotEmpty
    @Pattern(regexp =  "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",message = "{email.pattern.mismatch}")
    private String studentEmail;
    @NotNull
    @NotEmpty
    @Size(min = 3,max = 20)
    private String studentName;
    @NotNull
    @NotEmpty
    @Size(min = 1,max = 10)
    private String studentBatch;
    @NotNull
    @NotEmpty
    @Size(min = 4,max = 8)
    private String studentRollNum;
    @NotNull
    @NotEmpty
    @Size(min = 4,max = 20)
    private String studentAddress;

}
