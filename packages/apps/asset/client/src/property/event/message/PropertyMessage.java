package com.guhecloud.cube.property.management.client.property.event.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyMessage {
    public static final String TOPIC = "PROPERTY_DEPOSITORY";
    private Long id;
    private String status;
}
