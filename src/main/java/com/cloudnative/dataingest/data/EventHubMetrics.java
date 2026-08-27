package com.cloudnative.dataingest.data;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EventHubMetrics {

    private final Counter receivedEvents;
    private final MeterRegistry meterRegistry;

    private final ConcurrentMap<String, AtomicLong> lag = new ConcurrentHashMap<>();

    public EventHubMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.receivedEvents = Counter.builder("eventhub_received_events")
                .description("Number of events received from Event Hub")
                .register(meterRegistry);
    }

    public void receivedEvent() {
        receivedEvents.increment();
    }

    public void updateLag(String eventHub, String partition, long lagValue) {
        String key = eventHub + ":" + partition;

        lag.computeIfAbsent(key, k ->
                meterRegistry.gauge("eventhub_consumer_lag_events",
                        Tags.of(
                                "event_hub", eventHub,
                                "partition", partition
                        ),
                        new AtomicLong()
                )
        ).set(lagValue);
    }
}
