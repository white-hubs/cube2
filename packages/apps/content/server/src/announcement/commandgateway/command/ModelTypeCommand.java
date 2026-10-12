package com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command;

import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.InformationTypeEnum;
import lombok.Data;

@Data
public class ModelTypeCommand {
    //模块类型
    private String modelType;
    //类型名称
    private String typeName;
    //类型等级
    private String modelLevel;
    //一级类型
    private InformationTypeEnum oneLevelType;
}
