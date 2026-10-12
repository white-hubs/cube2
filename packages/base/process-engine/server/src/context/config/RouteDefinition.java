package com.guhecloud.cube.base.flow.context.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDefinition {
    private String id;
    private String path;

    private String serviceId;
}
