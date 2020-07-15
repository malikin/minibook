package com.github.malikin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class App {

    private final static String CREATE_DUMP = "--dump";

    public static void main(String[] args) {

        if (args.length == 0) {
            throw new IllegalArgumentException("Input file path not set");
        }

        final var quotes = new ArrayList<Quote>();

        try (var br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                quotes.add(Quote.parseQuote(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final var book = new Book();
        book.loadData(quotes);

        System.out.println("OFFER");
        book.getOffersResult().forEach(offer -> System.out.printf("%s/%.2f/%d%n", offer.getId(), offer.getPrice(), offer.getVolume()));

        System.out.println("BID");
        book.getBidsResult().forEach(bid -> System.out.printf("%s/%.2f/%d%n", bid.getId(), bid.getPrice(), bid.getVolume()));

        if (args.length >= 2 && CREATE_DUMP.equals(args[1])) {
            dumpContent(book);
        }
    }

    private static void dumpContent(Book book) {
        try (var writer = new BufferedWriter(new FileWriter("dump_" + Instant.now().getEpochSecond()))) {
            writer.append(String.format("QUOTES%n%n"));
            for (Quote quote : book.getQuotes()) {
                writer.append(quote.toString());
                writer.append(String.format("%n"));
            }
            writer.append(String.format("%nOFFER%n"));
            for (Quote offer : book.getOffersResult()) {
                writer.append(String.format("%s/%.2f/%d%n", offer.getId(), offer.getPrice(), offer.getVolume()));
            }
            writer.append(String.format("%nBID%n"));
            for (Quote bid : book.getBidsResult()) {
                writer.append(String.format("%s/%.2f/%d%n", bid.getId(), bid.getPrice(), bid.getVolume()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
