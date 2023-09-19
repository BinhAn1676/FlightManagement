package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUserName (String userName);
    Boolean existsByUserName(String userName);
}
