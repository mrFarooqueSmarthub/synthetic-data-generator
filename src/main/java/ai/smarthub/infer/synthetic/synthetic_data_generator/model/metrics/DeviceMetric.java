package ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics;

import lombok.Data;

import java.util.List;

@Data
public class DeviceMetric {
    private String deviceId;
    private List<MetricDetail> metricDetails;
}
