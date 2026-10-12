package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mioopp
 * @create 2020-12-14 13:57
 */
@Setter
@Getter
public class PropertyTsDTO {

    private String propertyCode;

    private String propertyName;

    private String dataType;

    private String propertyUnit;

    private String propertyConfig;

    private List<KeyValueWithTsDTO> valueTsBoList;
}
