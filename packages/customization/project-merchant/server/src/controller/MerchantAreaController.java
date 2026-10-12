package com.guhecloud.cube.deliver.project.merchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.dictionary.client.CascadeDictionaryClient;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.AreaDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AreaDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectMerchantAreaEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.SpaceMapper;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantAreaService;

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

import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantAreaController {

    private final MerchantAreaService merchantAreaService;
    private final DataDictionaryClient dataDictionaryClient;
    private final CascadeDictionaryClient cascadeDictionaryClient;
    private final SpaceMapper spaceMapper;

    @GetMapping("/areas/{id}")
    public AreaDTO getById(@PathVariable("id") String id) {
        AreaDTO dto = new AreaDTO(merchantAreaService.getById(id));
        //        this.extendBimInfo(dto);
        return dto;
    }

    @GetMapping("/areas/{id}/type")
    public String getTypeById(@PathVariable("id") String id) {
        AreaDeliver area = merchantAreaService.getById(id);
        if (area != null) {
            return area.getType();
        }
        return "";
    }

    @GetMapping("/areas/all-graph")
    public List<AreaDTO> selectAllGraph(@RequestParam(value = "projectId") String projectId) {
        return this.getAllGraph(projectId);
    }

    @GetMapping("/public/areas/all-graph")
    public List<AreaDTO> selectAllGraphPublic(@RequestParam(value = "projectId") String projectId) {
        return this.getAllGraph(projectId);
    }

    @GetMapping("/areas/all-graph/idsAndNames")
    public List<AreaDTO> selectAllGraphIdsAndNames(@RequestParam(value = "projectId") String projectId) {
        List<AreaDeliver> rootAreas = merchantAreaService.lambdaQuery()
                .eq(AreaDeliver::getProjectId, projectId)
                .eq(AreaDeliver::getPid, "")
                .orderByAsc(AreaDeliver::getSort)
                .list();
        List<AreaDTO> rootAreaDTOS = new AreaDTO(rootAreas).list();
        rootAreaDTOS.forEach(dto -> {
            dto.getIds().add(dto.getId());
            dto.getNames().add(dto.getName());
        });
        rootAreaDTOS.parallelStream().forEach(this::getChildAreasIdsAndNames);
        return rootAreaDTOS;
    }

    @GetMapping("/areas/list")
    public List<AreaDTO> getByLevel(@RequestParam(value = "projectId") String projectId, @RequestParam(value = "level") String level) {
        return new AreaDTO(merchantAreaService.lambdaQuery().eq(AreaDeliver::getProjectId, projectId).eq(AreaDeliver::getLevel, level)
                                   .list()).list();
    }

    @GetMapping("/areas/listAll")
    public List<AreaDTO> getListall(@RequestParam(value = "projectId") String projectId) {
        List<AreaDeliver> areas = merchantAreaService.lambdaQuery().eq(AreaDeliver::getProjectId, projectId).list();
        if (areas.size() > 0) {
            return new AreaDTO(areas).list();
        }
        return null;
    }

    @GetMapping("/areas/first-level")
    public List<AreaDTO> getFirstLevel(@RequestParam(value = "projectId") String projectId) {
        List<AreaDTO> areaDTOS = new AreaDTO(merchantAreaService.lambdaQuery().eq(AreaDeliver::getProjectId, projectId)
                                                     .eq(AreaDeliver::getPid, "").list()).list();
        areaDTOS.sort(Comparator.comparing(AreaDTO::getLevel));
        return areaDTOS;
    }

    @GetMapping("/areas/pid")
    public List<AreaDTO> getByPid(@RequestParam(value = "projectId") String projectId,
                                  @RequestParam(value = "pid", defaultValue = "") String pid
                                 ) {
        List<AreaDTO> areaDTOS = new AreaDTO(merchantAreaService.lambdaQuery().eq(AreaDeliver::getProjectId, projectId)
                                                     .eq(AreaDeliver::getPid, pid).list()).list();
        areaDTOS.sort(Comparator.comparing(AreaDTO::getLevel));
        return areaDTOS;
    }

    @GetMapping("/areas/path")
    public List<String> parsePath(@RequestParam("areaId") String areaId) {
        return merchantAreaService.parsePath(areaId);
    }

    @GetMapping("/areas/names")
    public List<String> parseName(@RequestParam("areaIds") List<String> areaIds) {
        return merchantAreaService.parseNames(areaIds);
    }

    //        //    @GetMapping("/areas/stat-site/v2/bimInfo")
    //    public List<AreaDTO> getBimInfoByAllGraph(@RequestParam("projectId") String projectId, @RequestParam(name = "areaId", defaultValue = "") String areaId) {
    //        List<Area> allAreas = areaService.lambdaQuery()
    //                .eq(Area::getProjectId, projectId)
    //                .list();
    //        if (allAreas.size() > 0) {
    //
    //            List<BimInfo> bimInfos = bimInfoService.lambdaQuery()
    //                    .eq(BimInfo::getBusinessType, AreaService.BUSINESS_TYPE)
    //                    .list();
    //            if (bimInfos.size() > 0) {
    //                Map<String, BimInfo> areaIdBimInfoMap = bimInfos.parallelStream().collect(Collectors.toMap(BimInfo::getBusinessKey, Function.identity()));
    //                List<Area> rootAreas = allAreas.stream()
    //                        .filter(area -> areaId.equals(area.getPid())).sorted(Comparator.comparing(Area::getSort))
    //                        .collect(Collectors.toList());
    //                List<AreaDTO> rootAreaDTOS = new AreaDTO(rootAreas).list();
    //                rootAreaDTOS.parallelStream().forEach(rootAreaDTO -> this.getChildren(rootAreaDTO, allAreas, areaIdBimInfoMap));
    //                return rootAreaDTOS;
    //            }
    //
    //        }
    //
    //        return new ArrayList<>();
    //    }

    @PostMapping("/areas/extend-bimInfo")
    public AreaDTO extendAreaDtoBimInfo(@RequestBody AreaDTO dto) {
        return merchantAreaService.extendAreaDtoBimInfo(dto);
    }

    @PostMapping("/areas")
    public void save(@Valid @RequestBody AreaDTO areaDTO) {
        merchantAreaService.save(areaDTO);
    }

    @PutMapping("/areas")
    public void update(@Valid @RequestBody AreaDTO areaDTO) {
        merchantAreaService.update(areaDTO);
    }

    @DeleteMapping("/areas/{id}")
    public void remove(@PathVariable("id") String id) {
        merchantAreaService.remove(id);
    }

    private List<AreaDTO> getAllGraph(String projectId) {
        List<AreaDeliver> rootAreas = merchantAreaService.lambdaQuery()
                .eq(AreaDeliver::getProjectId, projectId)
                .eq(AreaDeliver::getPid, "")
                .orderByAsc(AreaDeliver::getSort)
                .list();
        List<AreaDTO> rootAreaDTOS = new AreaDTO(rootAreas).list();
        rootAreaDTOS.parallelStream().forEach(this::getChildAreas);
        return rootAreaDTOS;
    }

    private void getChildAreas(AreaDTO rootAreaDTO) {
        var count = spaceMapper.selectCount(new LambdaQueryWrapper<Space>()
                                                        .eq(Space::getAreaId1, rootAreaDTO.getId()));
        rootAreaDTO.setSpaceNum(count.intValue());
        rootAreaDTO.setTypeName(ProjectMerchantAreaEnum.getCodeById(Long.parseLong(rootAreaDTO.getType())));
        rootAreaDTO.setLevelName(ProjectMerchantAreaEnum.getCodeByName(rootAreaDTO.getLevel()));

        List<AreaDeliver> childAreas = merchantAreaService.lambdaQuery()
                .eq(AreaDeliver::getPid, rootAreaDTO.getId())
                .orderByAsc(AreaDeliver::getSort)
                .list();
        if (childAreas.size() == 0) {
            return;
        }
        List<AreaDTO> childAreaDTOS = new AreaDTO(childAreas).list();
        childAreaDTOS.forEach(this::getChildAreas);
        rootAreaDTO.setChildren(childAreaDTOS);
        rootAreaDTO.setAreasNum(childAreas.size());

        //        CascadeDictionaryParam cascadeDictionaryParam = new CascadeDictionaryParam();
        //        cascadeDictionaryParam.setItemCode("project-merchant-area");
        //        List<CascadeDictionaryDTO> cascadeDictionaryList = cascadeDictionaryClient.getCascadeDictionaryItem(cascadeDictionaryParam);
    }

    private void getChildAreasIdsAndNames(AreaDTO rootAreaDTO) {
        List<AreaDeliver> childAreas = merchantAreaService.lambdaQuery()
                .eq(AreaDeliver::getPid, rootAreaDTO.getId())
                .orderByAsc(AreaDeliver::getSort)
                .list();
        if (childAreas.size() == 0) {
            return;
        }
        List<AreaDTO> childAreaDTOS = new AreaDTO(childAreas).list();
        childAreaDTOS.forEach(dto -> {
            dto.getNames().addAll(rootAreaDTO.getNames());
            dto.getNames().add(dto.getName());
            dto.getIds().addAll(rootAreaDTO.getIds());
            dto.getIds().add(dto.getId());
        });
        childAreaDTOS.forEach(this::getChildAreasIdsAndNames);
        rootAreaDTO.setChildren(childAreaDTOS);
        rootAreaDTO.setAreasNum(childAreas.size());
    }

    public String getLevelName(List<CascadeDictionaryDTO> dtos, String level) {
        for (CascadeDictionaryDTO ex : dtos) {
            if (level.equals(ex.getCode())) {
                return ex.getName();
            }
            this.getLevelName(ex.getNextList(), level);
        }
        return "";
    }

}
