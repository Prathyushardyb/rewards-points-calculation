package com.customer.rewards.service;

import com.customer.rewards.entity.Purchase;
import com.customer.rewards.exception.NotFoundException;
import com.customer.rewards.model.PurchaseTo;
import com.customer.rewards.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PurchaseServiceImplTest {

    private PurchaseServiceImpl purchaseServiceImpl;

    @Mock
    private PurchaseRepository purchaseRepository;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
        purchaseServiceImpl = new PurchaseServiceImpl(purchaseRepository);
    }

    @Test
    public void getPurchaseById() {

        //given
        Long purchaseId = 1L;
        Purchase mockedPurchase = mockPurchase(purchaseId);
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(mockedPurchase));

        //then
        PurchaseTo purchaseTo = purchaseServiceImpl.getPurchaseById(purchaseId);
        assertEquals(mockedPurchase.getId(), purchaseTo.getId());
        assertEquals(mockedPurchase.getCustomerId(), purchaseTo.getCustomerId());
        assertEquals(mockedPurchase.getPurchaseAmount(), purchaseTo.getPurchaseAmount());

    }

    @Test
    public void savePurchase() {
        //given
        Long purchaseId = 1L;
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(mockPurchase(purchaseId));

        //then
        PurchaseTo purchaseTo = purchaseServiceImpl.save(mockPurchaseTo());
        assertEquals(purchaseId, purchaseTo.getId());
    }

    @Test
    public void getPurchaseByIdShouldThrowNotFoundExceptionIfPurchaseNotExistWithTheId() {
        //given
        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(NotFoundException.class, () -> purchaseServiceImpl.getPurchaseById(1L));
    }

    @Test
    public void delete() {
        //given
        Long purchaseId = 1L;
        //then
        purchaseServiceImpl.delete(purchaseId);
        verify(purchaseRepository).deleteById(purchaseId);
    }

    @Test
    public void getAllPurchasesByCustomerIdAndPurchaseDateBetween() {
        //given
        Long customerId = 1L;
        Purchase mockedPurchase = mockPurchase(customerId);
        when(purchaseRepository
                .findAllByCustomerIdAndPurchaseDateBetween(anyLong(), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(asList(mockedPurchase));
        //then
        List<PurchaseTo> purchases = purchaseServiceImpl.getAllPurchasesByCustomerIdAndPurchaseDateBetween(customerId, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        assertFalse(purchases.isEmpty());
    }

    @Test
    public void updatePurchase() {
        //given
        Long purchaseId = 1L;
        Purchase mockedPurchase = mockPurchase(purchaseId);
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(mockedPurchase));
        //then
        purchaseServiceImpl.updatePurchase(purchaseId, mockPurchaseTo());
        verify(purchaseRepository).save(any(Purchase.class));
    }


    private Purchase mockPurchase(Long purchaseId) {
        Purchase purchase = new Purchase();
        purchase.setCustomerId(1L);
        purchase.setId(purchaseId);
        purchase.setPurchaseAmount(1);
        return purchase;
    }

    private PurchaseTo mockPurchaseTo() {
        PurchaseTo purchaseTo = new PurchaseTo();
        purchaseTo.setCustomerId(1L);
        purchaseTo.setPurchaseAmount(1);
        return purchaseTo;
    }

}
