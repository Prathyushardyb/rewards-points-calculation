package com.customer.rewards.service;

import com.customer.rewards.entity.Purchase;
import com.customer.rewards.exception.NotFoundException;
import com.customer.rewards.model.PurchaseTo;
import com.customer.rewards.repository.PurchaseRepository;
import com.customer.rewards.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.customer.rewards.utils.ObjectMapperUtils.convertPurchaseDtoToPurchaseEntity;
import static com.customer.rewards.utils.ObjectMapperUtils.convertPurchaseEntityToPurchaseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Override
    public PurchaseTo getPurchaseById(Long id) {
        return convertPurchaseEntityToPurchaseDto(findById(id));
    }

    @Override
    public PurchaseTo save(PurchaseTo purchase) {
        Purchase entity = convertPurchaseDtoToPurchaseEntity(purchase);
        return convertPurchaseEntityToPurchaseDto(purchaseRepository.save(entity));
    }

    @Override
    public void delete(Long purchaseId) {
        purchaseRepository.deleteById(purchaseId);
    }

    @Override
    public void updatePurchase(Long id, PurchaseTo purchase) {
        Purchase existingPurchase = findById(id);
        existingPurchase.setPurchaseAmount(purchase.getPurchaseAmount());
        existingPurchase.setCustomerId(purchase.getCustomerId());
        existingPurchase.setPurchaseDate(purchase.getPurchaseDate());
        purchaseRepository.save(existingPurchase);
    }

    @Override
    public List<PurchaseTo> getAllPurchasesByCustomerIdAndPurchaseDateBetween(Long customerId, Timestamp from, Timestamp to) {
        List<Purchase> purchases = purchaseRepository.findAllByCustomerIdAndPurchaseDateBetween(customerId, from, to);
        return purchases.stream()
                .map(ObjectMapperUtils::convertPurchaseEntityToPurchaseDto)
                .collect(Collectors.toList());
    }

    private Purchase findById(Long purchaseId) {
        Optional<Purchase> purchaseFromDb = purchaseRepository.findById(purchaseId);
        if (!purchaseFromDb.isPresent()) {
            String message = String.format("Purchase does not exist with id - %s.", purchaseId);
            log.warn(message);
            throw new NotFoundException(message);
        }
        return purchaseFromDb.get();
    }
}
