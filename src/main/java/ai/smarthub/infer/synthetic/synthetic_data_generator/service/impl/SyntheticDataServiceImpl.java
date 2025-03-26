package ai.smarthub.infer.synthetic.synthetic_data_generator.service.impl;


import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DataRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.MetricIngestEvent;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.ThingTemplate;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics.DataPoint;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics.DeviceMetric;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.metrics.MetricDetail;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.DeviceService;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.KafkaProducerService;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.SyntheticDataService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    private final Random random = new Random();

    @Override
    public void startGeneratingData(DataRequest request, String deviceId) {
        Runnable task = () -> generateAndSendData(request, request.getMetrics(), deviceId);

        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                task, 0, request.getFrequency(), TimeUnit.MILLISECONDS);

        activeTasks.put(deviceId, future);
    }

    @Override
    public Mono<Void> startGeneratingDataForThing(DataRequest request, ThingTemplate thingTemplate, String deviceId) {
        Runnable task = () -> generateAndSendData(request, thingTemplate.getMetrics(), deviceId);

        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                task, 0, request.getFrequency(), TimeUnit.MILLISECONDS);

        activeTasks.put(deviceId, future);
        return Mono.empty();
    }

    private String stopGeneratingData(String taskId) {
        ScheduledFuture<?> future = activeTasks.remove(taskId);
        if (future != null) {
            future.cancel(true);
            return "Task " + taskId + " stopped successfully.";
        }
        return "Task ID not found.";
    }

    public void generateAndSendData(DataRequest request, List<String> metrics, String deviceId) {
        log.error("generate metric for device id: {} with metrics: {}", deviceId, metrics);
        metrics.forEach(metricName -> {
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
}
