package com.electricitybill.electricitybillapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    public Role(String name) {
        this.name = name;
    }

}
