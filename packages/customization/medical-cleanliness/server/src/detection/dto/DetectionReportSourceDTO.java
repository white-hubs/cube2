package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DetectionReportSourceDTO {
    private List<Map<String,String>> header;
    private List<Map<String,String>> data;

}
