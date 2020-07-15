package com.github.malikin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@NoArgsConstructor
public class Book {

    private static final String ID_FOR_ALL_QUOTES = "0";
    private static final String ERROR_QUOTE_EXIST = "Quote with id=%s and type=%s is already exist";
    private static final String ERROR_QUOTE_NOT_EXIST = "Quote with id=%s and type=%s is not exist";

    @Getter
    private final List<Quote> quotes = new ArrayList<>();
    @Getter
    private final Set<Quote> offersResult = new TreeSet<>(Comparator.reverseOrder());
    @Getter
    private final Set<Quote> bidsResult = new TreeSet<>(Comparator.reverseOrder());

    private final Map<String, Quote> bids = new HashMap<>();
    private final Map<String, Quote> offers = new HashMap<>();

    public void loadData(final List<Quote> data) {
        quotes.addAll(data);

        quotes.forEach(quote -> {
            switch (quote.getOperation()) {
                case N:
                    switch (quote.getType()) {
                        case O: {
                            if (offers.containsKey(quote.getId())) {
                                throw new IllegalArgumentException(String.format(ERROR_QUOTE_EXIST, quote.getId(), quote.getType()));
                            }
                            offers.put(quote.getId(), quote);
                            break;
                        }
                        case B: {
                            if (bids.containsKey(quote.getId())) {
                                throw new IllegalArgumentException(String.format(ERROR_QUOTE_EXIST, quote.getId(), quote.getType()));
                            }
                            bids.put(quote.getId(), quote);
                            break;
                        }
                    }
                    break;
                case U:
                    switch (quote.getType()) {
                        case O: {
                            if (!offers.containsKey(quote.getId())) {
                                throw new IllegalArgumentException(String.format(ERROR_QUOTE_NOT_EXIST, quote.getId(), quote.getType()));
                            }
                            Quote quoteToUpdate = offers.get(quote.getId());
                            offers.put(quote.getId(), getUpdatedQuote(quoteToUpdate, quote));
                            break;
                        }
                        case B: {
                            if (!bids.containsKey(quote.getId())) {
                                throw new IllegalArgumentException(String.format(ERROR_QUOTE_NOT_EXIST, quote.getId(), quote.getType()));
                            }
                            Quote quoteToUpdate = bids.get(quote.getId());
                            bids.put(quote.getId(), getUpdatedQuote(quoteToUpdate, quote));
                            break;
                        }
                    }
                    break;
                case D:
                    switch (quote.getType()) {
                        case O: {
                            if (ID_FOR_ALL_QUOTES.equals(quote.getId())) {
                                offers.clear();
                                break;
                            }
                            if (!offers.containsKey(quote.getId())) {
                                throw new IllegalArgumentException(String.format(ERROR_QUOTE_NOT_EXIST, quote.getId(), quote.getType()));
                            }
                            offers.remove(quote.getId());
                            break;
                        }
                        case B: {
                            if (ID_FOR_ALL_QUOTES.equals(quote.getId())) {
                                bids.clear();
                                break;
                            }
                            if (!bids.containsKey(quote.getId())) {
                                throw new IllegalArgumentException(String.format(ERROR_QUOTE_NOT_EXIST, quote.getId(), quote.getType()));
                            }
                            bids.remove(quote.getId());
                            break;
                        }
                    }
                    break;
                default:
                    break;
            }
        });

        offersResult.addAll(offers.values());
        bidsResult.addAll(bids.values());
    }

    private static Quote getUpdatedQuote(Quote quote, Quote update) {

        return new Quote(
                quote.getId(),
                quote.getType(),
                quote.getOperation(),
                update.getPrice(),
                update.getVolume(),
                update.getTime()
        );
    }
}
