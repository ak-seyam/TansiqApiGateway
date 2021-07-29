package com.example.routingandfilteringgateway.integration;

import com.example.routingandfilteringgateway.models.Student;
import com.example.routingandfilteringgateway.repositories.StudentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StudentAuthentication {
    @Autowired
    StudentRepo studentRepo;

    @AfterEach
    public void afterEach(){
        studentRepo.deleteAll();
    }

    @Test
    public void itShouldGetStudentByEmailAndPassword(){
        String email = "ahmed1";
        String password= "ahmed";
        Student newStudent = studentRepo.save(new Student("name 1",420,email, password ));
        Student s = studentRepo.getByEmailAndPassword(email, password);
        assertThat(s.getId()).isEqualTo(newStudent.getId());
    }

    @Test
    public void itShouldReturnNullForWrongUsername(){
        String email = "ahmed2";
        String password= "ahmed";
        Student newStudent = studentRepo.save(new Student("name2",420,email, password ));
        Student s = studentRepo.getByEmailAndPassword(email+"asd", password);
        assertThat(s).isNull();
    }

    @Test
    public void itShouldReturnNullForWrongPassword(){
        String email = "ahmed3";
        String password= "ahmed";
        Student newStudent = studentRepo.save(new Student("name3",420,email, password ));
        Student s = studentRepo.getByEmailAndPassword(email, password+"asasd");
        assertThat(s).isNull();
    }
}
