package com.guhecloud.cube.biz.mdata.server.area.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroup;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroupConfig;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupModifyParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupConfigService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaGroupVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.util.EntityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 区域组管理接口
 *
 * @author lanchun
 * @date 2022/4/15 15:30
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaGroupController {

    private final AreaGroupService areaGroupService;

    private final AreaGroupConfigService areaGroupConfigService;

    /**
     * 区域组新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/area-group")
    public Long save(@Valid @RequestBody AreaGroupModifyParam param) {
        return this.areaGroupService.save(param);
    }

    /**
     * 区域组修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/area-group/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody AreaGroupModifyParam param) {
        this.areaGroupService.update(id, param);
    }

    /**
     * 区域组树形列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/area-group/list")
    public List<AreaGroupVO> listAreaGroups(@Valid AreaGroupQueryParam param) {
        List<AreaGroup> areaGroups = this.areaGroupService.listAreaGroups(param);
        if (CollectionUtils.isEmpty(areaGroups)) {
            return Collections.emptyList();
        }
        // 获取上级sequence
        List<String> sequences = EntityUtils.getAllSequences(areaGroups);
        List<AreaGroup> list = this.areaGroupService.getSequence(sequences);
        List<AreaGroupVO> areaGroupVOS = ObjectUtils.copying(list, AreaGroupVO.class);
        List<AreaGroupVO> areaGroupAreaVOS = areaGroupVOS.stream().peek(areaGroup -> areaGroup.setAreaCount(
                                                                                this.areaGroupConfigService.count(Wrappers.<AreaGroupConfig>lambdaQuery()
                                                                                                                          .likeLeft(AreaGroupConfig::getAreaGroup, areaGroup.getSequence())))
                                                                       ).collect(Collectors.toList());
        return TreeUtils.assembleTrees(areaGroupAreaVOS);
    }

    /**
     * 区域组删除
     *
     * @param id
     */
    @DeleteMapping("/area-group/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.areaGroupService.remove(id);
    }
}
