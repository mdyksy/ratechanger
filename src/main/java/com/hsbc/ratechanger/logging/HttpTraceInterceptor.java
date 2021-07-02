package com.hsbc.ratechanger.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HttpTraceInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest req, byte[] reqBody, ClientHttpRequestExecution ex) throws IOException {
        logRequest(req, reqBody);

        final ClientHttpResponse response = ex.execute(req, reqBody);
        logResponse(response);

        return response;
    }

    private void logRequest(HttpRequest req, byte[] body) {
        if (body.length > 0) {
            log.info("Request URL: {} Method: {} Body: {}",
                    req.getURI().getPath(),
                    req.getMethodValue(),
                    new String(body, StandardCharsets.UTF_8));
        } else {
            log.info("Request URL: {} Method: {} Body: Empty",
                    req.getURI().getPath(),
                    req.getMethodValue());
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        final InputStreamReader inputStreamReader = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
        final String body = new BufferedReader(inputStreamReader).lines()
                .collect(Collectors.joining("\n"));

        log.info("Response Status: {} Body: {}", response.getStatusText(), body);
    }
}
