package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyDeviceMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyDevice;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileSceneCodeEnum;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDevice2QueryParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyDeviceService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：应急设备管理服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyDeviceServiceImpl extends ServiceImpl<EmergencyDeviceMapper, EmergencyDevice> implements EmergencyDeviceService {
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyDeviceParam param) {
        EmergencyDevice emergencyDevice = ObjectUtils.copying(param, EmergencyDevice.class);
        emergencyDevice.setId(IdWorker.getId());
        this.baseMapper.insert(emergencyDevice);
        if (StringUtils.isNotBlank(param.getFileIds())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setFileIdStr(param.getFileIds());
            fileRefCreateParam.setBusinessKey(emergencyDevice.getId());
            fileRefCreateParam.setSceneCode(EmergencyFileSceneCodeEnum.EMERGENCY_DEVICE.getCode());
            fileRefClient.refInduce(fileRefCreateParam);
        }
        return emergencyDevice.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyDeviceParam param) {
        EmergencyDevice emergencyDevice = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyDevice, "未查询到相关信息");
        if (!StringUtils.equals(emergencyDevice.getFileIds(), param.getFileIds())) {
            fileRefClient.removeByBusinessKeyAndScene(emergencyDevice.getId(), EmergencyFileSceneCodeEnum.EMERGENCY_DEVICE.getCode());
            if (StringUtils.isNotBlank(param.getFileIds())) {
                FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
                fileRefCreateParam.setFileIdStr(param.getFileIds());
                fileRefCreateParam.setBusinessKey(emergencyDevice.getId());
                fileRefCreateParam.setSceneCode(EmergencyFileSceneCodeEnum.EMERGENCY_DEVICE.getCode());
                fileRefClient.refInduce(fileRefCreateParam);
            }
        }
        ObjectUtils.copying(param, emergencyDevice);
        this.baseMapper.updateById(emergencyDevice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencyDeviceVO getById(Long id) {
        EmergencyDevice emergencyDevice = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyDevice, EmergencyDeviceVO.class);
    }

    @Override
    public Pagination<EmergencyDeviceVO> getPage(EmergencyDevice2QueryParam param) {
        //请自行添加查询条件
        Page<EmergencyDevice> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getName()), EmergencyDevice::getName, param.getName())
                .page(PagingUtils.convert(param));
        Pagination<EmergencyDeviceVO> convert = PagingUtils.convert(page, EmergencyDeviceVO.class);
        if (!CollectionUtils.isEmpty(convert.getRecords())) {
            convert.getRecords().forEach(k -> {
                if (StringUtils.isNotEmpty(k.getFileIds())) {
                    List<Long> fileIds = Arrays.stream(k.getFileIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                    List<FileVO> fileList = fileClient.getByIds(fileIds);
                    if (!CollectionUtils.isEmpty(fileList)) {
                        k.setFilePaths(fileList.stream().map(FileVO::getPath).collect(Collectors.joining(",")));
                    }
                }
            });
        }
        return convert;
    }

    @Override
    public EmergencyDeviceStatVO getEmergencyDeviceStatForUsed() {
        return this.baseMapper.getEmergencyDeviceStatForUsed();
    }
}