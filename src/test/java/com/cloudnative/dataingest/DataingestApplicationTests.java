package com.cloudnative.dataingest;

import com.azure.messaging.eventhubs.EventProcessorClient;
import com.cloudnative.dataingest.data.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class DataingestApplicationTests {

	@MockitoBean
	private ReportRepository reportRepository;

	@MockitoBean
	private EventProcessorClient eventProcessorClient;

	@Test
	void contextLoads() {
	}

}
