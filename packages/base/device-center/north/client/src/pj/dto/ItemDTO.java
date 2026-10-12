package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDTO {
    private String itemLabel;
    private String itemCode;
    private List<ItemDTO> childrenList;
}
