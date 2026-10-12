package com.guhecloud.cube.deliver.project.merchant.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.util.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceExportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceImportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDevice;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDeviceType;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerDeviceService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerDeviceTypeService;

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
 * 商户档案-所有设备（批发商的设备） 前端控制器
 * </p>
 *
 * @author wangliang
 * @since 2022-01-04
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerDeviceController {

    private final WholesalerDeviceService service;
    private final WholesalerDeviceTypeService deviceTypeService;
    private final FileClient fileClient;

    @GetMapping("/wholesaler-device/{id}")
    public WholesalerDeviceDTO getById(@PathVariable("id") String id) {
        WholesalerDevice device = service.getById(id);
        if (device == null) {
            return null;
        }
        WholesalerDeviceDTO dto = new WholesalerDeviceDTO(device);
        this.extend(dto);
        return dto;
    }

    @GetMapping("/wholesaler-device/page")
    public PageResponse<WholesalerDeviceDTO> getDevices(WholesalerDeviceQueryDTO queryDto, PageRequest pageRequest) {
        Page<WholesalerDevice> page = this.getWrappers(queryDto).page(pageRequest.page());
        List<WholesalerDeviceDTO> dtos = new WholesalerDeviceDTO(page.getRecords()).list();
        dtos.forEach(this::extendList);
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    @GetMapping("/wholesaler-device/list")
    public List<WholesalerDeviceDTO> getDeviceList(WholesalerDeviceQueryDTO queryDto) {
        List<WholesalerDevice> devices = this.getWrappers(queryDto).list();
        List<WholesalerDeviceDTO> dtos = new WholesalerDeviceDTO(devices).list();
        dtos.forEach(this::extendList);
        return dtos;
    }

    @GetMapping("/wholesaler-device/template-download")
    public void getDeviceTemplate(HttpServletRequest request, HttpServletResponse response) {
        service.getDeviceTemplate(request, response);
    }

    @PostMapping("/wholesaler-device")
    public String save(@Valid @RequestBody WholesalerDeviceDTO dto) {
        return service.save(dto);
    }

    @PostMapping("/wholesaler-device/data-import")
    public List<ErrorExcelDTO> importDevices(@RequestParam("projectId") String projectId, @RequestParam("file") MultipartFile file) {
        ExcelUtils<WholesalerDeviceImportExcelDTO> utils = new ExcelUtils<>(WholesalerDeviceImportExcelDTO.class);
        List<WholesalerDeviceImportExcelDTO> dtos;
        try {
            dtos = utils.getDataByExcel(file.getInputStream(), WholesalerDeviceService.SHEET_NAME);
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
        } catch (Exception e) {
            throw new ServiceException("文件解析异常！");
        }
        return null;
    }

    @PutMapping("/wholesaler-device")
    public void update(@Valid @RequestBody WholesalerDeviceDTO dto) {
        service.update(dto);
    }

    @DeleteMapping("/wholesaler-device/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

    @GetMapping("/wholesaler-device/data-export")
    public void exportData(WholesalerDeviceQueryDTO queryDto, HttpServletRequest request, HttpServletResponse response) {
        List<WholesalerDeviceDTO> deviceDTOList = new WholesalerDeviceDTO(this.getWrappers(queryDto).list()).list();
        // 扩展设备类型的名称
        deviceDTOList.parallelStream().forEach(dto -> {
            WholesalerDeviceType type = deviceTypeService.getById(dto.getTypeId());
            dto.setTypeName(type == null ? "" : type.getName());
        });
        List<WholesalerDeviceExportExcelDTO> dtos = new WholesalerDeviceExportExcelDTO(deviceDTOList).list();
        // 按照档位排序
        dtos.sort(Comparator.comparing(d -> PinYinUtils.getHead(d.getStallName())));
        ExcelUtils<WholesalerDeviceExportExcelDTO> utils = new ExcelUtils<>(WholesalerDeviceExportExcelDTO.class);
        utils.createWorkbook();
        try {
            utils.createSheet("设备信息", dtos);
        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("商户设备备案信息", request, response);
    }

    public LambdaQueryChainWrapper<WholesalerDevice> getWrappers(WholesalerDeviceQueryDTO queryDto) {
        return service.lambdaQuery()
                .eq(StrUtil.isNotBlank(queryDto.getProjectId()), WholesalerDevice::getProjectId, queryDto.getProjectId())
                .eq(StrUtil.isNotBlank(queryDto.getType()), WholesalerDevice::getTypeId, queryDto.getType())
                .eq(StrUtil.isNotBlank(queryDto.getCompanyId()), WholesalerDevice::getCompanyId, queryDto.getCompanyId())
                .eq(StrUtil.isNotBlank(queryDto.getStallId()), WholesalerDevice::getStallId, queryDto.getStallId())
                .eq(queryDto.getDanger() != null, WholesalerDevice::getDanger, queryDto.getDanger())
                .and(StrUtil.isNotBlank(queryDto.getSearch()), q -> q
                        .like(WholesalerDevice::getStallName, queryDto.getSearch())
                        .or()
                        .like(WholesalerDevice::getCompanyName, queryDto.getSearch())
                        .or()
                        .like(WholesalerDevice::getDescription, queryDto.getSearch())
                        .or()
                        .like(WholesalerDevice::getCreateBy, queryDto.getSearch())
                    )
                .between(null != queryDto.getBeginTime() &&
                         null != queryDto.getEndTime(), WholesalerDevice::getCreateTime, queryDto.getBeginTime(), queryDto.getEndTime());
    }

    private void extend(WholesalerDeviceDTO dto) {
        // 查询关联的文件
        this.getMarkFile(dto);
        // 查询设备类型名称
        WholesalerDeviceType deviceType = deviceTypeService.getById(dto.getTypeId());
        dto.setTypeName(deviceType == null ? "" : deviceType.getName());
    }

    private void extendList(WholesalerDeviceDTO dto) {
        // 为了满足前端组件在编辑时回显档位的名称，把档位的信息装在数组中
        List<SpaceDTO> spaceDTOS = new ArrayList<>();
        SpaceDTO spaceDTO = new SpaceDTO();
        spaceDTO.setId(dto.getStallId());
        spaceDTO.setName(dto.getStallName());
        spaceDTO.setCompanyId(dto.getCompanyId());
        spaceDTO.setCompanyName(dto.getCompanyName());
        spaceDTOS.add(spaceDTO);
        dto.setSpaceDTOS(spaceDTOS);
        // 查询关联的信息
        this.extend(dto);
    }

    /**
     * 获取评论DTO关联的文件
     */
    private void getMarkFile(WholesalerDeviceDTO dto) {
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

