package com.example.authusersmicroservice.repositories;


import com.example.authusersmicroservice.models.Role;
import com.example.authusersmicroservice.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Query("select r from Role r where r.roleName = ?1")
    Role findByRoleName(RoleName roleName);


}
