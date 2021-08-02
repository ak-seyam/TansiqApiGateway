package io.asiam.gateway.repositories;

import io.asiam.gateway.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepo extends JpaRepository<Student, UUID> {
    Student getByEmailAndPassword(String email, String password);
    Student getByEmail(String email);
}
