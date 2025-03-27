package ai.smarthub.infer.synthetic.synthetic_data_generator.model;

import lombok.Data;

import java.util.List;

@Data
public class ThingTemplate {
    private String name;
    private String deviceName;
    private List<String> metrics;
    private int deviceCount;
}
