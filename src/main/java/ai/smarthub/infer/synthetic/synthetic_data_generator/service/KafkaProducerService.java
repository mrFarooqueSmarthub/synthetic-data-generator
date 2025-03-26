package ai.smarthub.infer.synthetic.synthetic_data_generator.service;

import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DeviceCreateRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.MetricIngestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static ai.smarthub.infer.synthetic.synthetic_data_generator.model.Constants.SYNTHETIC_DATA_TOPIC;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, DeviceCreateRequest> kafkaDeviceTemplate;
    @Autowired
    private KafkaTemplate<String, MetricIngestEvent> kafkaMetricTemplate;

    public void sendMetric(MetricIngestEvent message) {
        log.error("ingest metric data through kafka: {}", message);
        kafkaMetricTemplate.send(SYNTHETIC_DATA_TOPIC, message);
    }

//    public void createDevice(DeviceCreateRequest deviceCreateEvent) {
//        log.error("send data through kafka: {}", deviceCreateEvent);
//        kafkaDeviceTemplate.send(CRAETE_DEVICE_TOPIC, deviceCreateEvent);
//    }

}
