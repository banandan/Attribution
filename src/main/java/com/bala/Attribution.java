package main.java.com.bala;

import java.util.*;

import static main.java.com.bala.IOUtils.writeOutput;

public class Attribution {
    public static void main(String[] args) throws Exception {
        Map<String, List<Exposure>> exposures = IOUtils.readExposure();
        List<Sale> sales = IOUtils.readSales();

        Map<String, Set<String>> purchasers = new HashMap<>();
        Map<String, Double> totalSales = new HashMap<>();
        for (final Sale sale : sales) {
            if (!exposures.containsKey(sale.userId)) {
                continue;
            }
            Exposure lastExposure = null;
            Exposure clickedExposure = null;
            for (Exposure exposure : exposures.get(sale.userId)) {
                if ((sale.timestamp - exposure.timestamp) > 7*1000*3600*24){
                    continue;
                }
                if (exposure.timestamp > sale.timestamp) {
                    break;
                }
                if(exposure.hasClicked){
                    clickedExposure = exposure;
                }
                lastExposure = exposure;
            }
            lastExposure = clickedExposure != null? clickedExposure: lastExposure;
            if (lastExposure != null) {
                if (!purchasers.containsKey(lastExposure.creativeId)) {
                    purchasers.put(lastExposure.creativeId, new HashSet<>());
                    totalSales.put(lastExposure.creativeId, 0.0);
                }
                purchasers.get(lastExposure.creativeId).add(lastExposure.userId);
                double sum = totalSales.get(lastExposure.creativeId) + sale.amount;
                totalSales.put(lastExposure.creativeId, sum);
            }
        }
        writeOutput(purchasers, totalSales);
    }
}
