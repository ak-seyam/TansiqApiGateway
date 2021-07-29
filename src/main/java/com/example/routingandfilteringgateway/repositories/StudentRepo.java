package com.example.routingandfilteringgateway.repositories;

import com.example.routingandfilteringgateway.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepo extends JpaRepository<Student, UUID> {
    Student getByEmailAndPassword(String email, String password);
    Student getByEmail(String email);
}
