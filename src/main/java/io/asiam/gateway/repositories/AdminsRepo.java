package io.asiam.gateway.repositories;

import io.asiam.gateway.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminsRepo extends JpaRepository<Admin, UUID> {
    Admin getByEmailAndPassword(String email, String password);
    Admin getByEmail(String email);
}
