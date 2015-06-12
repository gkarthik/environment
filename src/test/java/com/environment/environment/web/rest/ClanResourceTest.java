package com.environment.environment.web.rest;

import com.environment.environment.Application;
import com.environment.environment.domain.Clan;
import com.environment.environment.repository.ClanRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClanResource REST controller.
 *
 * @see ClanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ClanResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private ClanRepository clanRepository;

    private MockMvc restClanMockMvc;

    private Clan clan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClanResource clanResource = new ClanResource();
        ReflectionTestUtils.setField(clanResource, "clanRepository", clanRepository);
        this.restClanMockMvc = MockMvcBuilders.standaloneSetup(clanResource).build();
    }

    @Before
    public void initTest() {
        clan = new Clan();
        clan.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createClan() throws Exception {
        int databaseSizeBeforeCreate = clanRepository.findAll().size();

        // Create the Clan
        restClanMockMvc.perform(post("/api/clans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clan)))
                .andExpect(status().isCreated());

        // Validate the Clan in the database
        List<Clan> clans = clanRepository.findAll();
        assertThat(clans).hasSize(databaseSizeBeforeCreate + 1);
        Clan testClan = clans.get(clans.size() - 1);
        assertThat(testClan.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllClans() throws Exception {
        // Initialize the database
        clanRepository.saveAndFlush(clan);

        // Get all the clans
        restClanMockMvc.perform(get("/api/clans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clan.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getClan() throws Exception {
        // Initialize the database
        clanRepository.saveAndFlush(clan);

        // Get the clan
        restClanMockMvc.perform(get("/api/clans/{id}", clan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClan() throws Exception {
        // Get the clan
        restClanMockMvc.perform(get("/api/clans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClan() throws Exception {
        // Initialize the database
        clanRepository.saveAndFlush(clan);

		int databaseSizeBeforeUpdate = clanRepository.findAll().size();

        // Update the clan
        clan.setName(UPDATED_NAME);
        restClanMockMvc.perform(put("/api/clans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clan)))
                .andExpect(status().isOk());

        // Validate the Clan in the database
        List<Clan> clans = clanRepository.findAll();
        assertThat(clans).hasSize(databaseSizeBeforeUpdate);
        Clan testClan = clans.get(clans.size() - 1);
        assertThat(testClan.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteClan() throws Exception {
        // Initialize the database
        clanRepository.saveAndFlush(clan);

		int databaseSizeBeforeDelete = clanRepository.findAll().size();

        // Get the clan
        restClanMockMvc.perform(delete("/api/clans/{id}", clan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Clan> clans = clanRepository.findAll();
        assertThat(clans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
