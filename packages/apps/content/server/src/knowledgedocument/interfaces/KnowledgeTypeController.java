package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces;

import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.KnowledgeTypeCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.KnowledgeTypeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeTypeTreeVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.KnowledgeTypeQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.KnowledgeTypeQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 知识文档类型
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeTypeController {

    private final KnowledgeTypeQueryService queryService;
    private final KnowledgeTypeCommandService commandService;

    /**
     * 树形类型集合
     *
     * @param query 条件
     *
     * @return 树形集合
     */
    @GetMapping("/knowledgeType/treeList")
    public List<KnowledgeTypeTreeVO> getKnowledgeTypeTree(KnowledgeTypeQuery query) {
        return queryService.getKnowledgeTypeTree(query);
    }

    /**
     * 添加类型
     *
     * @param command 文档类型
     *
     * @return id
     */
    @PostMapping("/knowledgeType/add")
    public Long insert(@RequestBody KnowledgeTypeCommand command) {
        return commandService.insert(command);
    }

    /**
     * 修改
     *
     * @param command 文档类型
     *
     * @return id
     */
    @PutMapping("/knowledgeType/edit")
    public Long edit(@RequestBody KnowledgeTypeCommand command) {
        return commandService.update(command);
    }

    /**
     * 删除
     *
     * @param id
     *
     * @return
     */
    @DeleteMapping("/knowledgeType/remove")
    public Boolean remove(Long id) {
        return commandService.remove(id);
    }

}
