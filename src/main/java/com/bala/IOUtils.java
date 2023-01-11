package main.java.com.bala;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class IOUtils {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String EXPOSURES_FILE = "ad_exposures1.csv";
    private static final String SALES_FILE = "sales_data1.csv";


    private static CSVReader getCsvReader(final String fileStr) throws FileNotFoundException {
        ClassLoader classLoader = IOUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(fileStr).getFile());
        FileReader filereader = new FileReader(file);
        return new CSVReader(filereader);
    }

    public static Map<String, List<Exposure>> readExposure() {
        Map<String, List<Exposure>> exposures = new HashMap<>();
        try {
            CSVReader csvReader = getCsvReader(EXPOSURES_FILE);
            String[] values;
            int line = 0;
            while ((values = csvReader.readNext()) != null) {
                line++;
                if (line == 1 || values.length == 0) continue;
                if (!exposures.containsKey(values[0])) {
                    exposures.put(values[0], new ArrayList<>());
                }
                exposures.get(values[0]).add(new Exposure(values[0], dateFormatter.parse(values[1]).getTime(), values[2]));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, List<Exposure>> entry : exposures.entrySet()) {
            List<Exposure> sortedExposures = entry.getValue();
            Collections.sort(sortedExposures);
            exposures.put(entry.getKey(), sortedExposures);
        }
        return exposures;
    }

    public static List<Sale> readSales() {
        List<Sale> sales = new ArrayList<>();
        try {
            CSVReader csvReader = getCsvReader(SALES_FILE);
            String[] values;
            int line = 0;
            while ((values = csvReader.readNext()) != null) {
                line++;
                if (line == 1 || values.length == 0) continue;
                sales.add(new Sale(values[0], dateFormatter.parse(values[1]).getTime(), Double.valueOf(values[2])));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Collections.sort(sales);
        return sales;
    }

    public static void writeOutput(Map<String, Set<String>> purchasers, Map<String, Double> totalSales) throws Exception {
        final Set<String> allPurchasers = purchasers.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        final double totalSum = totalSales.values().stream().reduce(0.0, Double::sum).doubleValue();

        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{"dimension", "value", "num_purchasers", "total_sales"});
        lines.add(new String[]{"overall", "overall", String.valueOf(allPurchasers.size()), String.valueOf(totalSum)});
        for (Map.Entry<String, Set<String>> entry : purchasers.entrySet()) {
            lines.add(new String[]{"creative_id", entry.getKey(), String.valueOf(entry.getValue().size()), String.valueOf(totalSales.get(entry.getKey()))});
        }

        File file = new File("/Users/bala/workspace/src/main/resources/output.csv");
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            for (String[] line : lines) {
                writer.writeNext(line);
            }
        }
    }
}
