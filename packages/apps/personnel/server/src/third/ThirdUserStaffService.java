package com.guhecloud.cube.srv.gen.personnel.management.server.third;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.server.third.dao.ThirdUserStaffMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.third.job.dto.ThirdUserDTO;
import com.guhecloud.cube.srv.gen.personnel.management.server.third.po.ThirdUserStaffPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author bq
 * @date 2023/03/22
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdUserStaffService extends ServiceImpl<ThirdUserStaffMapper, ThirdUserStaffPO> {

    private final StaffQueryService staffQueryService;
    private final ThirdUserStaffMapper thirdUserStaffMapper;
    private final RestTemplate restTemplate;

    public Long getStaffByThirdUserId(String thirdUserId) {
        ThirdUserStaffPO thirdUserStaffPO = thirdUserStaffMapper.selectOne(Wrappers.<ThirdUserStaffPO>lambdaQuery()
                                                                                   .eq(ThirdUserStaffPO::getThirdUserId, thirdUserId)
                                                                                   .eq(ThirdUserStaffPO::getTenant, SessionContextHolder
                                                                                           .getRequester().getTenant())
                                                                          );
        if (Objects.isNull(thirdUserStaffPO)) {
            // 如果关联表中无此用户的关联信息，直接查询第三方信息存起来
            ThirdUserDTO thirdUserDTO = this.getUserByThird(thirdUserId);
            if (Objects.nonNull(thirdUserDTO)) {
                List<Long> staffIds = this.saveOrUpdate(thirdUserDTO);
                return CollectionUtils.isEmpty(staffIds) ? null : staffIds.get(0);
            }
            return null;
        }
        return thirdUserStaffPO.getStaffId();
    }

    // 获取成农批用户数据
    public void collectCnpUsers(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer 80z74cfzXhv1NxYCtPiPiseB9OpoSYMljBOYo2jN3rUALOFrl5T1mPCky+V/6tPmNKs+4esHLBDjY4A3AE4RFQeqhsrdes3gJoD/DUYZ2fcQqb1E4VZl8cF5aAzkwVM4qmH8S/C+CAB7Nr5JCEM80M/s7Bc6I5gJWyLnsfGktOp7YJL93iXm0qXVpoluBXO1sCUdWP0SE+jgPk+QlOOFjw==");
        HttpEntity<?> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        log.info("获取成农批用户列表{}", jsonObject);
        JSONArray datas = jsonObject.getJSONArray("data");
        SessionContextHolder.setRequester(Requester.builder().tenant(null).build());
        datas.forEach(u -> {
            ThirdUserDTO userDTO = ObjectUtils.copying(u, ThirdUserDTO.class);
            if (Objects.nonNull(userDTO)) {
                this.saveOrUpdate(userDTO);
            }
        });
    }

    public List<Long> saveOrUpdate(ThirdUserDTO userDTO) {
        List<Long> result = new ArrayList<>();
        List<StaffPO> staffPOS = staffQueryService.lambdaQuery().eq(StaffPO::getMobile, userDTO.getPhoneNum())
                .ne(StaffPO::getStatus, StaffStatusEnum.LEAVE_OFFICE.name()).list();
        if (!CollectionUtils.isEmpty(staffPOS)) {
            staffPOS.forEach(staffPO -> {
                ThirdUserStaffPO thirdUserStaffPO = new ThirdUserStaffPO();
                thirdUserStaffPO.setStaffId(staffPO.getId());
                thirdUserStaffPO.setMobile(staffPO.getMobile());
                thirdUserStaffPO.setThirdUserId(String.valueOf(userDTO.getId()));
                thirdUserStaffPO.setTenant(staffPO.getTenant());
                ThirdUserStaffPO old = thirdUserStaffMapper.selectOne(Wrappers.<ThirdUserStaffPO>lambdaQuery()
                                                                              .eq(ThirdUserStaffPO::getMobile, staffPO.getMobile())
                                                                              .eq(ThirdUserStaffPO::getTenant, staffPO.getTenant()));
                if (Objects.isNull(old)) {
                    thirdUserStaffMapper.insert(thirdUserStaffPO);
                } else {
                    thirdUserStaffPO.setId(old.getId());
                    thirdUserStaffMapper.updateById(thirdUserStaffPO);
                }
                result.add(staffPO.getId());
            });
        }
        return result;
    }

    private ThirdUserDTO getUserByThird(String userId) {
        String url = "http://cap.cdhjx.cn:9087/api/user/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer 80z74cfzXhv1NxYCtPiPiseB9OpoSYMljBOYo2jN3rUALOFrl5T1mPCky+V/6tPmNKs+4esHLBDjY4A3AE4RFQeqhsrdes3gJoD/DUYZ2fcQqb1E4VZl8cF5aAzkwVM4qmH8S/C+CAB7Nr5JCEM80M/s7Bc6I5gJWyLnsfGktOp7YJL93iXm0qXVpoluBXO1sCUdWP0SE+jgPk+QlOOFjw==");
        HttpEntity<?> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        return jsonObject.getObject("data", ThirdUserDTO.class);
    }

}
