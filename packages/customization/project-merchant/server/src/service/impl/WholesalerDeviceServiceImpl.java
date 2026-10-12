package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceImportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDevice;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDeviceType;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerDeviceMapper;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerDeviceService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerDeviceTypeService;
import com.guhecloud.cube.deliver.project.merchant.utils.FilesUtil;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商户档案-所有设备（批发商的设备） 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-04
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerDeviceServiceImpl extends ServiceImpl<WholesalerDeviceMapper, WholesalerDevice> implements WholesalerDeviceService {

    private final FileClient fileClient;
    private final SpaceService spaceService;
    private final WholesalerDeviceMapper mapper;
    private final WholesalerDeviceTypeService deviceTypeService;
    private final FileRefClient fileRefClient;

    @Override
    public String save(WholesalerDeviceDTO dto) {
        Space stall = spaceService.lambdaQuery().eq(Space::getId, dto.getStallId()).one();
        if (stall == null) {
            throw new ServiceException("该档位不存在");
        }
        if (dto.getCount() < 1) {
            throw new ServiceException("数量不得小于1");
        }
        List<WholesalerDevice> list = this.lambdaQuery()
                .eq(WholesalerDevice::getTypeId, dto.getTypeId())
                .eq(WholesalerDevice::getStallId, dto.getStallId())
                .list();
        if (list != null && !list.isEmpty()) {
            throw new ServiceException("该档位下已经存在该类型设备，无需重复添加，只需修改数量即可");
        }
        dto.setStallId(dto.getStallId());
        dto.setStallName(stall.getName());
        dto.setCompanyId(stall.getCompanyId());
        dto.setCompanyName(stall.getCompanyName());
        dto.setProjectId(stall.getProjectId());
        this.judgeIsDanger(dto);
        WholesalerDevice device = dto.convert();
        device.setCreateTime(LocalDateTime.now());
        device.setCreateBy(SecurityContextHolder.getPrincipal().getName());
        this.save(device);
        //mark File
        if (null != dto.getFileIds() && !dto.getFileIds().isEmpty()) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(device.getId(), dto.getFileIds());
            fileRefClient.saveBatch(files);
        }
        return device.getId();
    }

    @Override
    public void getDeviceTemplate(HttpServletRequest request, HttpServletResponse response) {
        //构造设备类型为:_name_id
        List<WholesalerDeviceType> types = deviceTypeService.lambdaQuery().list();
        List<String> typeOfName_Id = types.stream().map(t -> MessageFormat.format("_{0}_{1}", t.getName(), t.getId()))
                .collect(Collectors.toList());
        List<WholesalerDeviceImportExcelDTO> dtos = new ArrayList<>();
        ExcelUtils<WholesalerDeviceImportExcelDTO> utils = new ExcelUtils<>(WholesalerDeviceImportExcelDTO.class);
        utils.createWorkbook();
        try {
            XSSFSheet sheet = utils.createSheet(SHEET_NAME, dtos);
            utils.setXSSFValidation(sheet, "deviceType", typeOfName_Id, 1, 1);
            utils.downLoadExcel("商户设备备案模板", request, response);
        } catch (Exception e) {
            throw new ServiceException("下载模板失败");
        }
    }

    @Override
    public List<ErrorExcelDTO> importData(String projectId, List<WholesalerDeviceImportExcelDTO> dtos) {
        List<ErrorExcelDTO> errorExcelDtos = new ArrayList<>();
        int i = 2;
        for (WholesalerDeviceImportExcelDTO dto : dtos) {
            WholesalerDevice device = new WholesalerDevice();
            device.setProjectId(projectId);
            // 解析类型ID
            this.parseType(dto.getDeviceType(), device);

            device.setStallName(dto.getStallName());
            device.setDescription(dto.getDescription());
            // 判断类型是否存在
            if (StringUtils.isEmpty(device.getTypeId())) {
                errorExcelDtos.add(new ErrorExcelDTO(i, "设备类型", "设备类型不存在，请下载最新的模板"));
            }
            try {
                // 判断数量必须是正整数
                int count = Integer.parseInt(dto.getCount());
                device.setCount(count);
            } catch (Exception e) {
                errorExcelDtos.add(new ErrorExcelDTO(i, "设备数量", "设备数量只能是正整数"));
            }
            try {
                Space space = spaceService.lambdaQuery().eq(Space::getName, dto.getStallName())
                        .one();
                device.setStallId(space.getId());
                device.setCompanyId(space.getCompanyId());
                device.setCompanyName(space.getCompanyName());
            } catch (Exception e) {
                errorExcelDtos.add(new ErrorExcelDTO(i, "档位名称", "档位名称有误或不存在"));
            }
            try {
                // 如果该档位下的设备类型已经有了就修改，没有就新增
                WholesalerDevice wholesalerDevice = mapper.selectOne(Wrappers.<WholesalerDevice>lambdaQuery()
                                                                             .eq(WholesalerDevice::getProjectId, projectId)
                                                                             .eq(WholesalerDevice::getTypeId, device.getTypeId())
                                                                             .eq(WholesalerDevice::getStallId, device.getStallId()));
                if (wholesalerDevice == null) {
                    mapper.insert(device);
                } else {
                    device.setId(wholesalerDevice.getId());
                    mapper.updateById(device);
                }
            } catch (Exception e) {

            }
            //i作为行数的标记
            i++;
        }
        if (errorExcelDtos.size() > 0) {
            throw new ServiceException("导入数据异常");
        }
        return errorExcelDtos;
    }

    private void parseType(String deviceType, WholesalerDevice device) {
        String typeId = this.parseIdByName_Id(deviceType);
        WholesalerDeviceType type = deviceTypeService.getById(typeId);
        if (null != type) {
            device.setTypeId(typeId);
            device.setDanger(type.isDanger() ? 1 : 0);
        }
    }

    private String parseIdByName_Id(String name_id) {
        StringBuilder id = new StringBuilder();
        String[] name_ids = name_id.split("_");
        int length = name_ids.length;
        if (length == 3) {
            id.append(name_ids[length - 1]);
        }
        return id.toString();
    }

    @Override
    public void update(WholesalerDeviceDTO dto) {
        //只修改数量和对应的档位
        if (dto.getCount() < 1) {
            throw new ServiceException("数量不得小于1");
        }
        //获取项目ID
        String projectId = this.lambdaQuery().eq(WholesalerDevice::getId, dto.getId()).one().getProjectId();
        if (dto.getStallName() != null && !dto.getStallName().isEmpty()) {
            //根据项目ID和档位名称定为到唯一的档位
            Space space = spaceService.lambdaQuery().eq(Space::getProjectId, projectId)
                    .eq(Space::getName, dto.getStallName())
                    .one();
            if (space == null) {
                throw new ServiceException("没有对应的档位 ，请确认");
            }
            //获取修改的这些东西
            dto.setCompanyId(space.getCompanyId());
            dto.setCompanyName(space.getCompanyName());
            dto.setStallId(space.getId());
        }
        this.judgeIsDanger(dto);
        WholesalerDevice device = dto.convert();
        mapper.updateById(device);
        if (dto.getFileIds() != null && !dto.getFileIds().isEmpty()) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(device.getId(), dto.getFileIds());
            fileRefClient.saveBatch(files);
        }
    }

    @Override
    public void remove(String id) {
        mapper.deleteById(id);
        //好像是会自动删除？后续确认一下
        //        fileApi.removeBatch(WHOLESALER_DEVICE, id);
    }

    private void judgeIsDanger(WholesalerDeviceDTO dto) {
        WholesalerDeviceType deviceType = deviceTypeService.getById(dto.getTypeId());
        if (deviceType != null) {
            dto.setDanger(deviceType.isDanger() ? 1 : 0);
        }
    }

}
