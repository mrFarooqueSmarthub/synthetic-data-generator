package ai.smarthub.infer.synthetic.synthetic_data_generator.service.impl;


import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DataRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DeviceCreateEvent;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.MetricIngestEvent;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.DeviceService;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.KafkaProducerService;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.SyntheticDataService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;


@Service
@Data
@Slf4j
public class SyntheticDataServiceImpl implements SyntheticDataService {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    private final Map<String, ScheduledFuture<?>> activeTasks = new ConcurrentHashMap<>();
    private final KafkaProducerService kafkaProducerService;
    private final DeviceService deviceService;
    private final Random random = new Random();

    @Override
    public void generate(DataRequest request) {
        String deviceId = createDevice(request);
        startGeneratingData("dummy-id", request, deviceId);
//        stopGeneratingData("dummy-id");
    }

    private String createDevice(DataRequest request) {
        log.error("created device with name: {}", request.getGatewayTemplate());

        DeviceCreateEvent deviceCreateEvent = new DeviceCreateEvent();
        deviceCreateEvent.setName("dummy-device");
        deviceCreateEvent.setTemplateName("Default Gateway Template");
        deviceCreateEvent.setOrgId("org-id");
        kafkaProducerService.createDevice(deviceCreateEvent);
        return deviceService.createDevice(deviceCreateEvent);
    }

    private String startGeneratingData(String taskId, DataRequest request, String deviceId) {
        Runnable task = () -> generateAndSendData(request, deviceId);

        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                task, 0, request.getFrequency(), TimeUnit.MILLISECONDS);

        activeTasks.put(taskId, future);
        return "Task started with ID: " + taskId;
    }

    private String stopGeneratingData(String taskId) {
        ScheduledFuture<?> future = activeTasks.remove(taskId);
        if (future != null) {
            future.cancel(true);
            return "Task " + taskId + " stopped successfully.";
        }
        return "Task ID not found.";
    }

    private void generateAndSendData(DataRequest request, String deviceId) {
        int value = getRandomValue(request.getDataRange().getMin(), request.getDataRange().getMax(), request.getStrategy().getAnomalyConfiguration());

        // Create JSON message
        MetricIngestEvent jsonMessage = new MetricIngestEvent();
        jsonMessage.setDeviceId(deviceId);

        log.error("Generated Data: {}", jsonMessage);

        // Send data to Kafka
        kafkaProducerService.sendMetric(jsonMessage);
    }

    private int getRandomValue(int min, int max, String anomalyType) {
        int value = random.nextInt((max - min) + 1) + min;

        if ("Random Spike".equals(anomalyType) && random.nextDouble() < 0.1) {
            return value * 3; // Simulate a spike
        }

        return value;
    }
}
