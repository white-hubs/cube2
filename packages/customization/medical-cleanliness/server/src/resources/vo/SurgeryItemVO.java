package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * 手术室物品
 */
@Data
public class SurgeryItemVO {
    private Long id;
    private Long deviceId;
    private String deviceCode;
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
    /**
     * 机组id
     */
    private Long machinesId;
    /**
     * 机组编号
     */
    private String machinesCode;

    private Long deptId;
    private String deptName;
    private String arguments;
    private String fileIds;
    private List<FileVO> fileList;
}
