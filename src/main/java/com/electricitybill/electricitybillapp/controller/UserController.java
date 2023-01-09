package com.electricitybill.electricitybillapp.controller;

import com.electricitybill.electricitybillapp.entity.User;
import com.electricitybill.electricitybillapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/registerUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerUser(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> respMap = userService.buildAndCreateUser(requestMap);
        return ResponseEntity.ok(respMap);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/adminLogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity adminLogin(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> respMap = userService.validateUserAuthentication(requestMap, true);
        return ResponseEntity.ok(respMap);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/customerLogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity customerLogin(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> respMap = userService.validateUserAuthentication(requestMap, false);
        return ResponseEntity.ok(respMap);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRegisteredUsers")
    public ResponseEntity getRegisteredUsers() {
        List<User> registeredUser = userService.getRegisteredUsers();
        List<Map<String, Object>> userMapList = userService.buildMapFromUserList(registeredUser);
        return ResponseEntity.ok(userMapList);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getUser/{userId}")
    public ResponseEntity getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        Map<String, Object> respMap = userService.buildUserMap(user);
        if(user == null) {
            respMap.put("error", "User not found");
        }
        return ResponseEntity.ok(respMap);
    }




}
