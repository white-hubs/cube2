package com.guhecloud.cube.base.message2.server.base.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.bff.param.MsgCustomGroupParam;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 消息查询参数
 *
 * @author lanchun
 * @date 2021/11/16 17:13
 */
@Data
public class MsgRecordQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 通道类型
     */
    @NotNull(message = "通道类型不能为空")
    private ChannelEnum channelType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号不能大于100个字符")
    private String applicationCode;

    /**
     * 自定义分类名称
     */
    private String customType;

    /**
     * 类别(区分是自定义类型or应用)
     */
    private String category;

    /**
     * 是否已读
     */
    private Boolean hasRead;

    /**
     * 日期范围
     */
    private Period period;

    /**
     * 发送状态
     */
    private Boolean sendStatus;

    /**
     * 自定义分组条件
     */
    private MsgCustomGroupParam msgCustomGroup;

    /**
     * 过滤掉的分组条件
     */
    private Set<MsgCustomGroupParam> filterMsgCustomGroups;

}
