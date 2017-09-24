package com.cluttered.cryptocurrency;

import com.cluttered.cryptocurrency.ann.NeuralNetwork;

import java.math.BigDecimal;
import java.util.List;

import static com.cluttered.cryptocurrency.Action.HOLD;

public interface MarketManager {

    MinMax getMinMax();

    String getMarket();

    NeuralNetwork getNeuralNetwork();

    long getBalance();

    void setBalance(final long balance);

    long getShares();

    void setShares(final long shares);

    long getMinimumTrade();

    MarketTick getLastSummary();

    void setLastSummary(final MarketTick lastSummary);

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
        setLastSummary(input);

        final Double normalizedInput = getMinMax().normalize(input.getLast());

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
