package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项扩展最新反馈
 */
@Data
@Builder
public class FocusWorkExtendReplyVO {
    private FocusWorkVO focusWork;
    private FocusWorkReplyVO focusWorkReply;
    private FocusWorkFailVO focusWorkFail;
}