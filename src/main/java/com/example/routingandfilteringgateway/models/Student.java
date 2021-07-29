package com.example.routingandfilteringgateway.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    private int mark;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    public Student(String name, int mark, String email, String password) {
        this.name = name;
        this.mark = mark;
        this.email = email;
        this.password = password;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mark=" + mark +
                '}';
    }
}
