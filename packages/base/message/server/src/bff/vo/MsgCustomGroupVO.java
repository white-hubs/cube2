package com.guhecloud.cube.base.message2.server.bff.vo;

import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordPageVO;
import lombok.Builder;
import lombok.Data;

/**
 * 消息自定义类型分组返回参数
 *
 * @author lanchun
 * @date 2024/7/8 13:59
 */
@Builder
@Data
public class MsgCustomGroupVO {

    /**
     * 自定义类型编号
     */
    private String type;

    /**
     * 自定义类型名称
     */
    private String typeName;

    /**
     * 类别(区分是自定义类型or应用)
     */
    private String category;

    /**
     * 未读消息总数
     */
    private Integer unReadMsgCount;

    /**
     * 最近一条消息记录
     */
    private MsgRecordPageVO lastMsgRecord;
}
