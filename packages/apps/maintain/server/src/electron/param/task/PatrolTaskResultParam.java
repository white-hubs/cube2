package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 提交巡更结果
 * @date 2023/5/29 13:58
 */
@Data
public class PatrolTaskResultParam {
    /**
     * 巡更记录id
     */
    private Long id;
    /**
     * 巡更结果
     */
    private String result;
    /**
     * 说明
     */
    private String remark;
    /**
     * 是否转隐患
     */
    private Boolean isToHidden;
    /**
     * 隐患流程实例id
     */
    private String hiddenId;
    /**
     * 文件主键
     */
    private List<Long> fileIds;

}
