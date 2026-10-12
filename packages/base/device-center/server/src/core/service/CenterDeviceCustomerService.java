package com.guhecloud.cube.base.device.center.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.model.BizTagRef;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.TagTypeEnum;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBindTagsCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagBatchBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CustomerTagPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.CustomerTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CenterDeviceCustomerService {

    @Resource
    private CenterBizTagMapper tagMapper;
    @Resource
    private CenterBizTagRefMapper tagRefMapper;
    @Resource
    private CenterBizDeviceMapper centerBizDeviceMapper;

    public void createTagIfNeed(CustomerTagParam createTagCmd) {
        BizTag bizTag = new BizTag();
        Long count = tagMapper.selectCount(Wrappers.<BizTag>lambdaQuery()
                                                   .likeLeft(BizTag::getTagCode, Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE)
                                                   .eq(BizTag::getTagName, createTagCmd.getTagName())
                                                   .ne(createTagCmd.getId() != null, BizTag::getId, createTagCmd.getId()));
        if (count > 0) {
            throw new ServiceException("标签名称不能重复");
        }
        if (ObjectUtils.isNotNull(createTagCmd.getId())) {
            tagMapper.updateTageName(createTagCmd);
        } else {
            bizTag.setTagCode(Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE.concat(".").concat(Long.toString(IdUtils.next())));
            bizTag.setTagName(createTagCmd.getTagName());
            bizTag.setDescription(createTagCmd.getDescription());
            tagMapper.insert(bizTag);
        }
    }

    public void editStatus(String tagId, String status) {
        long id = Long.parseLong(tagId);
        int enable = Integer.parseInt(status);
        tagMapper.updateEnableState(id, enable);
    }

    public Pagination<CustomerTagVO> customerTagPage(String type, CustomerTagPageParam param) {
        String tagCodePrefix;
        TagTypeEnum typeEnum = TagTypeEnum.valueOf(type);
        switch (typeEnum) {
            case CUSTOMER:
                tagCodePrefix = Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE;
                break;
            case SYSTEM:
                tagCodePrefix = Const.BUILTIN_USAGE_TAG_PREFIX;
                break;
            default:
                throw new RuntimeException("不支持的标签类型");
        }
        Page<CustomerTagVO> page = tagMapper.customerTagPage(PagingUtils.convert(param), param, tagCodePrefix);
        setChooseState(page, param.getDeviceId());
        return PagingUtils.convert(page);
    }

    private void setChooseState(Page<CustomerTagVO> page, Long deviceId) {
        if (ObjectUtils.isNotNull(deviceId)) {
            Set<Long> tagIds = tagRefMapper.queryDeviceTagId(deviceId);
            page.getRecords().forEach(vo -> {
                vo.setWhetherBindDevice(tagIds.contains(vo.getId()));
            });
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(String id) {
        long tagId = Long.parseLong(id);
        BizTag bizTag = tagMapper.selectById(tagId);
        Assert.isPermissionDenied(!bizTag.getTagCode().startsWith(Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE), "非自定义标签不可删除");
        tagMapper.realDeleteById(tagId);
        tagRefMapper.realDeleteByTagId(tagId);
    }

    public List<CustomerTagVO> deviceTagList(String deviceId) {
        Long devId = Long.parseLong(deviceId);
        return tagMapper.deviceTagList(devId, Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deviceBindTag(DeviceBindTagsCmd bindTagCmd) {
        Long deviceId = bindTagCmd.getDeviceId();
        com.guhecloud.cube.iot.mgt.comm.util.Assert.notNull(deviceId, "请先选择一个设备");
        //先删除,再绑定
        List<Long> tagIds = tagRefMapper.getTagRefIds(deviceId, Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE);
        if (ObjectUtils.isNotEmpty(tagIds)) {
            tagRefMapper.deleteBatchIds(tagIds);
        }
        List<BizTagRef> bizTagRefs = new ArrayList<>();
        bindTagCmd.getTagIds().forEach(tagId -> {
            bizTagRefs.add(buildBizTagRef(tagId, deviceId));
        });
        if (ObjectUtils.isNotEmpty(bizTagRefs)) {
            this.tagRefMapper.insertBatch(bizTagRefs);
        }
    }

    /**
     * 批量绑定时，仅新增不存在的绑定
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchBindTag(TagBatchBindDeviceCmd bindTagCmd) {
        com.guhecloud.cube.iot.mgt.comm.util.Assert.notNull(bindTagCmd.getDeviceIds(), "至少选择一个设备");
        CopyOnWriteArrayList<BizTagRef> bizTagRefs = Lists.newCopyOnWriteArrayList();
        bindTagCmd.getDeviceIds().forEach(deviceId -> {
            for (Long tagId : bindTagCmd.getTagIds()) {
                Long count = tagRefMapper.selectCount(new LambdaQueryWrapper<BizTagRef>()
                                                              .eq(BizTagRef::getTagId, tagId)
                                                              .eq(BizTagRef::getBusinessKey, deviceId));
                if (count == 0) {
                    bizTagRefs.add(buildBizTagRef(tagId, deviceId));
                }
            }
        });
        if (ObjectUtils.isNotEmpty(bizTagRefs)) {
            this.tagRefMapper.insertBatch(bizTagRefs);
        }
    }

    private BizTagRef buildBizTagRef(Long tagId, Long deviceId) {
        BizTagRef tagRef = new BizTagRef();
        tagRef.setId(IdWorker.getId());
        tagRef.setTagId(tagId);
        tagRef.setBusinessKey(deviceId);
        EntityAuditor.populateAudit(tagRef);
        return tagRef;
    }

    public List<CustomerTagVO> customerTagList() {
        return tagMapper.customerTagList(Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE);
    }

    public List<CustomerTagVO> tagRefTagList(String tagCode, String prefix) {
        Set<String> prefixCodes = new HashSet<>();
        if (StringUtils.isNotBlank(prefix)) {
            prefixCodes.add(prefix);
        }
        return tagRefTagList(tagCode, prefixCodes);
    }

    public List<CustomerTagVO> tagRefTagList(String tagCode, Set<String> prefixCode) {
        Set<Long> deviceIds = tagRefMapper.tagRefDeviceIds(Lists.newArrayList(tagCode));
        if (ObjectUtils.isEmpty(deviceIds)) {
            return new ArrayList<>();
        }
        return tagRefMapper.deviceRefTagList(deviceIds, prefixCode);
    }

    public Set<Long> queryBusinessCustomerTagDevices(String businessTag, List<String> customerTag) {
        Set<Long> businessDevices = tagRefMapper.tagRefDeviceIds(Lists.newArrayList(businessTag));
        Set<Long> customerDevices = tagRefMapper.tagRefDeviceIds(customerTag);
        businessDevices.retainAll(customerDevices);
        return businessDevices;
    }

    public List<DeviceBaseDetailsDTO> getThirdPartyIdListByTagCode(Map<String, Object> param) {
        return centerBizDeviceMapper.getThirdPartyIdListByTagCode(String.valueOf(param.get("tagCode"))).stream().peek(index -> {
            if (ObjectUtils.isEmpty(index.getDeviceStateCode()) || 0 == index.getDeviceStateCode()) {
                index.setDeviceState(Boolean.FALSE);
            } else {
                index.setDeviceState(Boolean.TRUE);
            }
        }).collect(Collectors.toList());
    }
}
