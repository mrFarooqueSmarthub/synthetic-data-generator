package ai.smarthub.infer.synthetic.synthetic_data_generator.service;

import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DataRequest;

public interface SyntheticDataService {
    void generate(DataRequest request);
}
