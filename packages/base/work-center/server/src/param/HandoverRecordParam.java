package com.guhecloud.cube.biz.work.center.server.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 16:15
 * @Description:
 */

@Data
public class HandoverRecordParam {

    /**
     * 交班任务集合
     */
    private List<Long> handoverTaskList;

    /**
     * 接班人
     */
    @NotNull(message = "接班人不能为空")
    private Long takeOverUser;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 附件
     */
    private List<Long> fileIds;

    /**
     * 交班人签名文件ID
     */
    private Long shiftSignFileId;

    /**
     * 交班扩展信息
     */
    private HandoverRecordExtendParam extend;

}
