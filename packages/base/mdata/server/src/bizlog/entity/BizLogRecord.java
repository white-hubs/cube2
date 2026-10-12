package com.guhecloud.cube.biz.mdata.server.bizlog.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;

@Data
public class BizLogRecord extends BasicEnhancedEntity<Long> {
    private Long id;
    private String account;
    private String userName;
    private String bizNo;//业务主键，可以为空
    private String action;//操作描述
    private String extra;//其他信息
    private String codeVariable;//操作的类信息
    private Boolean fail;//false 成功，true 失败；失败的信息也再action中
    private String type;//定义为模块名称或（应用名称）
    private String typeName;
    private String subType;//业务功能
    private String subTypeName;
    private String operationType;// ADD UPDATE DELETE QUERY
    private Integer tenant;
}
