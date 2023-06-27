package com.customer.rewards.rest;

import com.customer.rewards.model.PurchaseTo;
import com.customer.rewards.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;


    @GetMapping({"/{purchaseId}"})
    public ResponseEntity<PurchaseTo> getPurchase(@PathVariable Long purchaseId) {
        return new ResponseEntity<>(purchaseService.getPurchaseById(purchaseId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PurchaseTo> savePurchase(@RequestBody PurchaseTo purchase) {
        PurchaseTo savedPurchase = purchaseService.save(purchase);
        return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
    }

    @PutMapping({"/{purchaseId}"})
    public ResponseEntity<PurchaseTo> updatePurchase(@PathVariable("purchaseId") Long purchaseId, @RequestBody PurchaseTo purchase) {
        purchaseService.updatePurchase(purchaseId, purchase);
        return new ResponseEntity<>(purchaseService.getPurchaseById(purchaseId), HttpStatus.OK);
    }

    @DeleteMapping({"/{purchaseId}"})
    public ResponseEntity<PurchaseTo> deletePurchase(@PathVariable("purchaseId") Long purchaseId) {
        purchaseService.delete(purchaseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

