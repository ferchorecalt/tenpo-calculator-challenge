package com.tenpo.challenge.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tenpo.challenge.auth.model.Role;
import com.tenpo.challenge.auth.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByName(RoleName name);
}
