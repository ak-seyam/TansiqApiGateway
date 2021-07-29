package com.example.routingandfilteringgateway.integration;

import com.example.routingandfilteringgateway.models.Admin;
import com.example.routingandfilteringgateway.repositories.AdminsRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AdminAuthentication {
    @Autowired
    AdminsRepo adminsRepo;

    @AfterEach
    public void afterEach(){
        adminsRepo.deleteAll();
    }

    @Test
    public void itShouldGetStudentByEmailAndPassword(){
        String email = "ahmed1";
        String password= "ahmed";
        Admin newAdmin = adminsRepo.save(new Admin(email,password));
        Admin a = adminsRepo.getByEmailAndPassword(email, password);
        assertThat(a.getId()).isEqualTo(newAdmin.getId());
    }

    @Test
    public void itShouldReturnNullInvalidEmail(){
        String email = "ahmed2";
        String password= "ahmed";
        Admin newAdmin = adminsRepo.save(new Admin(email,password));
        Admin a = adminsRepo.getByEmailAndPassword(email+"asd", password);
        assertThat(a).isNull();
    }

    @Test
    public void itShouldReturnNullInvalidPassword(){
        String email = "ahmed3";
        String password= "ahmed";
        Admin newAdmin = adminsRepo.save(new Admin(email,password));
        Admin a = adminsRepo.getByEmailAndPassword(email, password+"asd");
        assertThat(a).isNull();
    }
}
