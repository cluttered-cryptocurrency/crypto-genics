package com.cluttered.cryptocurrency;

import com.cluttered.cryptocurrency.ann.NeuralNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static com.cluttered.cryptocurrency.Action.BUY;
import static com.cluttered.cryptocurrency.Action.HOLD;
import static com.cluttered.cryptocurrency.Action.SELL;

public interface MarketManager {

    Logger LOG = LoggerFactory.getLogger(MarketManager.class);

    String getMarket();

    NeuralNetwork getNeuralNetwork();

    long getBalance();

    void setBalance(final BigDecimal balance);

    long getShares();

    void setShares(final BigDecimal shares);

    long getMinimumTrade();

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

    CircularEvictingQueueList<Double> getInputsQueue();

    default Action fire(final MarketSummary input) {
        setLastSummary(input);

        final CircularEvictingQueueList<Double> inputsQueueList = getInputsQueue();
        inputsQueueList.add(Double.longBitsToDouble(input.getLast()));

        if(!inputsQueueList.isFull())
            return HOLD;

        final List<Double> outputs = getNeuralNetwork().fire(inputsQueueList);
        return Action.determine(outputs);
    }

    default void performAction(final Action action) {
        switch (action) {
            case BUY:
                incrementBuyAttempts();
                if (getBalance() < getMinimumTrade()) {
                    throw new LessThanMinimum(BUY);
                }
                // TODO: check if can purchase more shares
                if (getShares() == 0) {
                    incrementBuys();
                    performBuyAction();
                } else incrementSellHolds();
                break;
            case SELL:
                incrementSellAttempts();
                final long lastBid = getLastSummary().getBid();
                if (getShares() * lastBid < getMinimumTrade()) {
                    throw new LessThanMinimum(SELL);
                }
                if(getShares() > 0) {
                    incrementSells();
                    performSellAction();
                } else incrementBuyHolds();
                break;
            default:
                if(getShares() == 0) {
                    incrementBuyHolds();
                } else incrementSellHolds();
                performHoldAction();
        }
    }
}
