package com.justdeepfried.GyanJyotiLMS.entities.user.repository;

import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query("""
    SELECT u FROM UserModel u
    JOIN FETCH u.school sc
    LEFT JOIN FETCH u.teacher ta
    LEFT JOIN FETCH u.parent pa
    WHERE LOWER(u.username) = LOWER(:username)
    """)
    Optional<UserModel> findByUsername(String username);
    boolean existsByUsername(String username);
}
