package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.GroupBaseInfoMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseInfo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.GroupControlDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.GroupControlParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.GroupControlQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.PasswordCheckParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseInfoService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseDetailsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupBaseInfoServiceImpl extends ServiceImpl<GroupBaseInfoMapper, GroupBaseInfo> implements GroupBaseInfoService {
    private final GroupBaseInfoMapper groupBaseInfoMapper;
    private final GroupBaseDetailsService groupBaseDetailsService;

    private static final String CONTROL_PASSWORD_AES_SECURITY_KEY = "comguheyuncloud1";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroupControl(GroupControlParam groupControlParam) {

        long count = this.count(Wrappers.<GroupBaseInfo>lambdaQuery()
                                        .eq(GroupBaseInfo::getGroupControlName, groupControlParam.getGroupControlName()));
        Assert.isConflict(count > 0, "名称重复");
        long id = IdWorker.getId();
        GroupBaseInfo groupBaseInfo = ObjectUtils.copying(groupControlParam, GroupBaseInfo.class);
        groupBaseInfo.setId(id);
        ArrayList<GroupBaseDetails> groupBaseDetails = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groupControlParam.getDeviceDetails())) {
            groupBaseInfo.setNum(groupControlParam.getDeviceDetails().size());
            List<GroupControlDetailParam> deviceDetails = groupControlParam.getDeviceDetails();
            for (GroupControlDetailParam deviceDetail : deviceDetails) {
                GroupBaseDetails groupBaseDetail = new GroupBaseDetails();
                groupBaseDetail.setGroupControlId(id);
                groupBaseDetail.setDeviceId(Long.valueOf(deviceDetail.getDeviceId()));
                groupBaseDetail.setAreaName(deviceDetail.getAreaName());
                groupBaseDetail.setDeviceName(deviceDetail.getDeviceName());
                groupBaseDetails.add(groupBaseDetail);
            }
        }
        groupBaseInfoMapper.insert(groupBaseInfo);
        groupBaseDetailsService.saveBatch(groupBaseDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGroupControl(Long id, GroupControlParam groupControlParam) {

        GroupBaseInfo one1 = this.getOne(Wrappers.<GroupBaseInfo>lambdaQuery()
                                                 .eq(GroupBaseInfo::getGroupControlName, groupControlParam.getGroupControlName()));
        if (Objects.nonNull(one1)) {
            Assert.isConflict(!one1.getId().equals(id), "名称重复");
        }

        GroupBaseInfo one = this.getById(id);
        Assert.isNotFound(Objects.isNull(one), "未找到该群组");

        one.setGroupControlName(groupControlParam.getGroupControlName());
        one.setAscription(groupControlParam.getAscription());
        one.setPassword(groupControlParam.getPassword());
        ArrayList<GroupBaseDetails> groupBaseDetails = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groupControlParam.getDeviceDetails())) {
            one.setNum(groupControlParam.getDeviceDetails().size());
            List<GroupControlDetailParam> deviceDetails = groupControlParam.getDeviceDetails();
            for (GroupControlDetailParam deviceDetail : deviceDetails) {
                GroupBaseDetails groupBaseDetail = new GroupBaseDetails();
                groupBaseDetail.setGroupControlId(one.getId());
                groupBaseDetail.setDeviceId(Long.valueOf(deviceDetail.getDeviceId()));
                groupBaseDetail.setDeviceName(deviceDetail.getDeviceName());
                groupBaseDetail.setAreaName(deviceDetail.getAreaName());
                groupBaseDetails.add(groupBaseDetail);
            }
        }

        //删除原有回路
        groupBaseDetailsService.remove(Wrappers.<GroupBaseDetails>lambdaQuery()
                                               .eq(GroupBaseDetails::getGroupControlId, one.getId()));
        groupBaseInfoMapper.updateById(one);
        groupBaseDetailsService.saveBatch(groupBaseDetails);
    }

    @Override
    public void deleteGroupControl(Long id) {
        //删除原有回路
        groupBaseDetailsService.remove(Wrappers.<GroupBaseDetails>lambdaQuery()
                                               .eq(GroupBaseDetails::getGroupControlId, id));
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Pagination<GroupBaseInfoVo> getGroupControlsPage(GroupControlQueryParam param) {
        Page<GroupBaseInfo> page = this.page(PagingUtils.convert(param), Wrappers.<GroupBaseInfo>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), GroupBaseInfo::getGroupControlName, param.getKeyword())
                .orderByDesc(BasicEntity::getUpdateTime));
        Pagination<GroupBaseInfoVo> convert = PagingUtils.convert(page, GroupBaseInfoVo.class);
        convert.getRecords().forEach(s -> {
            //判断是否有锁
            if (StringUtils.isNotEmpty(s.getPassword())) {
                s.setIsLock(true);
            }
            //回路数统计
            long count = groupBaseDetailsService.count(Wrappers.<GroupBaseDetails>lambdaQuery()
                                                               .eq(GroupBaseDetails::getGroupControlId, s.getId()));
            s.setNum(count);
        });
        return convert;
    }

    @Override
    public GroupBaseInfoVo getGroupControlsInfo(Long id) {
        GroupBaseInfo byId = this.getById(id);
        GroupBaseInfoVo groupBaseInfoVo = ObjectUtils.copying(byId, GroupBaseInfoVo.class);
        List<GroupBaseDetails> list = groupBaseDetailsService.list(Wrappers.<GroupBaseDetails>lambdaQuery()
                                                                           .eq(GroupBaseDetails::getGroupControlId, id));
        List<GroupBaseDetailsVo> detailsVos = ObjectUtils.copying(list, GroupBaseDetailsVo.class);
        groupBaseInfoVo.setDeviceDetails(detailsVos);
        return groupBaseInfoVo;
    }

    @Override
    public List<GroupBaseVo> getGroupControlsList(GroupControlQueryParam param) {
        List<GroupBaseInfo> list = this.list(Wrappers.<GroupBaseInfo>lambdaQuery()
                                                     .eq(StringUtils.isNotEmpty(param.getKeyword()), GroupBaseInfo::getGroupControlName, param.getKeyword())
                                                     .eq(StringUtils.isNotEmpty(param.getAscription()), GroupBaseInfo::getAscription, param.getAscription()));

        return ObjectUtils.copying(list, GroupBaseVo.class);
    }

    @Override
    public Boolean groupControlPasswordCheck(PasswordCheckParam param) {
        GroupBaseInfo one = this.getById(param.getGroupControlId());
        String password = one.getPassword();
        String decrypt = AESUtils.decrypt(CONTROL_PASSWORD_AES_SECURITY_KEY, param.getPassword());
        if (decrypt.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

}
