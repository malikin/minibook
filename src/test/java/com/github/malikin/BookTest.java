package com.github.malikin;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void testBook() {
        var book = new Book();
        book.loadData(getBaseInput());

        final Set<Quote> offersResult = book.getOffersResult();
        final Set<Quote> bidsResult = book.getBidsResult();

        assertEquals(2, offersResult.size());
        assertEquals(4, bidsResult.size());

        assertEquals("Q1", new ArrayList<>(offersResult).get(0).getId());
        assertEquals("Q6", new ArrayList<>(offersResult).get(offersResult.size() - 1).getId());

        assertEquals("Q3", new ArrayList<>(bidsResult).get(0).getId());
        assertEquals("Q5", new ArrayList<>(bidsResult).get(bidsResult.size() - 1).getId());
    }

    @Test
    public void testFullOfferRemove() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("0/O/D/0/0"));

        var book = new Book();
        book.loadData(quotes);

        assertEquals(0, book.getOffersResult().size());
        assertEquals(4, book.getBidsResult().size());
    }

    @Test
    public void testFullBidRemove() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("0/B/D/0/0"));

        var book = new Book();
        book.loadData(quotes);

        assertEquals(2, book.getOffersResult().size());
        assertEquals(0, book.getBidsResult().size());
    }

    @Test
    public void testDeleteBid() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("Q3/B/D/0/0"));

        var book = new Book();
        book.loadData(quotes);

        assertEquals(3, book.getBidsResult().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOfferNotExist() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("QQ/O/D/0/0"));

        var book = new Book();
        book.loadData(quotes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteBidNotExist() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("QQ/B/D/0/0"));

        var book = new Book();
        book.loadData(quotes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOfferNotExist() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("QQ/O/U/0/0"));

        var book = new Book();
        book.loadData(quotes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateBidNotExist() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("QQ/B/U/0/0"));

        var book = new Book();
        book.loadData(quotes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleOfferInsert() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("Q6/O/N/1.32/1000000"));

        var book = new Book();
        book.loadData(quotes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleBidInsert() {
        final List<Quote> quotes = getBaseInput();
        quotes.add(Quote.parseQuote("Q5/B/N/1.20/500000"));

        var book = new Book();
        book.loadData(quotes);
    }

    private List<Quote> getBaseInput() {
        String[] input = {
                "Q1/O/N/1.31/1000000",
                "Q2/B/N/1.21/1000000",
                "Q3/B/N/1.22/1000000",
                "Q4/B/N/1.20/1000000",
                "Q5/B/N/1.20/1000000",
                "Q6/O/N/1.32/1000000",
                "Q7/O/N/1.33/200000",
                "Q5/B/U/1.20/500000",
                "Q7/O/U/1.33/100000",
                "Q7/O/D/0/0"
        };

        final List<Quote> quotes = new ArrayList<>();
        for (String in : input) {
            quotes.add(Quote.parseQuote(in));
        }

        return quotes;
    }
}