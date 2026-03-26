package com.minicloud.repository;

import com.minicloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByAccountIdAndIamUsername(String accountId, String iamUsername);
    Optional<User> findByAccountAliasAndIamUsername(String accountAlias, String iamUsername);
}
