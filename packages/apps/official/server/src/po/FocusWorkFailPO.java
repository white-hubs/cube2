package com.guhecloud.cube.srv.gen.official.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项未完成回复
 */
@Data
@TableName("focus_work_fail")
public class FocusWorkFailPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    private Long focusWorkId;

    private String failReason;

    private String fileIds;

}