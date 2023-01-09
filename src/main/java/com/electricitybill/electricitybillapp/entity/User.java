package com.electricitybill.electricitybillapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;
    private String name;
    private String username;
    private String email;
    @JsonIgnore
    @JsonIgnoreProperties
    private String password;
    private String authenticationToken;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
