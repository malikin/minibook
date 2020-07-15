package com.github.malikin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Getter
final class Quote implements Comparable<Quote> {

    private final String id;

    private final QuoteType type;

    private final QuoteOperation operation;

    private final BigDecimal price;

    private final int volume;

    private final Instant time;

    @Override
    public String toString() {
        return String.format("%s/%s/%s/%.2f/%d", id, type, operation, price, volume);
    }

    public static Quote parseQuote(String tuple) {
        if (tuple.isBlank()) {
            throw new IllegalArgumentException();
        }

        String[] input = tuple.strip().split("/");
        return new Quote(
                input[0],
                QuoteType.valueOf(input[1]),
                QuoteOperation.valueOf(input[2]),
                new BigDecimal(input[3]),
                Integer.parseInt(input[4]),
                Instant.now()
        );
    }

    @Override
    public int compareTo(Quote o) {
        if (this == o) {
            return 0;
        }

        int priceCompare = this.price.compareTo(o.price);
        if (priceCompare > 0) {
            if (this.type.equals(QuoteType.O)) {
                return -1;
            }
            return 1;
        }
        if (priceCompare < 0) {
            if (this.type.equals(QuoteType.O)) {
                return 1;
            }
            return -1;
        }

        if (this.volume > o.volume) {
            return 1;
        }
        if (this.volume < o.volume) {
            return -1;
        }
        if (this.time.isAfter(o.time)) {
            return 1;
        }

        return -1;
    }
}
