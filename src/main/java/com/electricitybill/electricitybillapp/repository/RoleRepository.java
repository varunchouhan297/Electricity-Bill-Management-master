package com.electricitybill.electricitybillapp.repository;

import com.electricitybill.electricitybillapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, CrudRepository<Role, Long> {
    Role findByName(String name);
}
