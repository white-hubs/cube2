package com.guhecloud.cube.srv.gen.maintain.server.common.enumeration;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 工单状态枚举
 *
 * @author lzy
 * @date 2022/5/6
 */
@Getter
public enum TicketStatusEnum {

    WAITING_CONFIRM("待确认"),

    WAITING_FOR_HANDLE("待处理"),

    TO_BE_CLOSED("待关闭"),

    FINISHED("已结束");

    private final String desc;

    TicketStatusEnum(String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(TicketStatusEnum.valueOf(name)) ? "" :
               TicketStatusEnum.valueOf(name).getDesc();
    }

    /**
     * 非活跃状态
     *
     * @return
     */
    public static List<String> getNotActiveStatus() {
        return Stream.of(FlowResult.REJECT.name(), FlowResult.UNDO.name(), FlowResult.PASS.name()).collect(Collectors.toList());
    }
}
