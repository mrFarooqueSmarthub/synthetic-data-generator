package ai.smarthub.infer.synthetic.synthetic_data_generator.model;

import lombok.Data;

@Data
public class DeviceCreateRequest {
    private String name;
    private String templateName;
    private boolean whitelistStatus;
    private String parentId;
}
