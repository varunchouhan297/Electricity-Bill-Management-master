package com.electricitybill.electricitybillapp.repository;

import com.electricitybill.electricitybillapp.entity.EBReading;
import com.electricitybill.electricitybillapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EBReadingRepository extends JpaRepository<EBReading, Long>, CrudRepository<EBReading, Long> {
    EBReading save(EBReading ebReading);
    List<EBReading> findByUser(User user);
}
