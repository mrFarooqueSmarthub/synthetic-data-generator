package ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics;

import lombok.Data;

@Data
public class DataPoint {
    private long timeMs;
    private double value;
}
