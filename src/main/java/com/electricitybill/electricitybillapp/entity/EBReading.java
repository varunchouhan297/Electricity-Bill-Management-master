package com.electricitybill.electricitybillapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class EBReading {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private Integer reading;
    private String month;
    private Integer year;

    @OneToOne
    @JoinColumn(name = "eb_bill_id")
    private EBBill ebBill;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public EBReading(Integer reading, String month, Integer year, EBBill ebBill, User user) {
        this.reading = reading;
        this.month = month;
        this.year = year;
        this.ebBill = ebBill;
        this.user = user;
    }
}
