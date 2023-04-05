package com.example.authusersmicroservice.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.authusersmicroservice.models.Token;
import com.example.authusersmicroservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.userId = u.userId\s
      where u.userId = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.user = :owner")
    void deleteTokensByUser(@Param("owner") User owner);
}

