package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerNoticeDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerNoticeSupDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerNotice;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerNoticeMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerNoticeService;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 商户相关功能通知市场方（目前合同、认证） 服务实现类
 * </p>
 *
 * @author bq
 * @since 2022-03-09
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerNoticeServiceImpl extends ServiceImpl<WholesalerNoticeMapper, WholesalerNotice> implements WholesalerNoticeService {

//    private final SysUserApi userApi;

    @Override
    public WholesalerNoticeSupDTO getList(String projectId, String businessType) {
        WholesalerNoticeSupDTO supDTO = new WholesalerNoticeSupDTO();
        List<WholesalerNotice> notices = this.lambdaQuery().eq(WholesalerNotice::getProjectId, projectId).eq(WholesalerNotice::getBusinessType, businessType).list();
        List<WholesalerNoticeDTO> dtos = new ArrayList<>();
        notices.forEach(notice -> {
            WholesalerNoticeDTO dto = new WholesalerNoticeDTO(notice);
            dto.setUserIdList(this.getListByStr(dto.getUserIds()));
            dto.setDeptIdList(this.getListByStr(dto.getDeptIds()));
            dto.setPostIdList(this.getListByStr(dto.getPostIds()));
            dtos.add(dto);
        });
        supDTO.setBusinessType(businessType);
        supDTO.setProjectId(projectId);
        supDTO.setNoticeDTOS(dtos);
        return supDTO;
    }

    @Override
    public void save(WholesalerNoticeSupDTO supDTO) {
        // 根据业务类型先删除后添加,一个都不设置的时候也要传一个businessType
        this.remove(Wrappers.<WholesalerNotice>lambdaQuery().eq(WholesalerNotice::getBusinessType, supDTO.getBusinessType()));
        List<WholesalerNoticeDTO> dtos = supDTO.getNoticeDTOS();
        if (dtos == null || dtos.size() < 1) return;
        dtos.forEach(dto -> {
            if (StringUtils.isEmpty(dto.getNoticeType())) return;
            dto.setProjectId(supDTO.getProjectId());
            dto.setBusinessType(supDTO.getBusinessType());
            dto.setUserIds(this.getStrByList(dto.getUserIdList()));
            dto.setDeptIds(this.getStrByList(dto.getDeptIdList()));
            dto.setPostIds(this.getStrByList(dto.getPostIdList()));
            this.save(dto.convert());
        });
    }

    @Override
    public List<WholesalerNoticeDTO> getNoticeIds(String businessType) {
        List<WholesalerNoticeDTO> dtos = new ArrayList<>();
        List<WholesalerNotice> notices = this.lambdaQuery().eq(WholesalerNotice::getBusinessType, businessType).list();
        notices.forEach(notice -> {
            Set<String> userIds = new HashSet<>();
//            userIds.addAll(this.getListByStr(notice.getUserIds()));
//            userIds.addAll(this.getUserIdsByDeptIds(notice.getProjectId(), this.getListByStr(notice.getDeptIds())));
//            userIds.addAll(this.getUserIdsByPostIds(notice.getProjectId(), this.getListByStr(notice.getPostIds())));
            WholesalerNoticeDTO dto = new WholesalerNoticeDTO(notice);
            dto.setNoticeUserIds(userIds);
            dtos.add(dto);
        });
        return dtos;
    }


    private String getStrByList(List<String> idList) {
        String ids = "";
        if (idList == null || idList.size() < 1) return ids;
        ids = String.join(",", idList);
        return ids;
    }

    private List<String> getListByStr(String ids) {
        if (StringUtils.isEmpty(ids)) return new ArrayList<>();
        return Arrays.asList(ids.split(","));
    }

//    private List<String> getUserIdsByDeptIds(String projectId, List<String> deptIds) {
//        if (deptIds.size() < 1) return new ArrayList<>();
//       return userApi.getUsersByDeptIds(projectId, deptIds).parallelStream().map(UserInfoDTO::getId).collect(Collectors.toList());
//    }

//    private List<String> getUserIdsByPostIds(String projectId, List<String> postIds) {
//        if (postIds.size() < 1) return new ArrayList<>();
//        return userApi.getUsersByPostIds(projectId, postIds).parallelStream().map(UserInfoDTO::getId).collect(Collectors.toList());
//    }

}
