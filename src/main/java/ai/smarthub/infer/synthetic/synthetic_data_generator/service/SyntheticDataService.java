package ai.smarthub.infer.synthetic.synthetic_data_generator.service;

import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DataRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.ThingTemplate;
import reactor.core.publisher.Mono;

public interface SyntheticDataService {
    void startGeneratingData(DataRequest request, String deviceId);

    Mono<Void> startGeneratingDataForThing(DataRequest request, ThingTemplate thingTemplate, String deviceId);
}
