package ai.smarthub.infer.synthetic.synthetic_data_generator.service;

import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DeviceCreateRequest;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.DeviceCreateResponse;
import ai.smarthub.infer.synthetic.synthetic_data_generator.model.ThingTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class DeviceService {

    private final WebClient webClient;
    private final String token = "Bearer eyJraWQiOiIzM2RjZjgyYy0wNTljLTRkYTgtOGQ3Yi1iZmVkMGY3M2JmN2UiLCJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNqEVm1v2zYQ_i_-fC7II0VJ_TTFZhwhtmRIdIJ2KAzFUdGsbRLE2bBi2H8fT6LeleyLoHt4z8O70x3FfxZ_vD4sPi64uCu5UnLJUIVLKUO5DFHhMlBBWfCvJ9-TxQIWD-ezdS5UERb-6W55XwrrfLoXy6Lw75d3igXqrpAFvxPW-fznnXX-Vpy-F6_fnh5_K54fPjw8fi1fPvx4OhU_rEf59_PiI_c95iuhJLf6xSsBEsPAr4C_ypfzw9MjRUiKp6fnkgI4ncrz2Tx9Lx-roPJf59fy5-Fcviw-fi1-nEtY_GmN-J58fS48xcSyLD1cSi_0lyE7ecsT8tA_nVRwqjJ7_VUrPz9cl7-s_Vy-_LTZ2r1txr9zQBAgwQMFPgQQAmfAOXAELoBL4B5wBdwHHgAPARmg5SCgAJSAHqAC9AEDwBAEA8FBWEkBQoLwQCgQPogARAiSgc1cIki7owTpgVQgfZAByBA8Bh4HD8ET4NmAPPAUeD54AXghKAaKg0JQApQEZeNVoHxQAagQfAY-Bx_BF-BL8D0IOAQIgYTAvisIbG4BBCGEDEIOIUIoIJQQ2uyYTZXZXBnj9CCrNiU9yIEpevj0COhBRaIyMSoUo1IxKhajcjEqGKOSMSoao7IxTgwkBhIDiYHEQGJgFQQxkBhIDCSGIIYghqhiJIYghiCGIIYghiCGIIYkhiSGLTNSRkgZIWWElBFSRkgZIWWElBFSRkgZIWWElBFSRkgZIWWElBFSRkgZIataAdmXfj8lxc-SemqxynRk9DFPL81tlOmj0bv91iK2-_Q6NrMLN7G-nV1Y661-Q2x_yDbzK6MIxhuP95tuM1Hvie6j1UxOY7TeYIw224xxt9kY7m852GmwwUh3KGetTOskS7fb41rfxKuebrTVmTlmep9mpvKL1mPMea6i3T6KN8nIeQrXIX7KbRJOaq0v4yQ2cZp0cnUgx02WHvYNaYRV6Y0wl-Wcp4ny6-Y9TvJ4c2XyRrln10XTFjDxTau0z9K9DTXWLeV9l0olynNtjlu93uisoc1hTiA3g08a7ePjtf7UEluz1m7N_DY2qysL7PNe-aL86iKNsnVbpx5Qb9oDmqr1oLHO8Ta2IZuJXIcPVTt8LN6tNL2rV4csNp-ogpfxtuvcKb6NczPB8153TymRMTo3k8Ze0ae6jFe9AR1CTnEIZvomvR6DVbBDaNjEw4kYg7YWDbZKd7soofLb7lpP0X63dGh_DjrUhbDTuwud5VdxOwcDpFIcIC7vKS_NNlESf45oTm2hjYmTTTsNby0e9uuqlzc66X_xevqT1JWMSHYETZT0zq33ffqnyFs-TVO87-ViyqLbY7_7erbT6SHRhf127dnWNcYh19nR2LekOmIvbL9eDQ8G-3VsMY6fV3nXkvVA01HYDrsz3M6N2SjutMniVd590YEZHUhim27y6enctmtn173f2U3jd4g9WrL0ZgDZ6g0LkJt0P7BXV3p92A5ILhb7z7m2NWhC6cwqks50gXSA49tvYGyZB3-NSmluodKcW3Dqc0vD6bXW2nZvHG0nS82Xduigw9b21ImTqB_fO-tVmO-su2jf8ZiPYVyit5fnIpiW622Huf3HM_0_IzhzG-hGYybaGbw_MLPxNaNUz3i6bfvOvTezXluONLhHDK4ko3tHuza6TR2SMdI_v6Y3h_HfakhrrcFtpx3lg53FkeiobUc30ync_6VM76bTBaff_wt07iN08jOZ-4d0mnSkNiz3Xnm7d7eHs5qznL7el3__AwAA__8.iVbRoMdyjQzV_YEZrdL5vZDqC0I9VCrvd9_difMTYdIP_NKMGlEHi9iaqvlnmZP9_A9Gc2m6p6YzQm2LmKO4hQ";
    private final String orgId = "a6a9a7cb-de39-4cd3-aa7d-b6086ba4a1b3";

    public String createGatewayDevice(DeviceCreateRequest deviceRequest) {
        // register device
        String deviceId = registerDevice(deviceRequest);
        // enroll device
        enrollDevice(deviceId);
        return deviceId;
    }

    public void createThingDevice(DeviceCreateRequest deviceRequest) {
        log.error("register device starts");
        Mono<DeviceCreateResponse> authorization = webClient.post()
                .uri("/api/devices")
                .headers(headers -> {
                    headers.set("Authorization", token);
                    headers.set("x-current-org-id", orgId);
                })
                .bodyValue(deviceRequest)
                .retrieve()
                .bodyToMono(DeviceCreateResponse.class)
                .doOnSuccess(res -> log.error("response for the device: {}", res))
                .doOnError(error -> log.error("error while retrieving: {}", error.getMessage()));
    }

    private void enrollDevice(String deviceId) {
        log.error("enroll device starts");
        webClient.get()
                .uri("/api/devices/{id}/enroll", deviceId)
                .headers(headers -> {
                    headers.set("Authorization", token);
                    headers.set("x-current-org-id", orgId);
                })
                .retrieve()
                .bodyToMono(DeviceCreateResponse.class)
                .doOnSuccess(res -> log.error("response for the device: {}", res))
                .doOnError(error -> log.error("error while retrieving: {}", error.getMessage()))
                .subscribe();
    }

    private String registerDevice(DeviceCreateRequest deviceRequest) {
        log.error("register device starts");
        DeviceCreateResponse response = webClient.post()
                .uri("/api/devices")
                .headers(headers -> {
                    headers.set("Authorization", token);
                    headers.set("x-current-org-id", orgId);
                })
                .bodyValue(deviceRequest)
                .retrieve()
                .bodyToMono(DeviceCreateResponse.class)
                .doOnSuccess(res -> log.error("response for the device: {}", res))
                .doOnError(error -> log.error("error while retrieving: {}", error.getMessage()))
                .block();
        if (response == null) {
            throw new RuntimeException("Device Registration Failure");
        }
        return response.getId();
    }
}
