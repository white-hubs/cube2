package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.AreaListVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.AreaProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.AreaProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AreaProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaTreeVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lrj
 * @Description 区域分项配置
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaProjectController {
    private final AreaProjectService areaProjectService;

    private final AdapterDeviceService adapterDeviceService;

    /**
     * 功能描述： 区域分项配置添加
     */
    @PostMapping(value = "/area-project")
    public Long add(@Valid @RequestBody AreaProjectParam param) {
        return areaProjectService.add(param);
    }

    /**
     * 区域绑定设备
     *
     * @param id
     * @param deviceList
     */
    @PostMapping(value = "/area-project/{id}/refer/device")
    public void areaReferDevice(@PathVariable Long id, @RequestBody List<EnergyProjectItemParam> deviceList) {
        areaProjectService.areaReferDevice(id, deviceList);
    }

    /**
     * 功能描述： 区域分项配置修改
     */
    @PutMapping(value = "/area-project/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody AreaProjectParam param) {
        areaProjectService.update(id, param);
    }

    /**
     * 功能描述： 区域分项配置删除
     */
    @DeleteMapping(value = "/area-project/{id}")
    public void delete(@PathVariable Long id) {
        areaProjectService.delete(id);
    }

    /**
     * 功能描述： 区域分项配置批量删除
     */
    @DeleteMapping(value = "/area-project/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        areaProjectService.batchDelete(ids);
    }

    /**
     * 功能描述： 区域分项配置分页
     */
    @GetMapping(value = "/area-project/page")
    public Pagination<AreaProjectVO> getPage(AreaProjectQueryParam param) {
        return areaProjectService.getPage(param);
    }

    /**
     * 功能描述： 区域分项配置详情
     */
    @GetMapping(value = "/area-project/{id}")
    public AreaProjectVO getById(@PathVariable Long id) {
        return areaProjectService.getById(id);
    }

    /**
     * 通过区域分项ID 查询关联的设备
     *
     * @param id
     *
     * @return
     */
    @GetMapping(value = "/area-project/{id}/refer/device")
    public List<EnergyProjectItemVO> getReferDeviceById(@PathVariable Long id) {
        return areaProjectService.getReferDeviceById(id);
    }

    /**
     * 功能描述： 区域分项配置列表
     */
    @GetMapping(value = "/area-project/list")
    public List<AreaProjectVO> list() {
        return ObjectUtils.copying(areaProjectService.list(), AreaProjectVO.class);
    }

    /**
     * 功能分区树
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/area-project/tree")
    public List<AreaTreeVO> getTree(@Valid AreaProjectQueryParam param) {
        List<AreaProject> list = areaProjectService.list(Wrappers.<AreaProject>lambdaQuery()
                                                                 .like(StringUtils.isNotBlank(param.getAreaName()), AreaProject::getAreaName, param.getAreaName())
                                                                 .eq(param.getParentId() !=
                                                                     null, AreaProject::getParentId, param.getParentId())
                                                                 .eq(StringUtils.isNotBlank(param.getInstrumentType()), AreaProject::getInstrumentType, param.getInstrumentType()));
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> ids = list.stream().flatMap(d -> Arrays.stream(d.getSequence().split("-"))).distinct().map(Long::parseLong)
                    .collect(Collectors.toList());
            List<AreaProject> datas = areaProjectService.list(Wrappers.<AreaProject>lambdaQuery()
                                                                      .in(AreaProject::getId, ids));
            List<AreaTreeVO> areaVOList = ObjectUtils.copying(datas, AreaTreeVO.class);
            //计算区域下绑定的设备数量
            areaVOList.stream().forEach(data -> {
                long deviceNum = areaProjectService.getDeviceNumInAreaProject(data.getId());
                data.setDeviceNum(deviceNum);
            });
            List<AreaTreeVO> areaTreeVOList = TreeUtils.assembleTrees(areaVOList);
            return areaTreeVOList;
        }
        return Collections.emptyList();
    }

    /**
     * 区域列表
     */
    @GetMapping(value = "/area-list")
    public List<AreaListVO> areaList() {
        return adapterDeviceService.areaWithDeviceList();
    }

    /**
     * 根据分区ID 查询设备
     * 分区ID 可以为空
     *
     * @param projectId
     *
     * @return
     */
    @GetMapping(value = "/area-project/all/device")
    public List<DeviceInEnergyProjectVO> getAllDeviceInAreaProject(String projectId) {
        return areaProjectService.getAllDeviceInAreaProject(projectId);
    }
}