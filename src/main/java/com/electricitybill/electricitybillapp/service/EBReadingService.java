package com.electricitybill.electricitybillapp.service;

import com.electricitybill.electricitybillapp.entity.EBBill;
import com.electricitybill.electricitybillapp.entity.EBReading;
import com.electricitybill.electricitybillapp.entity.User;
import com.electricitybill.electricitybillapp.repository.EBReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EBReadingService {

    @Autowired
    EBReadingRepository ebReadingRepository;

    @Autowired
    UserService userService;

    @Autowired
    EBBillService ebBillService;

    public List<EBReading> getAllEBReading() {
        return ebReadingRepository.findAll();
    }

    public List<EBReading> getAllEBReadingForUser(User user) {
        return ebReadingRepository.findByUser(user);
    }

    public List<Map<String, Object>> buildEBReadingMapList(List<EBReading> ebReadings) {
        List<Map<String, Object>> ebReadingMapList = new ArrayList<>();
        ebReadings.stream().forEach(user -> {
            ebReadingMapList.add(buildEbReadingMap(user));
        });
        return ebReadingMapList;
    }

    public Map<String, Object> buildEbReadingMap(EBReading ebReading) {
        Map<String, Object> ebReadingMap = new HashMap<>();
        ebReadingMap.put("id", ebReading.getId());
        ebReadingMap.put("reading", ebReading.getReading());
        ebReadingMap.put("month", ebReading.getMonth());
        ebReadingMap.put("year", ebReading.getYear());
        ebReadingMap.put("ebBill", ebBillService.buildEbBillMap(ebReading.getEbBill()));
        ebReadingMap.put("user", userService.buildUserMap(ebReading.getUser()));
        return ebReadingMap;
    }

    public EBReading buildAndCreateEbReading(Map<String, Object> reqMap) {
        Integer userIdInt = (Integer) reqMap.get("userId");
        Long userId = Long.valueOf(userIdInt.longValue());
        User user = userService.getUserById(userId);
        EBReading ebReading = null;
        if(user != null) {
            System.out.println("req map -- " + reqMap);
            Integer reading = (Integer) reqMap.get("reading");
            Integer penaltyIntAmount = (Integer) reqMap.get("penaltyAmount");
            Double penaltyAmount = penaltyIntAmount.doubleValue();
            Integer year = (Integer) reqMap.get("year");
            String month = (String) reqMap.get("month");
            EBBill ebBill = ebBillService.createEbBill(reading, penaltyAmount, user);
            if(ebBill != null) {
                ebReading = new EBReading(reading, month, year, ebBill, user);
                ebReading = saveEbReading(ebReading);
                ebBill.setEbReading(ebReading);
                ebBillService.saveEbBill(ebBill);
            }
        }
        return ebReading;
    }

    public EBReading saveEbReading(EBReading ebReading) {
        return ebReadingRepository.save(ebReading);
    }

    public List<EBReading> getAllEbReadingForUser(Long userId) {
        User user = userService.getUserById(userId);
        List<EBReading> ebReadings = new ArrayList<>();
        if(user != null) {
            ebReadings = getAllEBReadingForUser(user);
        }
        return ebReadings;
    }

    public List<EBReading> getAllUnPaidEbReadingForUser(Long userId) {
        User user = userService.getUserById(userId);
        List<EBReading> ebReadings = new ArrayList<>();
        if(user != null) {
            ebReadings = ebBillService.getAllUnPaidEbReadingForUser(user);
        }
        return ebReadings;
    }

    public Boolean payBill(Map<String, Object> reqMap) {
        Integer userIdInt = (Integer) reqMap.get("userId");
        Long userId = Long.valueOf(userIdInt.longValue());
        User user = userService.getUserById(userId);
        if(user == null) {
            return false;
        }
        Integer ebReadingIdInt = (Integer) reqMap.get("ebReadingId");
        Long ebReadingId = Long.valueOf(ebReadingIdInt.longValue());
        EBReading ebReading = getById(ebReadingId);
        if(ebReading == null) {
            return false;
        }
        if(ebReading.getUser().getId() == user.getId()) {
            EBBill ebBill = ebBillService.markBillAsPaid(ebReading.getEbBill());
            return ebBill.getIsPaid();
        }
        return false;
    }

    public EBReading getById(Long id) {
        return ebReadingRepository.findById(id).orElse(null);
    }

}
