package com.example.authentication.Repository;
import com.example.authentication.Entity.Role;
import com.example.authentication.Entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum role);
}
