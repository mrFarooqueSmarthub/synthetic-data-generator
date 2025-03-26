package ai.smarthub.infer.synthetic.synthetic_data_generator.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ai.smarthub.infer.synthetic.synthetic_data_generator.model.Constants.CRAETE_DEVICE_TOPIC;
import static ai.smarthub.infer.synthetic.synthetic_data_generator.model.Constants.SYNTHETIC_DATA_TOPIC;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic syntheticDataTopic() {
        return new NewTopic(SYNTHETIC_DATA_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic createDeviceTopic() {
        return new NewTopic(CRAETE_DEVICE_TOPIC, 3, (short) 1);
    }
}
