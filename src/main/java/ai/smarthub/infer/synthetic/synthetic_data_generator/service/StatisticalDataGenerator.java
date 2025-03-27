package ai.smarthub.infer.synthetic.synthetic_data_generator.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Random;

@Data
@Service
public class StatisticalDataGenerator {
    private int flatLineValue = Integer.MIN_VALUE; // Store flat-line value
    private int flatLineCounter = 0; // Counter to keep Flat Line active
    private int missingDataCounter = 0; // Counter to simulate missing data
    private final Random random = new Random();

    public Integer getRandomValue(int min, int max, String anomalyType, String statisticalDistribution) {
        int value;

        // If Missing Data Mode is active, return null for a few cycles
        if (missingDataCounter > 0) {
            missingDataCounter--;
            return null; // Represents missing data
        }
        // If Flat Line Mode is active, keep returning the same value
        if (flatLineCounter > 0) {
            flatLineCounter--;
            return flatLineValue;
        }

        // Statistical Distribution Selection
       if ("normal-distribution".equals(statisticalDistribution)) {
          double mean = (min + max) / 2.0;
          double stddev = (max - min) / 4.0; // Adjust spread factor (4.0 keeps more values inside range)

          // Generate values until they are within bounds
          do {
             value = (int) (mean + random.nextGaussian() * stddev);
          } while (value < min || value > max);
       } else {
          // Default to Uniform Distribution
          value = random.nextInt((max - min) + 1) + min;
       }

        // Anomaly Injection
        if ("random-spikes".equals(anomalyType) && random.nextDouble() < 0.1) {
            return Math.min(max, value * 3); // Simulate a spike, but clamp to max
        }

        if ("flatline".equals(anomalyType) && random.nextDouble() < 0.1) {
            flatLineValue = (min + max) / 2; // Set constant midpoint value
            flatLineCounter = 20 + random.nextInt(6); // Keep flat for 5 to 10 cycles
            return flatLineValue;
        }

        if ("missing-data-gaps".equals(anomalyType) && random.nextDouble() < 0.05) {
            missingDataCounter = 10 + random.nextInt(5); // Missing for 3-7 cycles
            return null;
        }

        return value;
    }

    public boolean getRandomBoolean(String anomalyType) {
        boolean value = true;

        if ("Random Spike".equals(anomalyType) && random.nextDouble() < 0.1) {
            return !value; // Flip the value to simulate an anomaly
        }

        return value;
    }
}
