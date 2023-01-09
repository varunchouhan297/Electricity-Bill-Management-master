package com.electricitybill.electricitybillapp.repository;

import com.electricitybill.electricitybillapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CrudRepository<User, Long> {
    User save(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameAndPassword(String username, String password);
}
