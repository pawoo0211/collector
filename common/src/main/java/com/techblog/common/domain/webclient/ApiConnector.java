package com.techblog.common.domain.webclient;

import com.techblog.common.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiConnector {

    private final WebClientConfig webClientConfig;

    public <T> T getHttpCall(String url, Class<T> responseDtoClass) {
        T responseEntity = webClientConfig.webClient()
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseDtoClass)
                .block();

        return responseEntity;
    }
}