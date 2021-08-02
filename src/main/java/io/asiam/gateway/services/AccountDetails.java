package io.asiam.gateway.services;

import io.asiam.gateway.models.Admin;
import io.asiam.gateway.models.Student;
import io.asiam.gateway.repositories.AdminsRepo;
import io.asiam.gateway.repositories.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public UUID getUUIDByEmailAndRole(String email, String role) {
        if ("ROLE_ADMIN".equals(role)) {
            Admin admin = adminsRepo.getByEmail(email);
            return admin.getId();
        }
        Student student = studentRepo.getByEmail(email);
        return student.getId();
    }

    public UUID getIdByUserDetails(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).get(0);
        return getUUIDByEmailAndRole(userDetails.getUsername(), role);
    }

    public UUID getIdByUser(User user) {
        String role = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).get(0);
        return getUUIDByEmailAndRole(user.getUsername(), role);
    }
}
