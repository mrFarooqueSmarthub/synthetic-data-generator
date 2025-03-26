package ai.smarthub.infer.synthetic.synthetic_data_generator.model;

import lombok.Data;

@Data
public class DeviceCreateEvent {
    private String name;
    private String templateName;
    private boolean whitelistStatus;
    private String orgId;
}
