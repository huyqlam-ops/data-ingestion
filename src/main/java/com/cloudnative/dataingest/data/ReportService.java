package com.cloudnative.dataingest.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    public void saveReportFromEvent(String payload) {
        try {
            ReportEventDto reportEventDto = objectMapper.readValue(payload, ReportEventDto.class);

            reportRepository.save(ReportDocument.builder()
                    .key(reportEventDto.key())
                    .data(reportEventDto.path())
                    .datetimeAdded(OffsetDateTime.now())
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
