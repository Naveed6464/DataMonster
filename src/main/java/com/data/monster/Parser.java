/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.monster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.primitives.Doubles;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author naveed
 */
public class Parser {

    private static final ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    public static void main(String[] args) throws IOException {

        String filePath = "/home/naveed/Downloads/ProductInformationExtraction/data/webpages";
        File dir = new File(filePath);
        Document doc;
        for (File child : dir.listFiles()) {            
            doc = Jsoup.parse(child, "UTF-8");
            Elements media = doc.select("div > img[src]");
            List<Product> products = new ArrayList<>();
            HashMap<Double, Element> images = new HashMap<>();
            List<Double> jacards = new ArrayList<>();
            for (Element img : media) {
                if (!img.attr("alt").isEmpty()) {                    
                    double jaccardSimilarity = jaccardSimilarity(doc.title(), img.attr("alt"));
                    images.put(jaccardSimilarity, img);
                    jacards.add(jaccardSimilarity);                    
                }
            }
            //Double reduce = jacards.stream().reduce(Double.MIN_VALUE, Double::max);
            if (!jacards.isEmpty()) {
                double max = Doubles.max(Doubles.toArray(jacards));
                Element get = images.get(max);                
                if (get != null) {
                    Product product = new Product(get.attr("alt"), "", "");
                    Elements parent = get.parent().parent().parent().children();
                    for (Element el : parent) {
                        Elements elementsContainingOwnText = el.getElementsContainingOwnText("$");
                        if (!elementsContainingOwnText.text().isEmpty()) {
                            String text = elementsContainingOwnText.text();                            
                            String price = text.substring(text.indexOf("$"));                            
                            int endIndex;
                            if (price.indexOf(".") + 3 > price.length()) {
                                endIndex = price.length();
                            } else {
                                endIndex = price.indexOf(".") + 3;
                            }
                            product.setOfferPrize(price.substring(0, endIndex));
                        }
                    }
                    products.add(product);
                }
            }
            ParserJson parserJson = new ParserJson(products);
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            System.out.println("" + mapper.writeValueAsString(parserJson));
            //mapper.writeValue(new File("/home/naveed/Test/" + child.getName().replace(".html", ".json")), parserJson);
        }

        //Pattern pattern = Pattern.compile("^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$");
        //Matcher matcher = pattern.matcher("sdfsdf $4,098.09");
        //System.out.println("Input String matches regex - " + matcher.matches());
    }

    public static double jaccardSimilarity(String similar1, String similar2) {
        HashSet<String> h1 = new HashSet<>();
        HashSet<String> h2 = new HashSet<>();

        for (String s : similar1.split("\\s+")) {
            h1.add(s);
        }
        //System.out.println("h1 " + h1);
        for (String s : similar2.split("\\s+")) {
            h2.add(s);
        }
        //System.out.println("h2 " + h2);

        int sizeh1 = h1.size();
        //Retains all elements in h3 that are contained in h2 ie intersection
        h1.retainAll(h2);
        //h1 now contains the intersection of h1 and h2
        //System.out.println("Intersection " + h1);

        h2.removeAll(h1);
        //h2 now contains unique elements
        //System.out.println("Unique in h2 " + h2);

        //Union 
        int union = sizeh1 + h2.size();
        int intersection = h1.size();

        return (double) intersection / union;

    }

}
