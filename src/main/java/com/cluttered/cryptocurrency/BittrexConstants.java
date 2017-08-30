package com.cluttered.cryptocurrency;

import java.math.BigDecimal;

public interface BittrexConstants {

    BigDecimal MINIMUM_TRADE = BigDecimal.valueOf(0.0005);      // 50k satoshi
    BigDecimal COMMISSION_PERCENT = BigDecimal.valueOf(0.0025); // 0.25%
}
