package com.guhecloud.cube.biz.todo.server.param;

import com.guhecloud.cube.biz.todo.server.service.TodoTagService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

/**
 * @author lanchun
 * @date 2022/7/22 11:00
 */
@Data
public class TodoTaskQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 待办标签编号
     */
    @Size(max = 50, message = "待办标签不能大于50")
    private String todoTagCode;

    /**
     * 业务来源
     */
    @Size(max = 100, message = "业务来源不能大于100")
    private String businessType;

    /**
     * 是否允许移交
     */
    private Boolean handover;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 业务来源集合
     */
    private Set<String> businessTypes;

    /**
     * 待办标签集合
     */
    private String todoTagCodes;

    /**
     * 任务状态 (全部：null，待处理：TODO，已处理：DONE，我发布的：CREATE)
     */
    private String status;

    /**
     * 创建人
     */
    private String createBy;

    public Set<String> getBusinessTypes() {
        if (CollectionUtils.isNotEmpty(businessTypes)) {
            return businessTypes;
        }
        if (StringUtils.isNotEmpty(this.businessType)) {
            return Collections.singleton(this.businessType);
        }
        if (StringUtils.isNotEmpty(this.todoTagCode)) {
            //根据标签获取到标签关联的业务类型列表
            TodoTagService todoTagService = ApplicationContextHolder.getBean(TodoTagService.class);
            return todoTagService.listBusinessTypesByCode(this.todoTagCode);
        }
        return businessTypes;
    }
}
