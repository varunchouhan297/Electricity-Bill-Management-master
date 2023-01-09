package com.electricitybill.electricitybillapp.repository;

import com.electricitybill.electricitybillapp.entity.EBBill;
import com.electricitybill.electricitybillapp.entity.EBReading;
import com.electricitybill.electricitybillapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EBBillRepository extends JpaRepository<EBBill, Long>, CrudRepository<EBBill, Long> {
    EBBill save(EBBill ebBill);
    List<EBBill> findByIsPaidAndUser(Boolean isPaid, User user);
}
