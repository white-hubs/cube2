package com.guhecloud.cube.biz.ouaa.server.service.organization.impl;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.DictionaryEnum;
import com.guhecloud.cube.biz.ouaa.server.dto.login.MSTokenDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.organization.MSOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.organization.MSOrganizationService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-05  14:17
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeishanOrganizationServiceImpl implements MSOrganizationService {
    private final SysDictionaryAdapter dictionaryAdapter;
    private final OrganizationService organizationService;

    @Override
    public void organization() {
        log.info("开始同步环天智慧园区数字大脑部门信息");
        List<Organization> updateOrgList;
        try {
            SysDictionaryDTO tockenDictionary = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DictionaryEnum.MEI_SHAN_URL_ADDRESS.name(), DictionaryEnum.OAUTH_TOCKEN.name());
            String tockenResult = HttpUtil.get(tockenDictionary.getVariable());
            MSTokenDTO tokenDTO = JsonUtils.deserialize(tockenResult, MSTokenDTO.class);
            SysDictionaryDTO dictionaryDTO = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DictionaryEnum.MEI_SHAN_URL_ADDRESS.name(), DictionaryEnum.ORGANIZATION_GET_ALL.name());
            if (ObjectUtils.isEmpty(dictionaryDTO)) {
                return;
            }
            updateOrgList = this.getOrgMap(tokenDTO.getToken(), tokenDTO.getUsercode());
            if (ObjectUtils.isNotEmpty(updateOrgList) && updateOrgList.size() > 0) {
                for (Organization organization : updateOrgList) {
                    try {
                        organizationService.saveOrUpdate(organization);
                    } catch (Exception e) {
                        log.error("同步部门失败:{};{}", organization.getName(), e.getMessage());
                    }
                }
            }
            log.info("同步部门完成,共{}条", updateOrgList.size());
        } catch (Exception e) {
            log.error("同步部门失败：{}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 将三方的部门信息进行组装
     */
    private List<Organization> getOrgMap(String token, String usercode) {
        List<Organization> organizationList = new ArrayList<>();
        SysDictionaryDTO dictionaryDTO = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DictionaryEnum.MEI_SHAN_URL_ADDRESS.name(), DictionaryEnum.ORGANIZATION_GET_ALL.name());
        if (ObjectUtils.isNotEmpty(dictionaryDTO)) {
            String result = HttpUtil.get(dictionaryDTO.getVariable() + "?token=" + token + "&usercode=" + usercode);
            Map<String, Object> map = JsonUtils.deserialize(result, Map.class);
            List<MSOrganizationDTO> organizationDTO = JsonUtils.deserialize(map.get("data"), new TypeReference<List<MSOrganizationDTO>>() {
            });
            for (MSOrganizationDTO organization : organizationDTO) {
                organizationList.add(organization.getOrganization());
            }
        }
        return organizationList;
    }
}
