package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBaseVO {

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
    private Long typeId;
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
     * 所属部门主键
     */
    private Long deptId;

    /**
     * 所属部门
     */
    private String deptName;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private List<FileVO> fileList;

    /**
     * 所属产品id
     */
    private Long productId;

    private String productName;

    /**
     * AI知识库id
     */
    private String aiLibraryId;

    /**
     * AI文档id
     */
    private String aiDocumentId;
}
