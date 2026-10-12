package com.guhecloud.cube.biz.work.center.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/30 17:35
 * @Description:
 */
@Data
public class HandoverRecordDetailVO {
    private Long id;

    /**
     * 交班人
     */
    private Long shiftOverUser;

    /**
     * 交班人
     */
    private String shiftOverUserName;

    /**
     * 交班任务
     */
    private List<WorkTaskPageVO> shiftOverTask;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 附件
     */
    private List<FileVO> files;

    /**
     * 交班人签名
     */
    private FileVO shiftOverUserSign;

    /**
     * 接班人签名
     */
    private FileVO takeOverUserSign;

    /**
     * 扩展信息
     */
    private HandoverRecordExtendVO extend;
}
