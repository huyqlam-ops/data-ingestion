package com.cloudnative.dataingest.data;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface ReportRepository extends CosmosRepository<ReportDocument, String> {
}
