package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.converters.EnergyProjectConverterForEnergyProjectTreeVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectTreeVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lrj
 * @Description 能耗分项
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyProjectController {

    private final EnergyProjectService energyProjectService;

    private final EnergyCollectConfigService energyCollectConfigService;

    /**
     * 功能描述： 能耗分项添加
     */
    @PostMapping(value = "/energy-project")
    public Long add(@Valid @RequestBody EnergyProjectParam param) {
        return energyProjectService.add(param);
    }

    /**
     * 功能描述： 能耗分项修改
     */
    @PutMapping(value = "/energy-project/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EnergyProjectParam param) {
        energyProjectService.update(id, param);
    }

    /**
     * 功能描述： 能耗分项删除
     */
    @DeleteMapping(value = "/energy-project/{id}")
    public void delete(@PathVariable Long id) {
        energyProjectService.delete(id);
    }

    /**
     * 功能描述： 能耗分项批量删除
     */
    @DeleteMapping(value = "/energy-project/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        energyProjectService.batchDelete(ids);
    }

    /**
     * 功能描述： 能耗分项分页
     */
    @GetMapping(value = "/energy-project/page")
    public Pagination<EnergyProjectVO> getPage(EnergyProjectQueryParam param) {
        return energyProjectService.getPage(param);
    }

    /**
     * 功能描述： 能耗分项集合
     */
    @GetMapping(value = "/energy-project/list")
    public List<EnergyProjectVO> list(EnergyProjectQueryParam param) {
        return energyProjectService.getList(param);
    }

    /**
     * 功能描述： 能耗分项详情
     */
    @GetMapping(value = "/energy-project/{id}")
    public EnergyProjectVO getById(@PathVariable Long id) {
        return energyProjectService.getById(id);
    }

    /**
     * 分项树
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-project/tree")
    public List<EnergyProjectTreeVO> getTree(@Valid EnergyProjectQueryParam param) {
        var list = energyProjectService.list(Wrappers.<EnergyProject>lambdaQuery()
                                                     .like(StringUtils.isNotBlank(param.getProjectName()), EnergyProject::getProjectName, param.getProjectName())
                                                     .eq(param.getParentId() !=
                                                         null, EnergyProject::getParentId, param.getParentId())
                                                     .eq(EnergyProject::getInstrumentType, param.getInstrumentType()));
        if (CollectionUtils.isNotEmpty(list)) {
            var ids = list.stream().filter(d -> StringUtils.isNotBlank(d.getSequence()))
                    .flatMap(d -> Arrays.stream(d.getSequence().split("-"))).distinct().map(Long::parseLong).collect(Collectors.toList());
            var energyProjects = energyProjectService.list(Wrappers.<EnergyProject>lambdaQuery()
                                                                   .in(EnergyProject::getId, ids));
            var energyProjectTreeVOS = EnergyProjectConverterForEnergyProjectTreeVO.toEnergyProjectTreeVOS(energyProjects);
            return TreeUtils.assembleTrees(energyProjectTreeVOS);
        }
        return Collections.emptyList();
    }

    /**
     * 特殊的一棵树，按照设备类型进行了组装
     *
     * @param instrumentType
     *
     * @return
     */
    @GetMapping(value = "/energy-project/tree/special")
    public List<EnergyProjectTreeVO> getTreeForSpecial(String instrumentType) {
        var list = energyProjectService.list(Wrappers.<EnergyProject>lambdaQuery()
                                                     .eq(StringUtils.isNotBlank(instrumentType), EnergyProject::getInstrumentType, instrumentType));
        if (CollectionUtils.isNotEmpty(list)) {
            var categoryMap = energyCollectConfigService.getCategoryMap();
            //返回数据
            List<EnergyProjectTreeVO> finalTree = new ArrayList<>();
            //根据仪表类型分组
            var epGroupMap = list.stream().collect(Collectors.groupingBy(EnergyProject::getInstrumentType));
            epGroupMap.forEach((key, value) -> {
                var energyProjectTreeVOS = EnergyProjectConverterForEnergyProjectTreeVO.toEnergyProjectTreeVOS(value);
                TreeUtils.assembleTrees(energyProjectTreeVOS);
                //构建一个顶层节点
                EnergyProjectTreeVO projectRoot = new EnergyProjectTreeVO();
                projectRoot.setProjectName(categoryMap.get(key).getCategoryName());
                projectRoot.setChildren(energyProjectTreeVOS);
                finalTree.add(projectRoot);
            });
            return finalTree;
        }
        return Collections.emptyList();
    }

    /**
     * 保存能耗分项 关联的设备
     *
     * @param id
     * @param referDeviceList
     */
    @PostMapping(value = "/energy-project/{id}/refer/device")
    public void saveProjectReferDevice(@PathVariable Long id,
                                       @Valid @RequestBody List<EnergyProjectItemParam> referDeviceList
                                      ) {
        energyProjectService.saveProjectReferDevice(id, referDeviceList);
    }

    /**
     * 获取分项关联的设备
     *
     * @param id
     */
    @GetMapping(value = "/energy-project/{id}/refer/device")
    public List<EnergyProjectItemVO> getProjectReferDevice(@PathVariable Long id) {
        return energyProjectService.getProjectReferDevice(id);
    }

    /**
     * 根据分项ID 查询设备
     * 分项ID 可以为空
     *
     * @param projectId
     *
     * @return
     */
    @GetMapping(value = "/energy-project/all/device")
    List<DeviceInEnergyProjectVO> getAllDeviceInProject(String projectId) {
        return energyProjectService.getAllDeviceInProject(projectId);
    }
}