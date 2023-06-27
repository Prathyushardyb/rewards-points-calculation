package com.customer.rewards.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class DateUtils {

    public static Timestamp getDateBasedOnOffSetDays(int numberOfDaysToSubtract) {
        return Timestamp.valueOf(LocalDateTime.now().minusDays(numberOfDaysToSubtract));
    }
}
