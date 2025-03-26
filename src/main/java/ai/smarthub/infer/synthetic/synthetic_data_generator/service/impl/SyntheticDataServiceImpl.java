package ai.smarthub.infer.synthetic.synthetic_data_generator.service.impl;


import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DataRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DeviceCreateRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.MetricIngestEvent;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics.DataPoint;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics.DeviceMetric;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics.MetricDetail;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.DeviceService;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.KafkaProducerService;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.SyntheticDataService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        String parentGatewayId = createParentGatewayDevice(request);
        createThingDevice(request, parentGatewayId);
        startGeneratingData(parentGatewayId, request, parentGatewayId);
//        stopGeneratingData("dummy-id");
    }

    private void createThingDevice(DataRequest request, String parentId) {
        request.getThingTemplate().forEach(thingTemplate -> {
            DeviceCreateRequest deviceCreateRequest = new DeviceCreateRequest();
            deviceCreateRequest.setName(generateRandomDeviceName());
            deviceCreateRequest.setParentId(parentId);
            deviceCreateRequest.setTemplateName(thingTemplate.getName());
            deviceService.createThingDevice(deviceCreateRequest);
        });
    }

    private String createParentGatewayDevice(DataRequest request) {
        log.error("created device with name: {}", request.getGatewayTemplate());
        DeviceCreateRequest deviceCreateRequest = new DeviceCreateRequest();
        deviceCreateRequest.setName(generateRandomDeviceName());
        deviceCreateRequest.setTemplateName(request.getGatewayTemplate());
        return deviceService.createGatewayDevice(deviceCreateRequest);
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
        request.getMetrics().forEach(metricName -> {
            // check if boolean or not
            Object value;
            if (isBooleanMetricType(metricName)) {
                value = getRandomBoolean(request.getStrategy().getAnomalyConfiguration());
            } else {
                value = getRandomValue(request.getDataRange().getMin(), request.getDataRange().getMax(), request.getStrategy().getAnomalyConfiguration());
            }
            MetricIngestEvent metricIngestEvent = createMetricRequestPayload(deviceId, value, metricName);
            // Send data to Kafka
            kafkaProducerService.sendMetric(metricIngestEvent);
        });
    }

    private boolean isBooleanMetricType(String metricName) {
        return metricName.endsWith("(BOOLEAN)");
    }

    private MetricIngestEvent createMetricRequestPayload(String deviceId, Object value, String metricName) {
        MetricIngestEvent metricsRequest = new MetricIngestEvent();
        List<DeviceMetric> devices = new ArrayList<>();
        DeviceMetric deviceMetric = new DeviceMetric();
        deviceMetric.setDeviceId(deviceId);
        List<MetricDetail> metricDetails = new ArrayList<>();
        MetricDetail metricDetail = new MetricDetail();
        metricDetail.setMetricName(metricName);
        List<DataPoint> dataPoints = new ArrayList<>();
        DataPoint dataPoint = new DataPoint();
        dataPoint.setValue(value);
        dataPoint.setTimeMs(System.currentTimeMillis());
        dataPoints.add(dataPoint);
        metricDetail.setDataPoints(dataPoints);
        metricDetails.add(metricDetail);
        deviceMetric.setMetricDetails(metricDetails);
        devices.add(deviceMetric);
        metricsRequest.setDevices(devices);
        return metricsRequest;
    }

    private int getRandomValue(int min, int max, String anomalyType) {
        int value = random.nextInt((max - min) + 1) + min;

        if ("Random Spike".equals(anomalyType) && random.nextDouble() < 0.1) {
            return value * 3; // Simulate a spike
        }

        return value;
    }

    private boolean getRandomBoolean(String anomalyType) {
        boolean value = true;

        if ("Random Spike".equals(anomalyType) && random.nextDouble() < 0.1) {
            return !value; // Flip the value to simulate an anomaly
        }

        return value;
    }


    // Generate a random device name without numbers
    private String generateRandomDeviceName() {
        String[] adjectives = {"Smart", "Fast", "Secure", "Advanced", "Reliable", "Efficient", "NextGen"};
        String[] nouns = {"Sensor", "Gateway", "Node", "Hub", "Module", "Unit", "Device"};

        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];

        return adjective + "-" + noun;
    }
}
