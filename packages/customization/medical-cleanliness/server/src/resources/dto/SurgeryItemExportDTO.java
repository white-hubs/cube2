package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class SurgeryItemExportDTO {
    private String deviceName;
    /**
     * 名称
     */
    private String name;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 数量
     */
    private String number;
    /**
     * 厂家
     */
    private String manufacturer;
    /**
     * 型号
     */
    private String model;

    private String deptName;
    /**
     * 机组编号
     */
    private String machinesCode;
    private String arguments;
}
