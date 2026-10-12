package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Data
public class BuildingDataVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 资料名称
     */
    private String name;

    /**
     * 资料类型
     */
    private Long type;
    /**
     * 资料类型
     */
    private String typeName;

    /**
     * 资料格式
     */
    private String format;

    /**
     * 资料大小
     */
    @Decimal
    private BigDecimal size;

    /**
     * 所属区域主键
     */
    private List<Long> areaIds;

    private Long areaId;

    /**
     * 所属区域
     */
    private List<String> areas;

    private String areasName;

    /**
     * 所属部门主键
     */
    private List<Long> deptIds;
    /**
     * 所属部门主键
     */
    private Long deptId;

    /**
     * 所属部门
     */
    private String deptsName;

    /**
     * 创建人
     */
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private FileVO fileVO;

}
