package com.mglvm.salesstatistics.model.dto;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

public final class SalesOrder implements Comparable<SalesOrder> , Comparator<SalesOrder> {

    final static AtomicLong NEXT_ID = new AtomicLong(1);

    private final long id = NEXT_ID.getAndIncrement();

    private final double amount;

    private final long timestamp;

    public SalesOrder(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesOrder)) return false;

        SalesOrder that = (SalesOrder) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }

    @Override

    public int compareTo(SalesOrder o) {
        return (this.id > o.id ) ? -1: (this.id < o.id ) ? 1 : 0 ;
    }

    @Override
    public int compare(SalesOrder o1, SalesOrder o2) {
        return o1.getId() == o2.getId() ? 0 : 1;
    }
}
