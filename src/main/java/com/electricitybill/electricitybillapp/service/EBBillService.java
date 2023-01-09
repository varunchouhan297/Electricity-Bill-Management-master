package com.electricitybill.electricitybillapp.service;

import com.electricitybill.electricitybillapp.entity.EBBill;
import com.electricitybill.electricitybillapp.entity.EBReading;
import com.electricitybill.electricitybillapp.entity.User;
import com.electricitybill.electricitybillapp.repository.EBBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EBBillService {

    @Autowired
    EBBillRepository ebBillRepository;

    public Map<String, Object> buildEbBillMap(EBBill ebBill) {
        Map<String, Object> ebBillMap = new HashMap<>();
        ebBillMap.put("readingAmount", ebBill.getReadingAmount());
        ebBillMap.put("penalty", ebBill.getPenalty());
        ebBillMap.put("totalAmount", ebBill.getTotalAmount());
        ebBillMap.put("isPaid", ebBill.getIsPaid());
        return ebBillMap;
    }

    public EBBill createEbBill(Integer reading, Double penalty, User user) {
        EBBill ebBill = null;
        if(reading != null) {
            Double readingAmount = computeBill(reading);
            penalty = penalty != null ? penalty : 0;
            Double totalAmount = readingAmount + penalty;
            ebBill = new EBBill(readingAmount, penalty, totalAmount, Boolean.FALSE, user);
            ebBill = ebBillRepository.save(ebBill);
        }
        return ebBill;
    }

    public EBBill saveEbBill(EBBill ebBill) {
        ebBill = ebBillRepository.save(ebBill);
        return ebBill;
    }

    public Double computeBill(Integer reading) {
        Double amt = 0d;
        if (reading <= 100) {
            amt = 1.25 * reading;
        } else if (reading <= 200) {
            amt = 1.25 * 100 + (reading - 100) * 1.5;
        } else {
            amt = 1.25 * 100 + 1.5 * 100 + (reading - 200) * 1.8;
        }
        return amt;
    }

    public List<EBReading> getAllUnPaidEbReadingForUser(User user) {
        List<EBReading> ebReadings = new ArrayList<>();
        if(user != null) {
            List<EBBill> ebBills = ebBillRepository.findByIsPaidAndUser(false, user);
            ebBills.stream().forEach(ebBill -> {
                ebReadings.add(ebBill.getEbReading());
            });
        }
        return ebReadings;
    }

    public EBBill markBillAsPaid(EBBill ebBill) {
        ebBill.setIsPaid(Boolean.TRUE);
        ebBill = ebBillRepository.save(ebBill);
        return ebBill;
    }

}
