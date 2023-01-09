package com.electricitybill.electricitybillapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EBBill {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private Double readingAmount;
    private Double penalty;
    private Double totalAmount;
    private Boolean isPaid;

    @OneToOne
    @JoinColumn(name = "eb_reading_id")
    private EBReading ebReading;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public EBBill(Double readingAmount, Double penalty, Double totalAmount, Boolean isPaid, User user) {
        this.readingAmount = readingAmount;
        this.penalty = penalty;
        this.totalAmount = totalAmount;
        this.isPaid = isPaid;
        this.user = user;
    }

}
