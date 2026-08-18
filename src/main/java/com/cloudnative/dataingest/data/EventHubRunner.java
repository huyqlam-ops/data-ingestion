package com.cloudnative.dataingest.data;

import com.azure.messaging.eventhubs.EventProcessorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHubRunner implements CommandLineRunner {

    private final EventProcessorClient processorClient;

    @Override
    public void run(String... args) throws Exception {
        log.info("Bắt đầu khởi chạy EventProcessorClient...");
        processorClient.start();

    }
}
