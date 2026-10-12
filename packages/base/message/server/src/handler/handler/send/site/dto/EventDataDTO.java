package com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDataDTO {
    private String event;
    private Object data;
}
