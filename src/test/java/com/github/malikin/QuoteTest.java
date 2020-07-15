package com.github.malikin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.math.BigDecimal;

public class QuoteTest {

    @Test
    public void testParseQuoteMethod() {
        String input = "Q1/O/N/1.31/1000000";
        Quote quote = Quote.parseQuote(input);

        assertEquals("Q1", quote.getId());
        assertEquals(QuoteType.O, quote.getType());
        assertEquals(QuoteOperation.N, quote.getOperation());
        assertEquals(new BigDecimal("1.31"), quote.getPrice());
        assertEquals(1000000, quote.getVolume());
    }

    @Test
    public void testCompareToMethodByPrice(){
        // Lower price is the best for an offer
        String input1 = "Q1/O/N/1.31/1000000";
        String input2 = "Q2/O/N/1.32/1000000";

        Quote quote1 = Quote.parseQuote(input1);
        Quote quote2 = Quote.parseQuote(input2);
        assertEquals(1, quote1.compareTo(quote2));
        assertEquals(-1, quote2.compareTo(quote1));

        // Higher price is the best for a bit
        String input3 = "Q1/B/N/1.31/1000000";
        String input4 = "Q2/B/N/1.32/1000000";

        Quote quote3 = Quote.parseQuote(input3);
        Quote quote4 = Quote.parseQuote(input4);
        assertEquals(1, quote4.compareTo(quote3));
        assertEquals(-1, quote3.compareTo(quote4));
    }

    @Test
    public void testCompareToMethodByVolume(){
        String input1 = "Q1/O/N/1.31/500000";
        String input2 = "Q2/O/N/1.31/1000000";

        Quote quote1 = Quote.parseQuote(input1);
        Quote quote2 = Quote.parseQuote(input2);
        assertEquals(1, quote2.compareTo(quote1));
        assertEquals(-1, quote1.compareTo(quote2));
    }

    @Test
    public void testCompareToMethodByTime(){
        String input1 = "Q1/O/N/1.31/1000000";
        String input2 = "Q2/O/N/1.31/1000000";

        Quote quote1 = Quote.parseQuote(input1);
        Quote quote2 = Quote.parseQuote(input2);
        assertEquals(1, quote2.compareTo(quote1));
        assertEquals(-1, quote1.compareTo(quote2));
    }
}