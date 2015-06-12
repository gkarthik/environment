package com.environment.environment.repository;

import com.environment.environment.domain.Clan;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Clan entity.
 */
public interface ClanRepository extends JpaRepository<Clan,Long> {

    @Query("select clan from Clan clan where clan.chieftain.login = ?#{principal.username}")
    List<Clan> findAllForCurrentUser();

    @Query("select clan from Clan clan left join fetch clan.memberss where clan.id =:id")
    Clan findOneWithEagerRelationships(@Param("id") Long id);

}
