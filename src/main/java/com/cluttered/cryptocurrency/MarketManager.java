package com.cluttered.cryptocurrency;

import com.cluttered.cryptocurrency.ann.NeuralNetwork;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static com.cluttered.cryptocurrency.Action.HOLD;

public interface MarketManager {

    Logger LOG = LoggerFactory.getLogger(MarketManager.class);

    String getMarket();

    NeuralNetwork getNeuralNetwork();

    BigDecimal getBalance();

    void setBalance(final BigDecimal balance);

    BigDecimal getShares();

    void setShares(final BigDecimal shares);

    BigDecimal getMinimumTrade();

    MarketSummary getLastSummary();

    void setLastSummary(final MarketSummary lastSummary);

    void performBuyAction();

    void performHoldAction();

    void performSellAction();

    BigDecimal getCommissionPercent();

    void incrementBuyAttempts();

    void incrementBuys();

    void incrementSellAttempts();

    void incrementSells();

    void incrementSellHolds();

    void incrementBuyHolds();

    EvictingQueue<Double> getInputsQueue();

    default Action fire(final MarketSummary input) {
        setLastSummary(input);

        final EvictingQueue<Double> inputsQueue = getInputsQueue();
        inputsQueue.add(input.getLast());

        if(inputsQueue.remainingCapacity() > 0)
            return HOLD;

        final List<Double> inputs = Lists.newArrayList(inputsQueue);
        final List<Double> outputs = getNeuralNetwork().fire(inputs);
        return Action.determine(outputs);
    }

    default void performAction(final Action action) {
        switch (action) {
            case BUY:
                incrementBuyAttempts();
                if (getBalance().compareTo(getMinimumTrade()) < 0) {
                    LOG.warn("Less than minimum required to perform buy");
                    return;
                }
                // TODO: check if can purchase more shares
                if (getShares().compareTo(BigDecimal.ZERO) == 0) {
                    incrementBuys();
                    performBuyAction();
                } else incrementSellHolds();
                break;
            case SELL:
                incrementSellAttempts();
                final BigDecimal lastBid = BigDecimal.valueOf(getLastSummary().getBid());
                if (getShares().multiply(lastBid).compareTo(getMinimumTrade()) < 0) {
                    LOG.warn("Less than minimum required to perform sell");
                    return;
                }
                if(getShares().compareTo(BigDecimal.ZERO) > 0) {
                    incrementSells();
                    performSellAction();
                } else incrementBuyHolds();
                break;
            default:
                if(getShares().compareTo(BigDecimal.ZERO) == 0) {
                    incrementBuyHolds();
                } else incrementSellHolds();
                performHoldAction();
        }
    }
}
