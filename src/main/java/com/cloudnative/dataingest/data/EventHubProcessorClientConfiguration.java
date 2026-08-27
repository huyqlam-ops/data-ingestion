package com.cloudnative.dataingest.data;

import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.implementation.instrumentation.EventHubsMetricsProvider;
import com.azure.messaging.eventhubs.models.LastEnqueuedEventProperties;
import com.azure.spring.cloud.service.eventhubs.consumer.EventHubsErrorHandler;
import com.azure.spring.cloud.service.eventhubs.consumer.EventHubsRecordMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EventHubProcessorClientConfiguration {

    @Bean
    EventHubsRecordMessageListener processEvent(ReportService reportService, EventHubMetrics eventHubMetrics) {
        return eventContext-> {
            String body = eventContext.getEventData().getBodyAsString();
            String partitionId = eventContext.getPartitionContext().getPartitionId();
            String eventHubName = eventContext.getPartitionContext().getEventHubName();

            log.info("Received event from partition {} with body: {}", partitionId, body);

            eventHubMetrics.receivedEvent();

            LastEnqueuedEventProperties lastEnqueued = eventContext.getLastEnqueuedEventProperties();
            if (lastEnqueued != null && lastEnqueued.getSequenceNumber() != null) {
                long lag = lastEnqueued.getSequenceNumber() - eventContext.getEventData().getSequenceNumber();
                eventHubMetrics.updateLag(eventHubName, partitionId, lag);
            }

            reportService.saveReportFromEvent(body);
            eventContext.updateCheckpoint();
        };
    }

    @Bean
    EventHubsErrorHandler processError() {
        return errorContext->log.info("Error occurred in partition processor for partition {},{}",
                errorContext.getPartitionContext().getPartitionId(),
                errorContext.getThrowable());
    }

    @Bean
    CommandLineRunner startEventProcessor(EventProcessorClient eventProcessorClient) {
        return args -> eventProcessorClient.start();
    }

}