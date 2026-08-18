package com.cloudnative.dataingest.data;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Container(containerName = "reports")
public class ReportDocument {

    @Id
    private String id;

    @PartitionKey
    private String key;

    private String data;

    private OffsetDateTime datetimeAdded;
}
