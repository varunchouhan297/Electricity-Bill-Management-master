package com.electricitybill.electricitybillapp.service;

import com.electricitybill.electricitybillapp.entity.Role;
import com.electricitybill.electricitybillapp.entity.User;
import com.electricitybill.electricitybillapp.repository.UserRepository;
import com.electricitybill.electricitybillapp.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Map<String, Object> buildAndCreateUser(Map<String, Object> reqMap) {
        User user;
        Map<String, Object> statusMap = new HashMap<>();
        String name = (String) reqMap.get("name");
        String username = (String) reqMap.get("username");
        String email = (String) reqMap.get("email");
        String password = (String) reqMap.get("password");
        String repassword = (String) reqMap.get("repassword");
        if(password.equals(repassword)) {
            if(!isEmailAlreadyPresent(email) && !isUsernameAlreadyPresent(username)) {
                user = createUser(name, username, email, password);
                if(user.getId() == null) {
                    statusMap.put("isSuccess", false);
                    statusMap.put("error", "Error occurred while create an user");
                    log.error("Error occurred while create an user");
                } else {
                    statusMap.put("isSuccess", true);
                    statusMap.put("message", "User registered successfully");
                }
            } else {
                statusMap.put("isSuccess", false);
                statusMap.put("error", "Username or Email already present");
                log.error("Username or Email already present");
            }
        }
        return statusMap;
    }

    private User createUser(String name, String username, String email, String password) {
        User user = new User(name, username, email, password);
        Role role = roleService.findRoleByName("customer");
        if(role != null) {
            user.setRole(role);
            user = userRepository.save(user);
        } else {
            log.error("Customer role is not present in DB");
        }
        return user;
    }

    private boolean isEmailAlreadyPresent(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    private boolean isUsernameAlreadyPresent(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    public Map<String, Object> validateUserAuthentication(Map<String, Object> reqMap, boolean onlyAdmin) {
        Map<String, Object> respMap = new HashMap<>();
        respMap.put("isSuccess", false);
        String username = (String) reqMap.get("username");
        String password = (String) reqMap.get("password");
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            User user = userRepository.findByUsernameAndPassword(username, password);
            Role userRole = Optional.ofNullable(user).map(User::getRole).orElse(null);
            if(userRole != null) {
                if(!onlyAdmin || userRole.getName().equalsIgnoreCase("admin")) {
                    String token = TokenUtils.generateNewToken();
                    user.setAuthenticationToken(token);
                    userRepository.save(user);
                    respMap.put("token", token);
                    respMap.put("username", user.getUsername());
                    respMap.put("userId", user.getId());
                    respMap.put("isSuccess", true);
                } else {
                    respMap.put("error", "Invalid login credential");
                }
            } else {
                respMap.put("error", "Invalid login credential");
            }
        } else {
            respMap.put("error", "Username or password is mandatory");
        }
        return respMap;
    }

    public List<User> getRegisteredUsers() {
        List<User> registeredUserList = userRepository.findAll();
        registeredUserList.removeIf(user -> user.getRole().getName().equalsIgnoreCase("admin"));
        return registeredUserList;
    }

    public List<Map<String, Object>> buildMapFromUserList(List<User> userList) {
        List<Map<String, Object>> userMapList = new ArrayList<>();
        userList.stream().forEach(user -> {
            userMapList.add(buildUserMap(user));
        });
        return userMapList;
    }


    public Map<String, Object> buildUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        if(user != null) {
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
        }
        return userMap;
    }

}
