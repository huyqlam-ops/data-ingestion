package com.cloudnative.dataingest.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    public void saveReportFromEvent(String payload) {
        try {
            ReportEventDto reportEventDto = objectMapper.readValue(payload, ReportEventDto.class);
            log.info("Saving report with key: {} and path: {}", reportEventDto.key(), reportEventDto.path());
            reportRepository.save(ReportDocument.builder()
                    .key(reportEventDto.key())
                    .data(reportEventDto.path())
                    .datetimeAdded(OffsetDateTime.now())
                    .build());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
