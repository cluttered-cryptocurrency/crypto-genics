package com.cluttered.cryptocurrency;

import com.cluttered.cryptocurrency.ann.NeuralNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    CircularEvictingQueueList<Long> getInputsQueue();

    default Action fire(final MarketSummary input) {
        setLastSummary(input);

        final CircularEvictingQueueList<Long> inputsQueueList = getInputsQueue();
        inputsQueueList.add(input.getLast());

        if(!inputsQueueList.isFull())
            return HOLD;

        final List<Double> normalizedInputs = normalizeInputList(inputsQueueList);

        final List<Double> outputs = getNeuralNetwork().fire(normalizedInputs);
        return Action.determine(outputs);
    }

    default List<Double> normalizeInputList(final CircularEvictingQueueList<Long> inputsQueueList) {
        final MinMax minMax = MinMax.process(inputsQueueList);
        return inputsQueueList.stream()
                .map(input -> normalizeInput(input, minMax))
                .collect(Collectors.toList());
    }

    default Double normalizeInput(final Long input, final MinMax minMax) {
        return (input - minMax.getMin()) / (minMax.getMax() - minMax.getMin());
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
