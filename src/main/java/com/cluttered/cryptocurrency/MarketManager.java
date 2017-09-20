package com.cluttered.cryptocurrency;

import com.cluttered.cryptocurrency.ann.NeuralNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static com.cluttered.cryptocurrency.Action.HOLD;

public interface MarketManager {

    Logger LOG = LoggerFactory.getLogger(MarketManager.class);

    String getMarket();

    NeuralNetwork getNeuralNetwork();

    long getBalance();

    void setBalance(final long balance);

    long getShares();

    void setShares(final long shares);

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
        inputsQueueList.add((double) input.getLast());

        if(!inputsQueueList.isFull())
            return HOLD;

        final List<Double> outputs = getNeuralNetwork().fire(inputsQueueList);
        return Action.determine(outputs);
    }

    default void performAction(final Action action) {
        switch (action) {
            case BUY:
                incrementBuyAttempts();
                // TODO: check if can purchase more shares
                if (getShares() == 0 && getBalance() > getLastSummary().getLast()) {
                    incrementBuys();
                    performBuyAction();
                } else incrementSellHolds();
                break;
            case SELL:
                incrementSellAttempts();
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
