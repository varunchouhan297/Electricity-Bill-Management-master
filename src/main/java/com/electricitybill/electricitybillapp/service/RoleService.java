package com.electricitybill.electricitybillapp.service;

import com.electricitybill.electricitybillapp.entity.Role;
import com.electricitybill.electricitybillapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

}
