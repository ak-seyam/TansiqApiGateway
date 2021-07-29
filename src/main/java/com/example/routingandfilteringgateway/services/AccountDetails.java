package com.example.routingandfilteringgateway.services;

import com.example.routingandfilteringgateway.models.Admin;
import com.example.routingandfilteringgateway.models.Student;
import com.example.routingandfilteringgateway.repositories.AdminsRepo;
import com.example.routingandfilteringgateway.repositories.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDetails implements UserDetailsService {
    private final StudentRepo studentRepo;
    private final AdminsRepo adminsRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student s = studentRepo.getByEmail(email);
        if (s != null) {
            // return the user details with this user
            return new User(s.getEmail(),s.getPassword(), List.of(
               new SimpleGrantedAuthority("ROLE_STUDENT")
            ));
        } else {
            Admin a = adminsRepo.getByEmail(email);
            if (a != null) {
                return new User(a.getEmail(),a.getPassword(), List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                ));
            }
        }
        throw new UsernameNotFoundException("User not found");
    }
}
