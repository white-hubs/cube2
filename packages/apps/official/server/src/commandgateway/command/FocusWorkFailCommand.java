package com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command;

import lombok.Data;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项最终未完成说明
 */
@Data
public class FocusWorkFailCommand {

    private Long focusWorkId;//督办事项ID
    private String failReason;//失败或未完成原因
    private String fileIds;//附件IDS

}