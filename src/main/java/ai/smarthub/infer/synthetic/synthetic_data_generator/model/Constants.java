package ai.smarthub.infer.synthetic.synthetic_data_generator.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
   public static final String SYNTHETIC_DATA_TOPIC = "synthetic-data-topic";
   public static final String CRAETE_DEVICE_TOPIC = "create-device-topic";
   public static final String DEVICEMGMT_BASE_URL = "https://mercury.smarthubai.net";
//   public static final String DEVICEMGMT_BASE_URL = "http://localhost:60055";

}
