package com.customer.rewards.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.customer.rewards.model.PurchaseTo;
import com.customer.rewards.model.Rewards;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import static com.customer.rewards.constants.Constants.DAYS_IN_MONTH;
import static com.customer.rewards.constants.Constants.DAYS_IN_THREE_MONTHS;
import static com.customer.rewards.constants.Constants.DAYS_IN_TWO_MONTHS;
import static com.customer.rewards.constants.Constants.MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_REWARDS;
import static com.customer.rewards.constants.Constants.MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_TWO_X_REWARDS;
import static com.customer.rewards.constants.Constants.REWARD_MULITPLIER;
import static com.customer.rewards.utils.DateUtils.getDateBasedOnOffSetDays;

@Service
@Slf4j
@RequiredArgsConstructor
public class RewardsServiceImpl implements RewardsService {


    private final PurchaseService purchaseService;
    private final CustomerService customerService;

    public Rewards getRewardsByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId);
        Timestamp lastThirdMonthTimestamp = getDateBasedOnOffSetDays(DAYS_IN_THREE_MONTHS);
        List<PurchaseTo> lastThreeMonthsTransactions = purchaseService
                .getAllPurchasesByCustomerIdAndPurchaseDateBetween(customerId, lastThirdMonthTimestamp,
                        Timestamp.from(Instant.now()));

        return calculateRewards(lastThreeMonthsTransactions);

    }

    private Rewards calculateRewards(List<PurchaseTo> threeMonthPurchases) {
        Timestamp lastMonthTimestamp = getDateBasedOnOffSetDays(DAYS_IN_MONTH);
        Timestamp lastSecondMonthTimestamp = getDateBasedOnOffSetDays(DAYS_IN_TWO_MONTHS);
        Timestamp lastThirdMonthTimestamp = getDateBasedOnOffSetDays(DAYS_IN_THREE_MONTHS);
        List<PurchaseTo> lastThirdMonthPurchases = purchasesBetween(threeMonthPurchases, lastThirdMonthTimestamp, lastSecondMonthTimestamp);
        List<PurchaseTo> lastSecondMonthPurchases = purchasesBetween(threeMonthPurchases, lastSecondMonthTimestamp, lastMonthTimestamp);
        List<PurchaseTo> lastMonthPurchases = purchasesBetween(threeMonthPurchases, lastMonthTimestamp, Timestamp.valueOf(LocalDateTime.now()));
        Rewards rewards = new Rewards();
        if (!threeMonthPurchases.isEmpty()) {
            rewards.setCustomerId(threeMonthPurchases.get(0).getCustomerId());
        }
        rewards.setLastThirdMonthRewardPoints(getRewardsPerMonth(lastThirdMonthPurchases));
        rewards.setLastSecondMonthRewardPoints(getRewardsPerMonth(lastSecondMonthPurchases));
        rewards.setLastMonthRewardPoints(getRewardsPerMonth(lastMonthPurchases));
        rewards.setTotalRewardPoints(rewards.getLastMonthRewardPoints() + rewards.getLastSecondMonthRewardPoints() + rewards.getLastThirdMonthRewardPoints());
        return rewards;

    }

    private List<PurchaseTo> purchasesBetween(List<PurchaseTo> purchases, Timestamp from, Timestamp to) {
        return purchases.stream().filter(p -> p.getPurchaseDate().compareTo(from) >= 0 &&
                p.getPurchaseDate().compareTo(to) < 0).collect(Collectors.toList());
    }

    private Long getRewardsPerMonth(List<PurchaseTo> purchases) {
        return purchases.stream().map(purchase -> calculateRewards(purchase))
                .collect(Collectors.summingLong(returnValue -> returnValue.longValue()));
    }

    private Long calculateRewards(PurchaseTo purchase) {
        double purchasedAmount = purchase.getPurchaseAmount();
        if (purchasedAmount > MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_REWARDS &&
                purchasedAmount <= MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_TWO_X_REWARDS) {
            return Math.round(purchasedAmount - MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_REWARDS);
        } else if (purchasedAmount > MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_TWO_X_REWARDS) {
            return Math.round(purchasedAmount - MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_TWO_X_REWARDS) * REWARD_MULITPLIER
                    + (MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_TWO_X_REWARDS - MINIMUM_ELIGIBLE_PURCHASE_AMT_TO_GAIN_REWARDS);
        } else
            return 0l;

    }

}
