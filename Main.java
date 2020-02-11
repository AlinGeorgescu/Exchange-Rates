import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (C) Copyright 2020
 */
public class Main {

    public static void main(String[] args) {

        RateDownloader downloader = new RateDownloader();
        downloader.download();

        RateParser parser = new RateParser();
        List<Triplet<String, Integer, Double>> currencies = parser.parse();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        System.out.println("Cursul pentru RON din data de " + dateFormat.format(date) + " este:");
        System.out.println("=========================================");
        System.out.println("|| Multiplicator || Valuta ||  Valoare ||");

        String separator = "||";
        for (Triplet<String, Integer, Double> currency : currencies) {
            System.out.format("%s %8d %7s %5s %3s %8.4f %s\n",
                    separator, currency.t2, separator, currency.t1,
                    separator, currency.t3, separator);
        }
        System.out.println("=========================================");
    }
}
