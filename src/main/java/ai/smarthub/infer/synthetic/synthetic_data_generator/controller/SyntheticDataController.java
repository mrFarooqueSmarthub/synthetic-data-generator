package ai.smarthub.infer.synthetic.synthetic_data_generator.controller;


import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DataRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.service.DeviceService;
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
    private DeviceService deviceService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateData(@RequestBody DataRequest request) {
        // Call service to generate synthetic data
        deviceService.createGatewayDevice(request);
        return ResponseEntity.ok("Synthetic data generation started");
    }
}