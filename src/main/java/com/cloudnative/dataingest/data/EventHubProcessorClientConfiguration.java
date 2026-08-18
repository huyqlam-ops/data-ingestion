package com.cloudnative.dataingest.data;

import com.azure.spring.cloud.service.eventhubs.consumer.EventHubsErrorHandler;
import com.azure.spring.cloud.service.eventhubs.consumer.EventHubsRecordMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EventHubProcessorClientConfiguration {

    // Bean xử lý khi nhận được tin nhắn mới từ Event Hub
    @Bean
    public EventHubsRecordMessageListener processEvent(ReportService reportService) {
        return eventContext -> {
            log.info("Đã nhận event từ Partition {}: {}",
                    eventContext.getPartitionContext().getPartitionId(),
                    eventContext.getEventData().getBodyAsString());

            reportService.saveReportFromEvent(eventContext.getEventData().getBodyAsString());
            // Đánh dấu đã xử lý (checkpoint)
            eventContext.updateCheckpoint();
        };
    }

    // Bean xử lý khi gặp lỗi trong quá trình lắng nghe
    @Bean
    public EventHubsErrorHandler processError() {
        return errorContext -> log.error("Lỗi tại Partition {}: {}",
                errorContext.getPartitionContext().getPartitionId(),
                errorContext.getThrowable().getMessage());
    }
}
