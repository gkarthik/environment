package com.environment.environment.web.rest;

import com.environment.environment.Application;
import com.environment.environment.domain.Battleground;
import com.environment.environment.repository.BattlegroundRepository;

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
 * Test class for the BattlegroundResource REST controller.
 *
 * @see BattlegroundResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BattlegroundResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_LOCATION = "SAMPLE_TEXT";
    private static final String UPDATED_LOCATION = "UPDATED_TEXT";

    @Inject
    private BattlegroundRepository battlegroundRepository;

    private MockMvc restBattlegroundMockMvc;

    private Battleground battleground;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BattlegroundResource battlegroundResource = new BattlegroundResource();
        ReflectionTestUtils.setField(battlegroundResource, "battlegroundRepository", battlegroundRepository);
        this.restBattlegroundMockMvc = MockMvcBuilders.standaloneSetup(battlegroundResource).build();
    }

    @Before
    public void initTest() {
        battleground = new Battleground();
        battleground.setName(DEFAULT_NAME);
        battleground.setLocation(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createBattleground() throws Exception {
        int databaseSizeBeforeCreate = battlegroundRepository.findAll().size();

        // Create the Battleground
        restBattlegroundMockMvc.perform(post("/api/battlegrounds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(battleground)))
                .andExpect(status().isCreated());

        // Validate the Battleground in the database
        List<Battleground> battlegrounds = battlegroundRepository.findAll();
        assertThat(battlegrounds).hasSize(databaseSizeBeforeCreate + 1);
        Battleground testBattleground = battlegrounds.get(battlegrounds.size() - 1);
        assertThat(testBattleground.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBattleground.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void getAllBattlegrounds() throws Exception {
        // Initialize the database
        battlegroundRepository.saveAndFlush(battleground);

        // Get all the battlegrounds
        restBattlegroundMockMvc.perform(get("/api/battlegrounds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(battleground.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getBattleground() throws Exception {
        // Initialize the database
        battlegroundRepository.saveAndFlush(battleground);

        // Get the battleground
        restBattlegroundMockMvc.perform(get("/api/battlegrounds/{id}", battleground.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(battleground.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBattleground() throws Exception {
        // Get the battleground
        restBattlegroundMockMvc.perform(get("/api/battlegrounds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBattleground() throws Exception {
        // Initialize the database
        battlegroundRepository.saveAndFlush(battleground);

		int databaseSizeBeforeUpdate = battlegroundRepository.findAll().size();

        // Update the battleground
        battleground.setName(UPDATED_NAME);
        battleground.setLocation(UPDATED_LOCATION);
        restBattlegroundMockMvc.perform(put("/api/battlegrounds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(battleground)))
                .andExpect(status().isOk());

        // Validate the Battleground in the database
        List<Battleground> battlegrounds = battlegroundRepository.findAll();
        assertThat(battlegrounds).hasSize(databaseSizeBeforeUpdate);
        Battleground testBattleground = battlegrounds.get(battlegrounds.size() - 1);
        assertThat(testBattleground.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBattleground.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void deleteBattleground() throws Exception {
        // Initialize the database
        battlegroundRepository.saveAndFlush(battleground);

		int databaseSizeBeforeDelete = battlegroundRepository.findAll().size();

        // Get the battleground
        restBattlegroundMockMvc.perform(delete("/api/battlegrounds/{id}", battleground.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Battleground> battlegrounds = battlegroundRepository.findAll();
        assertThat(battlegrounds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
