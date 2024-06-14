package com.cwunder.recipe.unit;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface UnitRepository extends JpaRepository<Unit, Long>, AppEntityRepository<Unit> {

}
