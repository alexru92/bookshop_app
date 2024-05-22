package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findUserEntityByEmail(String email);

}
