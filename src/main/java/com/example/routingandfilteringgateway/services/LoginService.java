package com.example.routingandfilteringgateway.services;

import com.example.routingandfilteringgateway.models.Admin;
import com.example.routingandfilteringgateway.models.Student;
import com.example.routingandfilteringgateway.repositories.AdminsRepo;
import com.example.routingandfilteringgateway.repositories.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoginService {
    private final AdminsRepo adminsRepo;
    private final StudentRepo studentRepo;

    @Autowired
    public LoginService(AdminsRepo adminsRepo, StudentRepo studentRepo) {
        this.adminsRepo = adminsRepo;
        this.studentRepo = studentRepo;
    }

    public Map<LoginStatusKeys, Object> validCredentials(String email, String password) {
        Student student = studentRepo.getByEmailAndPassword(email, password);
        if (student != null) {
            return Map.of(LoginStatusKeys.VALID, true, LoginStatusKeys.ROLES, List.of("ROLE_STUDENT"));
        }
        Admin admin = adminsRepo.getByEmailAndPassword(email, password);
        if (admin != null) {
            return Map.of(LoginStatusKeys.VALID, true, LoginStatusKeys.ROLES, List.of("ROLE_ADMIN"));
        }
        return Map.of(LoginStatusKeys.VALID, false);
    }
}
