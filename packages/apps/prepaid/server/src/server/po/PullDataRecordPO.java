package com.guhecloud.cube.srv.prepaid.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 拉取记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pull_data_record")
@EqualsAndHashCode(callSuper = true)
public class PullDataRecordPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 拉取的业务
     */
    private String pullBusiness;
    /**
     * 拉取时间
     */
    private LocalDateTime pullTime;
    /**
     * 拉取类型(全量,增量.目前只用到增量拉取)
     */
    private String pullType;
    /**
     * 拉取数据总量
     */
    private Long pullTotal;
    /**
     * 拉取结果
     */
    private String pullResult;
    /**
     * 拉取数据id
     */
    private String pullDataId;
    /**
     * 备注
     */
    private String remark;
}
