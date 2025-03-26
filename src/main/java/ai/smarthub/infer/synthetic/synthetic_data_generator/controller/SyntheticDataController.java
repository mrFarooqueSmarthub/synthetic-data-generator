package ai.smarthub.infer.synthetic.synthetic_data_generator.controller;


import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DataRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.SyntheticDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/synthetic-data")
@AllArgsConstructor
public class SyntheticDataController {

    private SyntheticDataService syntheticDataService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateData(@RequestBody DataRequest request) {
        // Call service to generate synthetic data
        syntheticDataService.generate(request);
        return ResponseEntity.ok("Synthetic data generation started");
    }
}