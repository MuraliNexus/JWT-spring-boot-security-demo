package com.example.JWT.springBoot.easy.Repoistory;


import com.example.JWT.springBoot.easy.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepoistory extends JpaRepository<Users,Long> {
   Optional<Users> findByName(String name);
}
