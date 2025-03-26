package ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics;

import lombok.Data;

import java.util.List;

@Data
public class MetricDetail {
    private String metricName;
    private List<DataPoint> dataPoints;
}
