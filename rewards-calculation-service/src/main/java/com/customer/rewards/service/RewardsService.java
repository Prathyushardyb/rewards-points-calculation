package com.customer.rewards.service;

import com.customer.rewards.model.Rewards;


public interface RewardsService {
    Rewards getRewardsByCustomerId(Long customerId);
}
