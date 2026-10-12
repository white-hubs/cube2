package com.guhecloud.cube.srv.gen.safety.management.metric;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MetricResult {
    private final String metric;

    private final String description;

    private final Object result;

    private final boolean successful;
}
