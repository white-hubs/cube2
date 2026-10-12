package com.guhecloud.cube.biz.work.center.server.param;

import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 工作任务查询参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WorkTaskQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 待办标签编号
     */
    @Size(max = 50, message = "待办标签不能大于50")
    private String workTagCode;

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
    private String workTagCodes;

    /**
     * 业务状态
     */
    private String status;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 人员id集合
     */
    private List<Long> userIdList;

    public Set<String> getBusinessTypes() {
        if (CollectionUtils.isNotEmpty(businessTypes)) {
            return businessTypes;
        }
        if (StringUtils.isNotEmpty(this.businessType)) {
            return Collections.singleton(this.businessType);
        }
        if (StringUtils.isNotEmpty(this.workTagCode)) {
            //根据标签获取到标签关联的业务类型列表
            WorkTagService workTagService = ApplicationContextHolder.getBean(WorkTagService.class);
            return workTagService.listBusinessTypesByCode(this.workTagCode);
        }
        return businessTypes;
    }
}
