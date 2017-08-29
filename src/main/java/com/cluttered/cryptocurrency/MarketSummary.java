package com.cluttered.cryptocurrency;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Entity("summaries")
public class MarketSummary {

    @Id
    private ObjectId id;

    @Property("Last")
    private Double last;

    @Property("Bid")
    private Double bid;
    @Property("Ask")
    private Double ask;

    public MarketSummary(final ObjectId id, final Double last,
                         final Double bid, final Double ask) {
        this.id = id;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
    }

    public ObjectId getId() {
        return id;
    }

    public Double getLast() {
        return last;
    }

    public Double getBid() {
        return bid;
    }

    public Double getAsk() {
        return ask;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        final MarketSummary that = (MarketSummary) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}