package com.pcb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.IOException;
import java.util.*;

public class BrlExtractor {
    private static Map<String, Map<String, List<double[]>>> librariesData = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String brdFilePath = "./src/main/resources/assets/PCB layout.brd";

        String fileData = new String(Files.readAllBytes(Paths.get(brdFilePath)));

        Document doc = Jsoup.parse(fileData, org.jsoup.parser.Parser.xmlParser());

        Elements libraries = doc.select("libraries").first().select("library");

        for (Element library : libraries) {
            Map<String, List<double[]>> packageData = new HashMap<>();
            librariesData.put(library.attr("name"), packageData);
            System.out.println("-" + library.attr("name"));

            Elements packages = library.select("package");
            for (Element package_ : packages) {
                List<double[]> holes = new ArrayList<>();
                packageData.put(package_.attr("name"), holes);
                System.out.println("---" + package_.attr("name"));

                Elements pads = package_.select("pad");
                for (Element pad : pads) {
                    holes.add(new double[]{Double.parseDouble(pad.attr("x")), Double.parseDouble(pad.attr("y"))});
                    System.out.println("------" + Double.parseDouble(pad.attr("x")) + " " + Double.parseDouble(pad.attr("y")));
                }
            }
        }

    }
}