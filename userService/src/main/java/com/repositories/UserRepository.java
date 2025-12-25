package com.repositories;

import com.entities.UserInfo;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    public Optional<UserInfo> findByEmail(String email);

    public Optional<UserInfo> findByUserId(@NonNull String userId);
}
