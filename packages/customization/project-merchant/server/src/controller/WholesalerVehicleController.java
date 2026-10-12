package com.guhecloud.cube.deliver.project.merchant.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.util.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholeSpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleExportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleImportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerVehicle;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectMerchantAreaEnum;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerVehicleService;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.PinYinUtils;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商户档案-所有车辆（批发商交通工具表） 前端控制器
 * </p>
 *
 * @author wangliang
 * @since 2021-12-31
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerVehicleController {

    private final WholesalerVehicleService service;
    private final FileClient fileClient;
    private final DataDictionaryClient dataDictionaryClient;

    @GetMapping("/wholesaler-vehicle/{id}")
    public WholesalerVehicleDTO getById(@PathVariable("id") String id) {
        WholesalerVehicle vehicle = service.getById(id);
        if (vehicle == null) {
            return null;
        }
        WholesalerVehicleDTO dto = new WholesalerVehicleDTO(vehicle);
        dto.setVehicleTypeName(ProjectMerchantAreaEnum.getCodeByName(dto.getVehicleType()));
        this.getMarkFile(dto);
        return dto;
    }

    @GetMapping("/wholesaler-vehicle/page")
    public PageResponse<WholesalerVehicleDTO> getVehicles(WholesalerVehicleQueryDTO queryDto, PageRequest pageRequest) {
        Page<WholesalerVehicle> page = this.getWrappers(queryDto).page(pageRequest.page());
        List<WholesalerVehicleDTO> dtos = new WholesalerVehicleDTO(page.getRecords()).list();
        dtos.forEach(this::extendList);
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    @GetMapping("/wholesaler-vehicle/list")
    public List<WholesalerVehicleDTO> getVehicles(WholesalerVehicleQueryDTO queryDto) {
        List<WholesalerVehicle> vehicles = this.getWrappers(queryDto).list();
        return new WholesalerVehicleDTO(vehicles).list();
    }

    @GetMapping("/wholesaler-vehicle/template-download")
    public void getVehicleTemplate(HttpServletRequest request, HttpServletResponse response) {
        service.getVehicleTemplate(request, response);
    }

    @GetMapping("/wholesaler-vehicle/wholesaler-stalls")
    public List<WholeSpaceDTO> getWholesalerStalls(String projectId) {
        return service.getWholesalerStalls(projectId);
    }

    @PostMapping("/wholesaler-vehicle")
    public String save(@Valid @RequestBody WholesalerVehicleDTO dto) {
        if (StrUtil.isBlank(dto.getStallId())) {
            throw new ServiceException("添加车辆时候，需要输入所属档位ID");
        }
        return service.save(dto);
    }

    @PostMapping("/wholesaler-vehicle/data-import")
    public List<ErrorExcelDTO> importVehicles(@RequestParam("projectId") String projectId, @RequestParam("file") MultipartFile file) {
        ExcelUtils<WholesalerVehicleImportExcelDTO> utils = new ExcelUtils<>(WholesalerVehicleImportExcelDTO.class);
        List<WholesalerVehicleImportExcelDTO> dtos;
        try {
            dtos = utils.getDataByExcel(file.getInputStream(), WholesalerVehicleService.SHEET_NAME);
        } catch (Exception e) {
            if (ExcelUtils.CHECKDATA_ERROR.equals(e.getMessage())) {
                List<ExcelUtils.ExcelMsg> excelMsgs = utils.getExcelMsgs();
                if (excelMsgs != null) {
                    List<ErrorExcelDTO> collect = excelMsgs.stream()
                            .map(excelMsg -> new ErrorExcelDTO(excelMsg.getRowId(), excelMsg.getColName(), excelMsg.getMsg()))
                            .collect(Collectors.toList());
                    return collect;
                }
            }
            log.error("导入设备备案文件解析异常！", e);
            throw new ServiceException("文件解析异常！");
        }
        if (dtos == null) {
            throw new ServiceException("excel没有数据");
        }
        try {
            service.importData(projectId, dtos);
        } catch (ServiceException e) {
            throw new ServiceException("文件解析异常！");
        }
        return null;
    }

    @PutMapping("/wholesaler-vehicle")
    public void update(@Valid @RequestBody WholesalerVehicleDTO dto) {
        service.update(dto);
    }

    @DeleteMapping("/wholesaler-vehicle/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

    @GetMapping("/wholesaler-vehicle/data-export")
    public void exportData(WholesalerVehicleQueryDTO queryDto, HttpServletRequest request, HttpServletResponse response) {
        List<WholesalerVehicleDTO> vehicDTOList = new WholesalerVehicleDTO(this.getWrappers(queryDto).list()).list();
        // 查询车辆类型
        //        List<DictDataDTO> dataDTOS = dictDataApi.getList(WholesalerVehicleService.VEHICLE_TYPE);
        //        vehicDTOList.parallelStream().forEach(dto -> {
        //            DictDataDTO dictDataDTO = dataDTOS.stream().filter(d -> d.getCode().equals(dto.getVehicleType())).findAny().orElse(null);
        //            dto.setVehicleType(dictDataDTO == null ? "" : dictDataDTO.getName());
        //        });
        List<WholesalerVehicleExportExcelDTO> dtos = new WholesalerVehicleExportExcelDTO(vehicDTOList).list();
        // 按照档位排序
        dtos.sort(Comparator.comparing(d -> PinYinUtils.getHead(d.getStallName())));
        ExcelUtils<WholesalerVehicleExportExcelDTO> utils = new ExcelUtils<>(WholesalerVehicleExportExcelDTO.class);
        utils.createWorkbook();
        try {
            utils.createSheet("车辆信息", dtos);
        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("商户车辆备案信息", request, response);
    }

    private LambdaQueryChainWrapper<WholesalerVehicle> getWrappers(WholesalerVehicleQueryDTO queryDto) {
        return service.lambdaQuery()
                .eq(StrUtil.isNotBlank(queryDto.getProjectId()), WholesalerVehicle::getProjectId, queryDto.getProjectId())
                .eq(StrUtil.isNotBlank(queryDto.getType()), WholesalerVehicle::getVehicleType, queryDto.getType())
                .eq(StrUtil.isNotBlank(queryDto.getCompanyId()), WholesalerVehicle::getCompanyId, queryDto.getCompanyId())
                .eq(StrUtil.isNotBlank(queryDto.getStallId()), WholesalerVehicle::getStallId, queryDto.getStallId())
                .and(StrUtil.isNotBlank(queryDto.getSearch()), q -> q
                        .like(WholesalerVehicle::getVehiclePlate, queryDto.getSearch())
                        .or()
                        .like(WholesalerVehicle::getOwner, queryDto.getSearch())
                        .or()
                        .like(WholesalerVehicle::getStallName, queryDto.getSearch())
                        .or()
                        .like(WholesalerVehicle::getMobile, queryDto.getSearch())
                    )
                .orderByDesc(WholesalerVehicle::getCreateTime);
    }

    private void extendList(WholesalerVehicleDTO dto) {
        // 为了满足前端组件在编辑时回显档位的名称，把档位的信息装在数组中
        List<SpaceDTO> spaceDTOS = new ArrayList<>();
        SpaceDTO spaceDTO = new SpaceDTO();
        spaceDTO.setId(dto.getStallId());
        spaceDTO.setName(dto.getStallName());
        spaceDTO.setCompanyId(dto.getCompanyId());
        spaceDTO.setCompanyName(dto.getCompanyName());
        spaceDTOS.add(spaceDTO);
        dto.setSpaceDTOS(spaceDTOS);
        dto.setVehicleTypeName(ProjectMerchantAreaEnum.getCodeByName(dto.getVehicleType()));
        // 查询关联的文件
        this.getMarkFile(dto);
    }

    /**
     * 获取评论DTO关联的文件
     */
    private void getMarkFile(WholesalerVehicleDTO dto) {
        List<FileVO> FileVOs = null;
        try {
            FileVOs = fileClient.getByBusinessKey(Long.parseLong(dto.getId()));
        } catch (Exception e) {
            log.error("获取文件异常", e);
        }
        assert FileVOs != null;
        dto.setFileIds(FileVOs.stream().map(FileVO::getId).collect(Collectors.toList()));
        dto.setFileDTOS(FileVOs);
    }

}

