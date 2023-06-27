package com.customer.rewards.rest;

import com.customer.rewards.model.PurchaseTo;
import com.customer.rewards.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class PurchaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PurchaseService purchaseService;

    @Test
    void testGetPurchaseByIdShouldReturnNotFoundIfNoPurchaseExist() throws Exception {
        mockMvc.perform(get("/purchase/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPurchaseById() throws Exception {
        PurchaseTo purchaseTo = new PurchaseTo();
        purchaseTo.setId(1L);
        purchaseTo.setPurchaseAmount(100.10);
        when(purchaseService.getPurchaseById(anyLong())).thenReturn(purchaseTo);
        mockMvc.perform(get("/purchase/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseAmount").value(100.10));
    }

    @Test
    void testSavePurchase() throws Exception {
        String content = "{\"customerId\": \"1\",\"purchaseAmount\": 200 }";
        mockMvc.perform(post("/purchase")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/purchase/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePurchase() throws Exception {
        String content = "{\"customerId\": \"1\",\"purchaseAmount\": 200 }";
        mockMvc.perform(put("/purchase/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(purchaseService).updatePurchase(anyLong(),any(PurchaseTo.class));
    }

    @Test
    void testDeletePurchase() throws Exception {

        mockMvc.perform(delete("/purchase/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(purchaseService).delete(anyLong());
    }
}
