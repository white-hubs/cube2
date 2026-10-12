package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces;

import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.KnowledgeBaseCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.KnowledgeBaseCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeBaseVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeBatchVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeTypeStatVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.KnowledgeBaseQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.KnowledgeBaseQuery;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 知识库
 * 原来的设备文档和建筑资料整合成知识库
 * 所以 设备文档和建筑资料 废弃
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeBaseController {

    private final KnowledgeBaseCommandService commandService;
    private final KnowledgeBaseQueryService queryService;

    /**
     * 分页列表
     *
     * @param query 条件
     *
     * @return 分页列表
     */
    @GetMapping("/knowledge/page")
    public Pagination<KnowledgeBaseVO> pages(KnowledgeBaseQuery query) {
        return queryService.getPage(query);
    }

    /**
     * 详情
     *
     * @param query id主键
     *
     * @return 详情
     */
    @GetMapping("/knowledge/detail")
    public KnowledgeBaseVO detail(KnowledgeBaseQuery query) {
        return queryService.getDetail(query);
    }

    /**
     * 新增知识文档
     *
     * @param command 知识文档
     *
     * @return 主键
     */
    @PostMapping("/knowledge/add")
    public Long insert(@RequestBody @Valid KnowledgeBaseCommand command) {
        return commandService.addCommand(command);
    }

    /**
     * 编辑知识文档
     *
     * @param command 知识文档
     *
     * @return 主键
     */
    @PutMapping("/knowledge/edit")
    public Long edit(@RequestBody KnowledgeBaseCommand command) {
        return commandService.editCommand(command);
    }

    /**
     * 批量删除知识文档
     *
     * @param command idList 需要删除的id集合
     *
     * @return 是否成功
     */
    @PostMapping("/knowledge/removes")
    public Boolean removeBatch(@RequestBody KnowledgeBaseCommand command) {
        return commandService.removeBatch(command);
    }

    /**
     * 批量添加
     *
     * @param batchCommand 文档集合
     *
     * @return 结果
     */
    @PostMapping("/knowledge/add/batch")
    public KnowledgeBatchVO addBatch(@RequestBody List<KnowledgeBaseCommand> batchCommand) {
        return commandService.addBatch(batchCommand);
    }

    /**
     * 统计文档各类型数量
     *
     * @return 统计结果
     */
    @GetMapping("/knowledge/stat/type")
    public KnowledgeTypeStatVO statKnowledgeByType() {
        return queryService.statKnowledgeByType();
    }

}
