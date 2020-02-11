import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * (C) Copyright 2020
 */
public class RateParser {

    List<Triplet<String, Integer, Double>> parse() {
        List<Triplet<String, Integer, Double>> currenciesList = new ArrayList<>();

        try {
            File file = new File("tmp_rates.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Rate");

            for (int i = 0; i < nList.getLength(); ++i) {
                Element element = (Element) nList.item(i);
                String multiplier = element.getAttribute("multiplier");
                if (multiplier.equals("")) {
                    multiplier = "1";
                }

                currenciesList.add(new Triplet<>(element.getAttribute("currency"),
                                                 Integer.valueOf(multiplier),
                                                 Double.parseDouble(element.getTextContent())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File("tmp_rates.xml");
        if(file.delete()) {
            System.out.println("File deleted successfully!");
        } else {
            System.out.println("Failed to delete the file!");
        }

        return currenciesList;
    }
}
