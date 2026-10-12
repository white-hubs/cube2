package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchGroupExtendMapper;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchGroupMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGroupRefer;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchGroup;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchGroupExtend;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGroupModel;
import com.guhecloud.cube.biz.dispatch.server.model.extend.DispatchGroupExtendModel;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchGroupParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchGroupQueryParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceGroupReferService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchGroupService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupTreeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchNodeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.extend.DispatchGroupExtendVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：调度设备-分组服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchGroupServiceImpl extends ServiceImpl<DispatchGroupMapper, DispatchGroup> implements DispatchGroupService {
    private final DispatchGroupMapper dispatchGroupMapper;
    private final DispatchGroupExtendMapper dispatchGroupExtendMapper;
    private final DispatchService dispatchService;
    private final DispatchDeviceGroupReferService dispatchDeviceGroupReferService;
    private final DispatchDeviceService dispatchDeviceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(DispatchGroupParam param) {
        //在第三方平台添加组和成员
        DispatchGroupModel model = this.addGroupAndMemberToThirdPlatform(param);
        //添加组
        DispatchGroup dispatchDeviceGroup = ObjectUtils.copying(param, DispatchGroup.class);
        dispatchDeviceGroup.setId(param.getCode());
        dispatchDeviceGroup.setParentId(StringUtils.isNotBlank(param.getParentId()) ? param.getParentId() : model.getParentCode());
        this.baseMapper.insert(dispatchDeviceGroup);
        //插入组扩展信息
        this.addOrUpdateGroupExtend(dispatchDeviceGroup.getId(), ObjectUtils.copying(param, DispatchGroupExtend.class));
        //添加组与人员的关系
        this.dispatchDeviceGroupReferService.batchAdd(param.getCode(), param.getMembers());
        return dispatchDeviceGroup.getId();
    }

    //添加扩展信息
    private void addOrUpdateGroupExtend(String id, DispatchGroupExtend extend) {
        extend.setGroupId(id);
        extend.setDeleted(Boolean.FALSE);
        extend.setTenant(SessionContextHolder.getRequester().getTenant());
        extend.setCreateBy(SecurityContextHolder.getPrincipal().getAccount());
        extend.setCreateTime(LocalDateTime.now());
        extend.setUpdateBy(SecurityContextHolder.getPrincipal().getAccount());
        extend.setUpdateTime(LocalDateTime.now());
        dispatchGroupExtendMapper.insertOrUpdateGroupExtendBatch(List.of(extend));
    }

    /**
     * 在第三方平台添加组和成员
     *
     * @param param
     */
    private DispatchGroupModel addGroupAndMemberToThirdPlatform(DispatchGroupParam param) {
        //模型转换
        DispatchGroupModel model;
        model = ObjectUtils.copying(param, DispatchGroupExtendModel.class);
        model.setParentCode(param.getParentId());
        DispatchGroupModel addGroup = dispatchService.addGroup(model);
        log.info("第三方平台:添加组：" + addGroup.getCode() + " 组名：" + addGroup.getName());

        if (CollectionUtils.isNotEmpty(param.getMembers())) {
            dispatchService.addDeviceToGroup(model.getCode(), param.getMembers());
            log.info("第三方平台:将设备添加进组：" + addGroup.getCode() + " 成员：" + param.getMembers());
        }

        return addGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, DispatchGroupParam param) {
        DispatchGroup dispatchDeviceGroup = this.baseMapper.selectById(id);
        Assert.isNotFound(dispatchDeviceGroup == null, "未查询到相关信息");
        ObjectUtils.copying(param, dispatchDeviceGroup);
        this.baseMapper.updateById(dispatchDeviceGroup);
        //保存扩展信息
        this.addOrUpdateGroupExtend(id, ObjectUtils.copying(param, DispatchGroupExtend.class));
        //修改第三方调度台数据
        DispatchGroupModel model = ObjectUtils.copying(param, DispatchGroupExtendModel.class);
        model.setParentCode(param.getParentId());
        this.dispatchService.updateGroup(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCode(String code) {
        //删除数据库中创建的组
        this.remove(Wrappers.<DispatchGroup>lambdaQuery().eq(DispatchGroup::getCode, code));
        //删除第三方调度台的组
        //如果组被第三方平台删除，会造成业务平台无法删除组的问题；所以在删除组的实现方法内处理掉【无法找到组】异常
        this.dispatchService.delGroup(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        ids.stream().forEach(id -> this.removeById(id));
    }

    @Override
    public DispatchGroupVO getById(String id) {
        DispatchGroup dispatchDeviceGroup = this.baseMapper.selectById(id);
        DispatchGroupExtendVO groupVO = ObjectUtils.copying(dispatchDeviceGroup, DispatchGroupExtendVO.class);
        //查询扩展信息
        this.withExtend(groupVO);
        return groupVO;
    }

    private void withExtend(DispatchGroupExtendVO groupVO) {
        DispatchGroupExtend extend = dispatchGroupExtendMapper.selectOne(Wrappers.<DispatchGroupExtend>lambdaQuery()
                                                                                 .eq(DispatchGroupExtend::getGroupId, groupVO.getId()));
        if (extend != null) {
            groupVO.setIsRecord(extend.getIsRecord());
            groupVO.setIsVideo(extend.getIsVideo());
            groupVO.setGroupLevel(extend.getGroupLevel());
        }
    }

    @Override
    public Pagination<DispatchGroupVO> getPage(DispatchGroupQueryParam param) {
        //请自行添加查询条件
        Page<DispatchGroup> page = this.lambdaQuery()
                .and(StringUtils.isNotBlank(param.getKeyword()), q ->
                        q.like(DispatchGroup::getName, param.getKeyword())
                                .or()
                                .like(DispatchGroup::getCode, param.getKeyword()))
                .eq(StringUtils.isNotBlank(param.getType()), DispatchGroup::getType, param.getType())
                .orderByDesc(DispatchGroup::getCreateTime)
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, convert(page.getRecords()));
    }

    @Override
    public List<DispatchGroupVO> getList(DispatchGroupQueryParam param) {
        List<DispatchGroup> datas = this.lambdaQuery()
                .and(StringUtils.isNotBlank(param.getKeyword()), q ->
                        q.like(DispatchGroup::getName, param.getKeyword())
                                .or()
                                .like(DispatchGroup::getCode, param.getKeyword()))
                .eq(StringUtils.isNotBlank(param.getType()), DispatchGroup::getType, param.getType())
                .list();
        return convert(datas);
    }

    @Override
    public List<DispatchGroupTreeVO> getListWithDevice(DispatchGroupQueryParam param) {
        return this.dispatchGroupMapper.getGroupWithDevice(param);
    }

    @Override
    public List<DispatchGroupTreeVO> getListWithDeviceDetail(DispatchGroupQueryParam param) {
        List<DispatchGroupTreeVO> groupWithDeviceDetail = this.dispatchGroupMapper.getGroupWithDeviceDetail(param);
        //过滤掉没有在组中的设备
        return groupWithDeviceDetail.stream().filter(d ->
                                                             d.getCategory().equals("group") ||
                                                             groupWithDeviceDetail.stream().filter(s -> d.getParentId().equals(s.getId()))
                                                                     .findAny().orElse(null) != null
                                                    ).collect(Collectors.toList());
    }

    /**
     * 数据转换
     *
     * @param datas
     *
     * @return
     */
    private List<DispatchGroupVO> convert(List<DispatchGroup> datas) {
        return Optional.ofNullable(datas).orElse(Collections.emptyList()).stream()
                .map(g -> {
                    DispatchGroupExtendVO groupVO = ObjectUtils.copying(g, DispatchGroupExtendVO.class);
                    groupVO.setTypeName(dispatchService.getGroupTypeName(g.getType()));
                    //查询组下的设备
                    groupVO.setDevices(dispatchDeviceService.deviceInGroup(g.getId()));
                    //查询组扩展信息
                    this.withExtend(groupVO);
                    return groupVO;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMembersFromGroup(String code, List<String> deviceCodes) {
        if (StringUtils.isNotBlank(code) && CollectionUtils.isNotEmpty(deviceCodes)) {
            DispatchGroup group = this.getOne(Wrappers.<DispatchGroup>lambdaQuery().eq(DispatchGroup::getCode, code));
            Assert.isNotFound(group, "未找到组信息，请确认");
            this.dispatchDeviceGroupReferService.batchDelete(group.getCode(), deviceCodes);
            //从第三方调度台移除成员
            this.dispatchService.delDeviceFromGroup(group.getCode(), deviceCodes);
        }
    }

    @Override
    public void addMembersToGroup(String code, List<String> deviceCodes) {
        if (StringUtils.isNotBlank(code) && CollectionUtils.isNotEmpty(deviceCodes)) {
            DispatchGroup group = this.getOne(Wrappers.<DispatchGroup>lambdaQuery().eq(DispatchGroup::getCode, code));
            Assert.isNotFound(group, "未找到组信息，请确认");
            this.dispatchDeviceGroupReferService.batchAdd(group.getId(), deviceCodes);
            //第三方调度台加入成员
            this.dispatchService.addDeviceToGroup(group.getCode(), deviceCodes);
        }
    }

    @Override
    public void addAndDeleteMembersToGroup(String code, List<String> deviceCodes) {
        //对比数据 找到需要增加和删除的关联数据
        //通过组ID 找到当前组下的成员
        List<String> existDeviceIds = this.dispatchDeviceGroupReferService.list(Wrappers.<DispatchDeviceGroupRefer>lambdaQuery()
                                                                                        .eq(DispatchDeviceGroupRefer::getGroupId, code))
                .stream().map(DispatchDeviceGroupRefer::getDeviceId)
                .toList();
        //需要添加的关系
        List<String> diffAinB = new ArrayList<>(deviceCodes);
        diffAinB.removeAll(existDeviceIds);
        this.addMembersToGroup(code, diffAinB);
        //需要移除的关系
        List<String> diffBinA = new ArrayList<>(existDeviceIds);
        diffBinA.removeAll(deviceCodes);
        this.removeMembersFromGroup(code, diffBinA);
    }

    @Override
    public void obtainThirdPlatformDevice(String code) {
        List<DispatchNodeVO> groups = this.dispatchService.allGroup(code);
        String operator = SecurityContextHolder.getPrincipal(true) != null ? SecurityContextHolder.getPrincipal().getAccount() : "admin";
        List<DispatchGroup> datas = groups.stream().map(d -> buildDispatchGroup(d, operator)).collect(Collectors.toList());
        this.dispatchGroupMapper.insertOrUpdateGroupBatch(datas);
        //删除已经不存在的组
        List<String> ids = groups.stream().map(DispatchNodeVO::getCode).collect(Collectors.toList());
        this.dispatchGroupMapper.delete(Wrappers.<DispatchGroup>lambdaQuery()
                                                .notIn(DispatchGroup::getId, ids));
        //更新扩展信息
        List<DispatchGroupExtend> groupExtendList = groups.stream().map(d -> buildDispatchGroupExtend(d, operator))
                .collect(Collectors.toList());
        this.dispatchGroupExtendMapper.insertOrUpdateGroupExtendBatch(groupExtendList);
    }

    /**
     * 构建调度组
     *
     * @param data
     * @param operator
     *
     * @return
     */
    private DispatchGroup buildDispatchGroup(DispatchNodeVO data, String operator) {
        DispatchGroup group = new DispatchGroup();
        group.setId(data.getCode());
        group.setCode(data.getCode());
        group.setName(data.getName());
        group.setSort(data.getSort());
        group.setParentId(String.join(",", data.getParentCodes()));
        group.setTenant(SessionContextHolder.getRequester().getTenant());
        group.setCreateBy(operator);
        group.setCreateTime(LocalDateTime.now());
        group.setUpdateBy(operator);
        group.setUpdateTime(LocalDateTime.now());
        group.setDeleted(false);
        //数据清洗  由于普天的逻辑已经实现了，这里把普天独立出来
        if (dispatchService instanceof PTDispatchServiceImpl) {
            group.setType(data.getSubType());
        } else {
            group.setType(data.getType());
        }
        return group;
    }

    /**
     * 构建调度组扩展信息
     *
     * @param data
     * @param operator
     *
     * @return
     */
    private DispatchGroupExtend buildDispatchGroupExtend(DispatchNodeVO data, String operator) {
        DispatchGroupExtend groupExtend = ObjectUtils.copying(data, DispatchGroupExtend.class);
        groupExtend.setGroupId(data.getCode());
        groupExtend.setTenant(SessionContextHolder.getRequester().getTenant());
        groupExtend.setCreateBy(operator);
        groupExtend.setCreateTime(LocalDateTime.now());
        groupExtend.setUpdateBy(operator);
        groupExtend.setUpdateTime(LocalDateTime.now());
        groupExtend.setDeleted(false);
        return groupExtend;
    }
}