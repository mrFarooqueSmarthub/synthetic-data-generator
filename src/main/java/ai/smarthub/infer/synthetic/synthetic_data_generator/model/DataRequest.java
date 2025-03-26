package ai.smarthub.infer.synthetic.synthetic_data_generator.model;

import lombok.Data;

import java.util.List;

@Data
public class DataRequest {
    private String gatewayTemplate;
    private List<String> thingTemplate;
    private List<String> metrics;
    private int frequency;
    private Boolean isRealTime;
    private DataRange dataRange;
    private Strategy strategy;
}

