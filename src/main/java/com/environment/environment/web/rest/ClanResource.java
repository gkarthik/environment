package com.environment.environment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.environment.environment.domain.Clan;
import com.environment.environment.repository.ClanRepository;
import com.environment.environment.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Clan.
 */
@RestController
@RequestMapping("/api")
public class ClanResource {

    private final Logger log = LoggerFactory.getLogger(ClanResource.class);

    @Inject
    private ClanRepository clanRepository;

    /**
     * POST  /clans -> Create a new clan.
     */
    @RequestMapping(value = "/clans",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Clan clan) throws URISyntaxException {
        log.debug("REST request to save Clan : {}", clan);
        if (clan.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new clan cannot already have an ID").build();
        }
        clanRepository.save(clan);
        return ResponseEntity.created(new URI("/api/clans/" + clan.getId())).build();
    }

    /**
     * PUT  /clans -> Updates an existing clan.
     */
    @RequestMapping(value = "/clans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Clan clan) throws URISyntaxException {
        log.debug("REST request to update Clan : {}", clan);
        if (clan.getId() == null) {
            return create(clan);
        }
        clanRepository.save(clan);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /clans -> get all the clans.
     */
    @RequestMapping(value = "/clans",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Clan>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Clan> page = clanRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clans", offset, limit);
        return new ResponseEntity<List<Clan>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clans/:id -> get the "id" clan.
     */
    @RequestMapping(value = "/clans/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clan> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Clan : {}", id);
        Clan clan = clanRepository.findOneWithEagerRelationships(id);
        if (clan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clan, HttpStatus.OK);
    }

    /**
     * DELETE  /clans/:id -> delete the "id" clan.
     */
    @RequestMapping(value = "/clans/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Clan : {}", id);
        clanRepository.delete(id);
    }
}
