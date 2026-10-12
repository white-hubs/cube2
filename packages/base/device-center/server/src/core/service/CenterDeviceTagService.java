package com.guhecloud.cube.base.device.center.core.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.model.BizTagRef;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBindTagsCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagUnBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.QueryDeviceByAreaParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceRefTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.BUILTIN_USAGE_TAG_PREFIX;

@Slf4j
@Service
public class CenterDeviceTagService {
    @Resource
    private CenterBizTagMapper tagMapper;
    @Resource
    private CenterBizTagRefMapper tagRefMapper;
    @Resource
    private AreaClient areaClient;

    public BizTag createTagIfNeed(CreateTagCmd createTagCmd) {
        /**
         *  检查标签有效,先按最基本的正则匹配格式
         */
        String regex = "^(\\w+)\\.(\\w+)::(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(createTagCmd.getTagCode());
        if (!matcher.matches()) {
            log.error("标签:{} 创建失败，非法格式", createTagCmd.getTagCode());
            return null;
        }
        BizTag bizTag = ensureOneTag(createTagCmd.getTagCode(),
                                     createTagCmd.getTagName(), createTagCmd.getLevel(), createTagCmd.getParentTagCode());
        return bizTag;
    }

    public void unbindTag(TagUnBindDeviceCmd tagUnBindDeviceCmd) {
        BizTag bizTag = tagMapper.selectOne(Wrappers.<BizTag>lambdaQuery().eq(BizTag::getTagCode, tagUnBindDeviceCmd.getTagCode()));
        if (Objects.isNull(bizTag)) {
            return;
        }
        Set<Long> deviceIds = tagUnBindDeviceCmd.getBusinessKeys();
        if (Objects.isNull(deviceIds) || deviceIds.isEmpty()) {
            return;
        }
        tagRefMapper.logicBatchDelete(bizTag.getId(), deviceIds, LocalDateTime.now());
    }

    /**
     * TODO 待优化
     * author：剑秋
     * 微信：17683181184
     * 0、检查标签有效 - 这一步目前没有做
     * 1、ref表数据量大，可以分库分表。
     *      假设10个db，每个db100张表，库号 = hash(设备id+"盐值")%10，表号 = hash(设备id+"盐值")%100
     *      查询时先走redis，再走db
     *      redis使用hash结构 key = "prefix_{deviceId}" field = tagId value = version
     * <p>
     * 进入db后产生binlog，监听binlog将该标签hset到redis中去
     * <p>
     * 2、绑定流量很大？
     * - 接口操作异步化，请求发送到kafka，按照设备id hash到对应的partition ,发送完了返回成功
     * 消费者执行绑定逻辑，消费端控制速度，针对数据库进行限流，不至于打垮数据库
     * <p>
     * - 也可以先写redis，后去消费redis的数据写db，存在的问题是redis的数据可能会过期，消费者又没有消费到
     *
     * @param tagBindDeviceCmd
     */
    public void bindTag(TagBindDeviceCmd tagBindDeviceCmd) {
        BizTag bizTag = tagMapper.selectOne(Wrappers.<BizTag>lambdaQuery().eq(BizTag::getTagCode, tagBindDeviceCmd.getTagCode()));

        Set<Long> deviceIds = tagBindDeviceCmd.getBusinessKeys();
        if (Objects.isNull(deviceIds) || deviceIds.isEmpty()) {
            return;
        }

        this.tagRefMapper.physicalDeleteByTagDeviceIds(bizTag.getId(), tagBindDeviceCmd.getBusinessKeys());
        List<BizTagRef> bizTagRefs = new ArrayList<>(tagBindDeviceCmd.getBusinessKeys().size());
        for (Long deviceId : deviceIds) {
            BizTagRef tagRef = new BizTagRef();
            tagRef.setId(IdWorker.getId());
            tagRef.setTagId(bizTag.getId());
            tagRef.setBusinessKey(deviceId);
            EntityAuditor.populateAudit(tagRef);
            bizTagRefs.add(tagRef);
        }
        this.tagRefMapper.insertBatch(bizTagRefs);
    }

    /**
     * 设备绑定标签
     *
     * @param bindTagCmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deviceBindTag(DeviceBindTagsCmd bindTagCmd) {
        Long deviceId = bindTagCmd.getDeviceId();
        Assert.notNull(deviceId, "请先选择一个设备");
        //先删除,再绑定
        List<Long> tagIds = tagRefMapper.getTagRefIds(deviceId, Const.BUILTIN_USAGE_TAG_PREFIX);
        if (!CollectionUtils.isEmpty(tagIds)) {
            tagRefMapper.deleteBatchIds(tagIds);
        }
        List<BizTagRef> bizTagRefs = new ArrayList<>();
        bindTagCmd.getTagIds().forEach(tagId -> bizTagRefs.add(buildBizTagRef(tagId, deviceId)));
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

    /**
     * 获取预置标签列表
     *
     * @return
     */
    public Set<DeviceTagVO> getBuiltinDeviceTags() {
        List<BizTag> bizTags = this.tagMapper.selectDeviceTagsByCodeLeftLike(BUILTIN_USAGE_TAG_PREFIX);
        if (CollectionUtils.isEmpty(bizTags)) {
            return Collections.emptySet();
        }
        return bizTags.stream().map(bizTag -> {
            DeviceTagVO deviceTagVO = new DeviceTagVO();
            deviceTagVO.setId(bizTag.getId());
            deviceTagVO.setTagCode(bizTag.getTagCode());
            deviceTagVO.setTagName(bizTag.getTagName());
            return deviceTagVO;
        }).collect(Collectors.toSet());
    }

    /**
     * 确保标签存在，且唯一
     *
     * @param tagCode 标签编码
     * @param tagName 标签名称
     *
     * @return 标签
     */
    private BizTag ensureOneTag(String tagCode, String tagName, Integer level, String parentTagCode) {
        // 标签名称不能重复
        Long count = tagMapper.selectCount(Wrappers.<BizTag>lambdaQuery().eq(BizTag::getTagName, tagName));
        if (count > 1) {
            throw new ServiceException("标签名称不能重复");
        }

        List<BizTag> listOfDb = tagMapper.selectList(Wrappers.<BizTag>lambdaQuery().eq(BizTag::getTagCode, tagCode));
        BizTag bizTag;
        if (listOfDb.isEmpty()) {
            bizTag = new BizTag();
            bizTag.setTagCode(tagCode);
            bizTag.setTagName(tagName);
            bizTag.setLevel(level);
            tagMapper.insert(bizTag);
        } else if (listOfDb.size() > 1) {
            throw new IllegalStateException("标签编码重复");
        } else {
            bizTag = listOfDb.get(0);
        }

        return bizTag;
    }

    public List<Long> queryDeviceIdsByTagCode(String tagCode) {
        return tagMapper.queryDeviceIdsByTagCode(tagCode);
    }

    public List<Long> queryDeviceIdsByTagCodeAndArea(String tagCode, QueryDeviceByAreaParam param) {
        if (Objects.nonNull(param.getStartWithAreaId())) {
            AreaDTO areaById = this.areaClient.getAreaById(param.getStartWithAreaId());
            param.setStartWithAreaSeq(Objects.nonNull(areaById) ? areaById.getSequence() : StringUtils.EMPTY_STRING);
        }
        return tagMapper.queryDeviceIdsByTagCodeAndArea(tagCode, param);
    }

    public Map<Long, List<CustomerTagVO>> deviceAllTag(Set<Long> deviceIds) {
        return tagMapper.deviceAllTag(deviceIds).stream().collect(Collectors.toMap(DeviceRefTagVO::getId, DeviceRefTagVO::getTags));
    }

    /**
     * 换取设备的租户信息
     *
     * @param deviceId 设备ID
     *
     * @return 租户ID
     */
    public Integer findTenantIdByDeviceId(Long deviceId) {
        return tagMapper.findTenantIdByDeviceId(deviceId);
    }

    public Integer checkTag(String tagCode) {
        List<BizTag> bizTags = tagMapper.queryByTagCode(tagCode);
        return bizTags.size();
    }
}
