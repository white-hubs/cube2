package com.guhecloud.cube.biz.dispatch.server.map.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * 高德gps转详细位置DTO
 *
 * @author lanchun
 * @date 2024/5/21 10:57
 */
@Data
public class GaoDeRegeoAddressDTO extends GaoDeBaseResponseDTO {

    private LinkedHashMap<String, LinkedHashMap<String, String>> regeocode;

}
