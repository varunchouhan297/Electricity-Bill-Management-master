package com.electricitybill.electricitybillapp.controller;

import com.electricitybill.electricitybillapp.entity.EBReading;
import com.electricitybill.electricitybillapp.service.EBReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ebReading")
public class EBReadingController {

    @Autowired
    EBReadingService ebReadingService;


    @CrossOrigin(origins = "*")
    @GetMapping("/getAllReadings")
    public ResponseEntity getAllEBReading() {
        List<EBReading> ebReadings = ebReadingService.getAllEBReading();
        List<Map<String, Object>> ebReadingMapList = ebReadingService.buildEBReadingMapList(ebReadings);
        return ResponseEntity.ok(ebReadingMapList);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getReadingById/{id}")
    public ResponseEntity getEBReadingById(@PathVariable Long id) {
        EBReading ebReading = ebReadingService.getById(id);
        Map<String, Object> ebReadingMapList = new HashMap<>();
        if(ebReading != null) {
            ebReadingMapList = ebReadingService.buildEbReadingMap(ebReading);
        }
        return ResponseEntity.ok(ebReadingMapList);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getAllReadingsForUser/{userId}")
    public ResponseEntity getAllEBReadingForUser(@PathVariable Long userId) {
        List<EBReading> ebReadings = ebReadingService.getAllEbReadingForUser(userId);
        List<Map<String, Object>> ebReadingMapList = ebReadingService.buildEBReadingMapList(ebReadings);
        return ResponseEntity.ok(ebReadingMapList);
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/getNotPaidReadingsForUser/{userId}")
    public ResponseEntity getNotPaidEBReadingForUser(@PathVariable Long userId) {
        List<EBReading> ebReadings = ebReadingService.getAllUnPaidEbReadingForUser(userId);
        List<Map<String, Object>> ebReadingMapList = ebReadingService.buildEBReadingMapList(ebReadings);
        return ResponseEntity.ok(ebReadingMapList);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/generateBill", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generateEbBill(@RequestBody Map<String, Object> requestMap) {
        EBReading ebReading = ebReadingService.buildAndCreateEbReading(requestMap);
        Map<String, Object> respMap = new HashMap<>();
        respMap.put("isSuccess", true);
        if(ebReading.getId() == null) {
            respMap.put("error", "Error while creating EB reading");
            respMap.put("isSuccess", false);
        }
        return ResponseEntity.ok(respMap);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/payBill", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity payBill(@RequestBody Map<String, Object> requestMap) {
        Boolean isPaid = ebReadingService.payBill(requestMap);
        Map<String, Object> respMap = new HashMap<>();
        respMap.put("isSuccess", isPaid);
        if(!isPaid) {
            respMap.put("error", "Payment is not done. Please try again.");
        }
        return ResponseEntity.ok(respMap);
    }


}
