package ai.smarthub.infer.synthetic.synthetic_data_generator.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataRequest {
    private String gatewayTemplate;
    private List<ThingTemplate> thingTemplate = new ArrayList<>();
    private List<String> metrics;
    private int frequency;
    private Boolean isRealTime;
    private DataRange dataRange;
    private Strategy strategy;
}

