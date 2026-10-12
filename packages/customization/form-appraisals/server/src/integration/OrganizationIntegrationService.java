
package com.guhecloud.cube.srv.gen.appraisals.server.integration;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizationIntegrationService {
    private final OrganizationClient organizationClient;

    /**
     * 根据部门 ID 获取部门名称
     *
     * @param departmentId 部门 ID
     * @return 部门名称，如果 ID 为 null 或部门不存在则返回空字符串
     */
    public String getDepartmentName(Long departmentId) {
        // 如果部门 ID 为 null，直接返回空字符串
        if (departmentId == null) {
            return "";
        }
        // 调用组织客户端接口，根据 ID 获取组织信息 DTO
        OrganizationDTO dto = organizationClient.getOrganizationsById(departmentId);
        // 如果 DTO 为 null（部门不存在），返回空字符串；否则返回部门名称
        return dto == null ? "" : dto.getName();
    }

    /**
     * 根据部门 ID 列表获取部门名称列表，并保持原有顺序。
     *
     * @param departmentIds 部门 ID 列表
     * @return 部门名称列表
     */
    public List<String> getDepartmentNames(List<Long> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) {
            return List.of();
        }
        List<Long> distinctDepartmentIds = new java.util.ArrayList<>(new LinkedHashSet<>(departmentIds));
        Map<Long, OrganizationDTO> organizationMap = getDepartmentMapByIds(new LinkedHashSet<>(distinctDepartmentIds));
        Map<Long, String> departmentNameMap = new LinkedHashMap<>();
        for (Map.Entry<Long, OrganizationDTO> entry : organizationMap.entrySet()) {
            if (entry.getValue() != null) {
                departmentNameMap.put(entry.getKey(), entry.getValue().getName());
            }
        }
        return distinctDepartmentIds.stream().map(id -> departmentNameMap.getOrDefault(id, "")).toList();
    }

    public Map<Long, OrganizationDTO> getDepartmentMapByIds(Set<Long> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, OrganizationDTO> organizationMap = organizationClient.postOrganizationsMapByIds(
                OrganizationQueryParam.builder().ids(departmentIds).build()
        );
        return organizationMap == null ? Map.of() : organizationMap;
    }
}
