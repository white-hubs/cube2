package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/6
 */
@Data
public class FaultCaseVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 故障案例名称
     */
    private String name;

    /**
     * 故障类型主键
     */
    private Long faultTypeId;

    /**
     * 故障类型名称
     */
    private String faultTypeName;

    /**
     * 设备类型主键
     */
    private Long deviceTypeId;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 故障次数
     */
    private Integer faultCount = 0;

    /**
     * 故障现象
     */
    private String faultPhenomenon;

    /**
     * 故障原因
     */
    private String faultReason;

    /**
     * 处理措施
     */
    private String treatmentMeasures;

    /**
     * 文件信息
     */
    private List<FileVO> files;
    /**
     * 品类code
     */
    private String productCategoryCode;

    /**
     * 品类名称
     */
    private String productCategoryName;

}
