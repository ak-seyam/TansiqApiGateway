package com.example.routingandfilteringgateway.repositories;

import com.example.routingandfilteringgateway.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminsRepo extends JpaRepository<Admin, UUID> {
    Admin getByEmailAndPassword(String email, String password);
    Admin getByEmail(String email);
}
