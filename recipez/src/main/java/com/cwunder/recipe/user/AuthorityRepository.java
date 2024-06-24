package com.cwunder.recipe.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, AppEntityRepository<Authority> {

}
