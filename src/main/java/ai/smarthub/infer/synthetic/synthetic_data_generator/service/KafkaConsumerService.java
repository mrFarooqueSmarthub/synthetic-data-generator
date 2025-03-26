package ai.smarthub.infer.synthetic.synthetic_data_generator.service;

import ai.smarthub.infer.synthetic.synthetic_data_generator.model.MetricIngestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static ai.smarthub.infer.synthetic.synthetic_data_generator.model.Constants.SYNTHETIC_DATA_TOPIC;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private MetricIngestService metricIngestService;

    @KafkaListener(topics = SYNTHETIC_DATA_TOPIC, groupId = "synthetic-data-group")
    public void consume(MetricIngestEvent message) {
        log.error("Received synthetic data: {}", message);
        metricIngestService.ingestMetric(message.getDeviceId());
    }


//    @KafkaListener(topics = CRAETE_DEVICE_TOPIC, groupId = "synthetic-data-group")
//    public void handleDeviceCreateEvent(DeviceCreateEvent deviceCreateEvent) {
//        log.error("Received synthetic data: {}", deviceCreateEvent);
//        String deviceId = deviceService.createDevice(deviceCreateEvent);
//        log.error("device is created with id: {}", deviceId);
//    }
}
