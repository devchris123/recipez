package com.cwunder.recipe.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface UserRepository extends JpaRepository<User, Long>, AppEntityRepository<User> {
    static String selectByUserAndUsername = "select user from User user where user.username = ?#{ principal?.username } and user.username = ?1";
    static String selectByUserAndPublicId = "select user from User user where user.username = ?#{ principal?.username } and user.publicId = ?1";
    static String deleteByUserAndPublicId = "delete from User user where user.username = ?#{ principal?.username }  and user.publicId = ?1";

    @Query(selectByUserAndUsername)
    Optional<User> findByUsernameAuth(String userName);

    // Used by user details service to find a user before authentication. Do not use
    // in `authenticated only endpoints`.
    Optional<User> findByUsername(String userName);

    // Used by user UserController to check for existence before creating a user.
    boolean existsByUsername(String userName);

    @Query(selectByUserAndPublicId)
    Optional<User> findByPublicId(String publicId);

    @Modifying
    @Query(deleteByUserAndPublicId)
    void deleteByPublicId(String publicId);
}
