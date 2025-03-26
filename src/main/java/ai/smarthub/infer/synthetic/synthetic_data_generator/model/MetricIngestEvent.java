package ai.smarthub.infer.synthetic.synthetic_data_generator.model;

import ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics.DeviceMetric;
import lombok.Data;

import java.util.List;

@Data
public class MetricIngestEvent {
    private List<DeviceMetric> devices;
}
