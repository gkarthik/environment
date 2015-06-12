package com.environment.environment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.environment.environment.domain.Battleground;
import com.environment.environment.repository.BattlegroundRepository;
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
 * REST controller for managing Battleground.
 */
@RestController
@RequestMapping("/api")
public class BattlegroundResource {

    private final Logger log = LoggerFactory.getLogger(BattlegroundResource.class);

    @Inject
    private BattlegroundRepository battlegroundRepository;

    /**
     * POST  /battlegrounds -> Create a new battleground.
     */
    @RequestMapping(value = "/battlegrounds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Battleground battleground) throws URISyntaxException {
        log.debug("REST request to save Battleground : {}", battleground);
        if (battleground.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new battleground cannot already have an ID").build();
        }
        battlegroundRepository.save(battleground);
        return ResponseEntity.created(new URI("/api/battlegrounds/" + battleground.getId())).build();
    }

    /**
     * PUT  /battlegrounds -> Updates an existing battleground.
     */
    @RequestMapping(value = "/battlegrounds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Battleground battleground) throws URISyntaxException {
        log.debug("REST request to update Battleground : {}", battleground);
        if (battleground.getId() == null) {
            return create(battleground);
        }
        battlegroundRepository.save(battleground);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /battlegrounds -> get all the battlegrounds.
     */
    @RequestMapping(value = "/battlegrounds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Battleground>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Battleground> page = battlegroundRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/battlegrounds", offset, limit);
        return new ResponseEntity<List<Battleground>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /battlegrounds/:id -> get the "id" battleground.
     */
    @RequestMapping(value = "/battlegrounds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Battleground> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Battleground : {}", id);
        Battleground battleground = battlegroundRepository.findOne(id);
        if (battleground == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(battleground, HttpStatus.OK);
    }

    /**
     * DELETE  /battlegrounds/:id -> delete the "id" battleground.
     */
    @RequestMapping(value = "/battlegrounds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Battleground : {}", id);
        battlegroundRepository.delete(id);
    }
}
