package com.cluttered.cryptocurrency;

import com.cluttered.cryptocurrency.ann.NeuralNetwork;

import java.math.BigDecimal;
import java.util.List;

import static com.cluttered.cryptocurrency.Action.HOLD;

public interface MarketManager {

    String getMarket();

    NeuralNetwork getNeuralNetwork();

    long getBalance();

    void setBalance(final long balance);

    long getShares();

    void setShares(final long shares);

    long getMinimumTrade();

    MarketTick getLastTick();

    void setLastTick(final MarketTick lastTick);

    MarketTick getPreviousTick();

    void setPreviousTick(final MarketTick previousTick);

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

    default Action fire(final MarketTick input) {
        setPreviousTick(getLastTick());
        setLastTick(input);

        if(getPreviousTick() == null) {
            return HOLD;
        }

        final Double normalizedInput = Math.log(getLastTick().getLast()/getPreviousTick().getLast());

        final CircularEvictingQueueList<Double> inputsQueueList = getInputsQueue();
        inputsQueueList.add(normalizedInput);

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
                if (getShares() == 0 && getBalance() > getLastTick().getLast()) {
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
