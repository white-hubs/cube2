package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskMeasureExecutionRecordVO {
    /**
     * 执行反馈记录 ID。
     */
    private Long id;

    /**
     * 管控措施 ID。
     */
    private Long measureId;

    /**
     * 执行结果。
     */
    private String executionResult;

    /**
     * 执行说明。
     */
    private String executionDesc;

    /**
     * 执行人用户 ID。
     */
    private String executorUserId;

    /**
     * 执行人引用对象，由 executorUserId 解析得到。
     */
    private RiskUserRefVO executorUser;

    /**
     * 执行时间。
     */
    private LocalDateTime executionTime;

    /**
     * 执行 GPS 位置 JSON。
     */
    private String gpsLocation;

    /**
     * 附件文件 ID 集合。
     */
    private List<String> fileIds;

    /**
     * 附件文件对象集合，由 fileIds 调用文件服务解析得到。
     */
    private List<FileVO> files;
}
