package com.environment.environment.repository;

import com.environment.environment.domain.Battleground;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Battleground entity.
 */
public interface BattlegroundRepository extends JpaRepository<Battleground,Long> {

}
