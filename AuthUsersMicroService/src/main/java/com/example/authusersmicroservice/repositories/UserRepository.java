package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Role;
import com.example.authusersmicroservice.models.RoleName;
import com.example.authusersmicroservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select count(*) from User")
    Integer countUsers();

    User findUserByUserId(Integer id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Query("select u from User u where u.email = ?1 or u.username = ?1 or u.phone = ?1")
    Optional<User> findUserByEmailOrUsernameOrPhone(String credential);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
    @Modifying
    @Query("delete from User u where u.username = :username")
    void deleteUserByUsername(@Param("username") String username);
    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    // List<User> findUsersByRoles;

    /*@Query(" select s.user_id, s.username, s.email, s.locked, s.enabled from users s wher")
    List<UserInformation> findProvidersByCategory(@Param("id") Long categoryId);*/
}
