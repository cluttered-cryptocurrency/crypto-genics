package com.cluttered.cryptocurrency;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Date;
import java.util.Objects;

@Entity("ticker")
public class MarketTick {

    @Id
    private ObjectId id;

    @Property("TimeStamp")
    private Date timestamp;

    @Property("Last")
    private long last;

    @Property("Bid")
    private long bid;
    @Property("Ask")
    private long ask;

    public MarketTick() {
        // Morphia Constructor
    }

    public Date getTimestamp() {
        return timestamp;
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
        final MarketTick that = (MarketTick) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}