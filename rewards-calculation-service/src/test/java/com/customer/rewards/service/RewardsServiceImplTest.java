package com.customer.rewards.service;

import com.customer.rewards.model.CustomerTo;
import com.customer.rewards.model.PurchaseTo;
import com.customer.rewards.model.Rewards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.customer.rewards.utils.DateUtils.getDateBasedOnOffSetDays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class RewardsServiceImplTest {

    private RewardsServiceImpl rewardsService;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
        rewardsService = new RewardsServiceImpl(purchaseService, customerService);
    }

    @Test
    public void getRewardsByCustomerIdForLastMonth() {

        //given
        Long customerId = 100L;
        when(customerService.getCustomerById(customerId)).thenReturn(new CustomerTo());

        Map<Timestamp, Double> purchasesByDate = new HashMap<>();
        purchasesByDate.put(getDateBasedOnOffSetDays(1), 100.00);
        purchasesByDate.put(getDateBasedOnOffSetDays(2), 100.00);

        //Time -1
        when(purchaseService.getAllPurchasesByCustomerIdAndPurchaseDateBetween(anyLong(),
                any(Timestamp.class), any(Timestamp.class))).thenReturn(mockPurchases(customerId, purchasesByDate));

        //when
        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        //then
        //Expected to get 100 points as there are two 100$ purchase in the last month
        assertEquals(100L, rewards.getLastMonthRewardPoints());
    }

    @Test
    public void getRewardsByCustomerIdForLastMonthMultiplePurchases() {

        //given
        Long customerId = 100L;
        when(customerService.getCustomerById(customerId)).thenReturn(new CustomerTo());

        //Mock multiple purchases 100$ ,50$ and 200$ in a same month
        Map<Timestamp, Double> purchasesByDate = new HashMap<>();
        purchasesByDate.put(getDateBasedOnOffSetDays(1), 100.00);
        purchasesByDate.put(getDateBasedOnOffSetDays(2), 50.00);
        purchasesByDate.put(getDateBasedOnOffSetDays(2), 200.00);
        when(purchaseService.getAllPurchasesByCustomerIdAndPurchaseDateBetween(anyLong(),
                any(Timestamp.class), any(Timestamp.class))).thenReturn(mockPurchases(customerId, purchasesByDate));

        //when
        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        //then
        //Expected to get 300 points as there are three purchase in the last month
        //100$ purchase - 50points
        //50$ purchase - no points
        //200$ purchase - 250 points
        assertEquals(300L, rewards.getLastMonthRewardPoints());
    }

    @Test
    public void getRewardsByCustomerIdForLastTwoMonths() {

        //given
        Long customerId = 100L;
        when(customerService.getCustomerById(customerId)).thenReturn(new CustomerTo());
        //Mock the purchases to span into last two months
        Map<Timestamp, Double> purchasesByDate = new HashMap<>();
        purchasesByDate.put(getDateBasedOnOffSetDays(1), 100.00);
        purchasesByDate.put(getDateBasedOnOffSetDays(40), 100.00);
        when(purchaseService.getAllPurchasesByCustomerIdAndPurchaseDateBetween(anyLong(), any(Timestamp.class), any(Timestamp.class))).thenReturn(mockPurchases(customerId, purchasesByDate));

        //when
        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        //then
        //Expected to get 50 points each month as there are two 100$ purchases in each month
        assertEquals(50L, rewards.getLastMonthRewardPoints());
        assertEquals(50L, rewards.getLastSecondMonthRewardPoints());

    }

    @Test
    public void getRewardsByCustomerIdForLastThreeMonths() {

        //given
        Long customerId = 100L;
        when(customerService.getCustomerById(customerId)).thenReturn(new CustomerTo());
        //Mock the purchases to span into last two months
        Map<Timestamp, Double> purchasesByDate = new HashMap<>();
        purchasesByDate.put(getDateBasedOnOffSetDays(1), 100.00);
        purchasesByDate.put(getDateBasedOnOffSetDays(40), 100.00);
        purchasesByDate.put(getDateBasedOnOffSetDays(70), 100.00);
        purchasesByDate.put(getDateBasedOnOffSetDays(75), 50.00);

        when(purchaseService.getAllPurchasesByCustomerIdAndPurchaseDateBetween(anyLong(), any(Timestamp.class), any(Timestamp.class))).thenReturn(mockPurchases(customerId, purchasesByDate));

        //when
        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        //then
        //Expected to get 50 points each month as there are 100$ purchase in each month
        assertEquals(50L, rewards.getLastMonthRewardPoints());
        assertEquals(50L, rewards.getLastSecondMonthRewardPoints());
        assertEquals(50L, rewards.getLastThirdMonthRewardPoints());

    }


    private List<PurchaseTo> mockPurchases(Long customerId, Map<Timestamp, Double> amountsByDate) {
        List<PurchaseTo> purchaseTos = new ArrayList<>();
        for (Timestamp timestamp : amountsByDate.keySet()) {
            PurchaseTo purchase = new PurchaseTo();
            purchase.setCustomerId(customerId);
            purchase.setPurchaseDate(timestamp);
            purchase.setPurchaseAmount(amountsByDate.get(timestamp));
            purchaseTos.add(purchase);
        }
        return purchaseTos;
    }


}
