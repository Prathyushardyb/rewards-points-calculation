package com.customer.rewards.rest;

import com.customer.rewards.model.Rewards;
import com.customer.rewards.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class RewardsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RewardsService rewardsService;

    @Test
    void testGetRewardsByCustomerIdSHouldReturnNotFoundIfNoCustomerExist() throws Exception {
        mockMvc.perform(get("/rewards/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRewardsByCustomerId() throws Exception {
        Rewards  rewards = new Rewards();
        rewards.setCustomerId(1L);
        rewards.setLastMonthRewardPoints(100);
        rewards.setLastSecondMonthRewardPoints(100);
        rewards.setTotalRewardPoints(200);

        when(rewardsService.getRewardsByCustomerId(anyLong())).thenReturn(rewards);
        mockMvc.perform(get("/rewards/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.lastMonthRewardPoints").value(100))
                .andExpect(jsonPath("$.totalRewardPoints").value(200));

    }

}
