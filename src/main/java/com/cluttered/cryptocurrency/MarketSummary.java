package com.cluttered.cryptocurrency;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Objects;

@Entity("summaries")
public class MarketSummary {

    @Id
    private ObjectId id;

    @Property("Last")
    private long last;

    @Property("Bid")
    private long bid;
    @Property("Ask")
    private long ask;

    public MarketSummary() {
        // Morphia Constructor
    }

    public MarketSummary(final ObjectId id, final long last,
                         final long bid, final long ask) {
        this.id = id;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
    }

    public ObjectId getId() {
        return id;
    }

    public long getLast() {
        return last;
    }

    public long getBid() {
        return bid;
    }

    public long getAsk() {
        return ask;
    }

    @Override
    public boolean equals(final Object obj) {
        if(Objects.isNull(obj)) return false;
        final MarketSummary that = (MarketSummary) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}