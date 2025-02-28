package com.example.user.persistance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;

@Entity
@Data
@Table(name = "user", schema = "public")
public class UserEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "password")
    private String password;

    private String authority;
}
