package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.BuildingDataCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.SubmitBuildingDataCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.BuildingDataVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.BuildingDataQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.BuildingDataCollectionQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 建筑资料接口
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 *
 * @author lzy
 * @date 2022/5/19
 */
@Deprecated
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BuildingDataController {

    private final BuildingDataCommandService buildingDataCommandService;
    private final BuildingDataQueryService buildingDataQueryService;

    /**
     * 建筑资料新增
     *
     * @param command 新增参数
     */
    @PostMapping("/building-data")
    public void save(@Valid @RequestBody SubmitBuildingDataCommand command) {
        this.buildingDataCommandService.submitBuildingData(command);
    }

    /**
     * 建筑资料删除
     *
     * @param id 主键
     */
    @DeleteMapping("/building-data/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.buildingDataCommandService.remove(id);
    }

    /**
     * 建筑资料删除批量
     *
     * @param ids 主键
     */
    @DeleteMapping("/building-data/batch")
    public void batchRemove(@RequestParam("ids") List<Long> ids) {
        this.buildingDataCommandService.batchRemove(ids);
    }

    /**
     * 建筑资料分页
     *
     * @param query 查询参数
     *
     * @return 建筑资料信息
     */
    @GetMapping("/building-data/page")
    public Page<BuildingDataVO> pageBuildingData(@Valid BuildingDataCollectionQuery query) {
        // 获取信息
        return this.buildingDataQueryService.pageBuildingData(query);
    }

}
