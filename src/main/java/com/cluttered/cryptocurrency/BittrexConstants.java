package com.cluttered.cryptocurrency;

import java.math.BigDecimal;

public interface BittrexConstants {

    // 50k satoshi
    long MINIMUM_TRADE = 50_000;

    // 0.25%
    BigDecimal COMMISSION_PERCENT = BigDecimal.valueOf(0.0025);
}