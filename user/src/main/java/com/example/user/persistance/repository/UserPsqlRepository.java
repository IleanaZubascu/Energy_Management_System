package com.example.user.persistance.repository;

import com.example.user.persistance.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPsqlRepository extends JpaRepository<UserEntity, Long> {

   Optional<UserEntity> findByNameAndPassword(String name, String password);
   Optional<UserEntity> findByName(String name);

   @Modifying
   @Query("update UserEntity u set u.name = :name where u.id = :id")
   void updateUserName(@Param(value = "id") Long id, @Param(value = "name") String name);

}

