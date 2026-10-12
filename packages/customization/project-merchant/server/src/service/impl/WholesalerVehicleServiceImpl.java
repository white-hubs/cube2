package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleImportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerVehicle;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerVehicleMapper;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerVehicleService;
import com.guhecloud.cube.deliver.project.merchant.utils.FilesUtil;
import com.guhecloud.cube.deliver.project.merchant.dto.WholeSpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 商户档案-所有车辆（批发商交通工具表） 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2021-12-31
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerVehicleServiceImpl extends ServiceImpl<WholesalerVehicleMapper, WholesalerVehicle> implements WholesalerVehicleService {

    //    private final FileApi fileApi;
    private final SpaceService spaceService;
    private final WholesalerVehicleMapper mapper;
    private final WholesalerService wholesalerService;
    private final FileRefClient fileRefClient;
    private final DataDictionaryClient dataDictionaryClient;

    @Override
    public void getVehicleTemplate(HttpServletRequest request, HttpServletResponse response) {
        // 查询车辆类型数据
        List<String> dictNames = null;// dictDataApi.getList(VEHICLE_TYPE).stream().map(DictDataDTO::getName).collect(Collectors.toList());
        List<WholesalerVehicleImportExcelDTO> dtos = new ArrayList<>();
        ExcelUtils<WholesalerVehicleImportExcelDTO> utils = new ExcelUtils<>(WholesalerVehicleImportExcelDTO.class);
        utils.createWorkbook();
        try {
            XSSFSheet sheet = utils.createSheet(SHEET_NAME, dtos);
            utils.setXSSFValidation(sheet, "vehicleType", dictNames, 1, 2);
            utils.downLoadExcel("商户车辆备案模板", request, response);
        } catch (Exception e) {
            throw new ServiceException("下载模板失败");
        }
    }

    @Override
    public String save(WholesalerVehicleDTO dto) {
        List<WholesalerVehicle> list = this.lambdaQuery()
                .eq(WholesalerVehicle::getVehiclePlate, dto.getVehiclePlate()).list();
        if (list != null && !list.isEmpty()) {
            throw new ServiceException("该车牌已存在，请确认");
        }
        Space stall = spaceService.lambdaQuery().eq(Space::getId, dto.getStallId()).one();
        if (stall == null) {
            throw new ServiceException("该档位ID不存在");
        }
        dto.setStallName(stall.getName());
        dto.setCompanyId(stall.getCompanyId());
        dto.setCompanyName(stall.getCompanyName());
        dto.setProjectId(stall.getProjectId());
        WholesalerVehicle wholesalerVehicle = dto.convert();
        wholesalerVehicle.setCreateTime(LocalDateTime.now());
        wholesalerVehicle.setCreateBy(SecurityContextHolder.getPrincipal().getName());
        this.save(wholesalerVehicle);
        //mark File
        if (null != dto.getFileIds() && !dto.getFileIds().isEmpty()) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(wholesalerVehicle.getId(), dto.getFileIds());
            fileRefClient.saveBatch(files);
        }
        return wholesalerVehicle.getId();
    }

    @Override
    public void update(WholesalerVehicleDTO dto) {
        WholesalerVehicle exit = this.lambdaQuery()
                .eq(WholesalerVehicle::getVehiclePlate, dto.getVehiclePlate())
                .one();
        if (exit != null && !Objects.equals(exit.getId(), dto.getId())) {
            throw new ServiceException("该车牌已存在，请确认");
        }
        WholesalerVehicle wholesalerVehicle = dto.convert();
        mapper.updateById(wholesalerVehicle);
        if (dto.getFileIds() != null && !dto.getFileIds().isEmpty()) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(wholesalerVehicle.getId(), dto.getFileIds());
            fileRefClient.saveBatch(files);
        }
    }

    @Override
    public void remove(String id) {
        mapper.deleteById(id);
        //好像是会自动删除？后续确认一下
        //        fileApi.removeBatch(WholesalerVehicleService.WHOLESALER_VEHICLE, id);
    }

    @Override
    public List<ErrorExcelDTO> importData(String projectId, List<WholesalerVehicleImportExcelDTO> dtos) {
        List<ErrorExcelDTO> errorExcelDtos = new ArrayList<>();
        int i = 2;
        for (WholesalerVehicleImportExcelDTO dto : dtos) {
            WholesalerVehicle vehicle = new WholesalerVehicle();
            vehicle.setProjectId(projectId);
            vehicle.setVehiclePlate(dto.getVehiclePlate());
            vehicle.setColor(dto.getColor());
            vehicle.setOwner(dto.getOwner());
            vehicle.setMobile(dto.getMobile());
            vehicle.setStallName(dto.getStallName());
            // 字典查找车辆类型的code
            DataDictionaryaramParam param = new DataDictionaryaramParam();
            param.setItemCode(VEHICLE_TYPE);
            List<DataDictionaryDTO> dictDataDTOS = dataDictionaryClient.getDataDictionaryByItem(param);
            DataDictionaryDTO dictDataDTO = dictDataDTOS.stream().filter(d -> d.getName().equals(dto.getVehicleType())).findAny()
                    .orElse(null);
            vehicle.setVehicleType(dictDataDTO.getCode());
            try {
                Space space = spaceService.lambdaQuery().eq(Space::getName, dto.getStallName()).one();
                vehicle.setStallId(space.getId());
                vehicle.setCompanyId(space.getCompanyId());
                vehicle.setCompanyName(space.getCompanyName());
            } catch (Exception e) {
                errorExcelDtos.add(new ErrorExcelDTO(i, "档位名称", "档位名称有误"));
            }
            try {
                // 如果某个车牌的车辆在该档位下已经备案了，就修改，没有就新增
                WholesalerVehicle wholesalerVehicle = mapper.selectOne(Wrappers.<WholesalerVehicle>lambdaQuery()
                                                                               .eq(WholesalerVehicle::getProjectId, projectId)
                                                                               .eq(WholesalerVehicle::getStallId, vehicle.getStallId())
                                                                               .eq(WholesalerVehicle::getVehiclePlate, vehicle.getVehiclePlate())
                                                                      );
                if (wholesalerVehicle == null) {
                    mapper.insert(vehicle);
                } else {
                    vehicle.setId(wholesalerVehicle.getId());
                    mapper.updateById(vehicle);
                }
            } catch (Exception e) {

            }
            //i作为行数的标记
            i++;
        }
        return errorExcelDtos;
    }

    @Override
    public List<WholeSpaceDTO> getWholesalerStalls(String projectId) {
        return wholesalerService.getWholesalerStalls(projectId);
    }

}
