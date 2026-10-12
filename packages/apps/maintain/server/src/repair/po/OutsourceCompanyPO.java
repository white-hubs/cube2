package com.guhecloud.cube.srv.gen.maintain.server.repair.po;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/5/18 15:28
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("outsource_company")
public class OutsourceCompanyPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    private String name;

    private String outsourceModel;

    private String outsourceScope;

    public List<Long> getOutsourceModelList() {
        return JSONArray.parseArray(this.outsourceModel, Long.class);
    }

    public List<String> getOutsourceScopeList() {
        return JSONArray.parseArray(this.outsourceScope, String.class);
    }

    private String responsiblePerson;

    private String responsiblePersonPhone;

    public OutsourceCompanyPO() {
        this.id = IdWorker.getId();
    }
}
