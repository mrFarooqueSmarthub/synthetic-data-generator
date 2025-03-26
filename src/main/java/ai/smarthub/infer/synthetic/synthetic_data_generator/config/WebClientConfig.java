package ai.smarthub.infer.synthetic.synthetic_data_generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static ai.smarthub.infer.synthetic.synthetic_data_generator.model.Constants.DEVICEMGMT_BASE_URL;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(DEVICEMGMT_BASE_URL)
                .defaultHeader("Accept", "application/json;api-version=0.17")
                .build();
    }
}
