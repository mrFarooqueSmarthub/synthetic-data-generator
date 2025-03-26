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
        metricIngestService.ingestMetric(message);
    }
}
