package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationModifyParam;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 组织部门接口
 *
 * @author lzy
 * @date 2021/9/13
 */
@FeignClient(name = "ouaa")
public interface OrganizationClient {
    /**
     * 查询组织机构列表
     *
     * @param param 查询参数
     *
     * @return 应用列表
     */
    @GetMapping("/authenticated/organizations/list")
    List<OrganizationDTO> getOrganizations(@SpringQueryMap @Valid OrganizationQueryParam param);

    /**
     * 通过机构IDS查询机构列表
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/authenticated/organizations/ids/list")
    List<OrganizationDTO> getOrganizationsByIds(@RequestParam(value = "ids") List<Long> ids);

    /**
     * 获取机构信息
     *
     * @param depts 部门集合
     *
     * @return 机构集合
     */
    @GetMapping("/authenticated/organizations/depts/list")
    List<OrganizationDTO> listOrganizationByDepts(@RequestParam("depts") Collection<String> depts);

    /**
     * 通过机构ID查询机构
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/authenticated/organizations/{id}")
    OrganizationDTO getOrganizationsById(@PathVariable("id") Long id);

    /**
     * 查询当前节点及其所有父节点
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/authenticated/organizations/parent/list/{id}")
    LinkedList<OrganizationDTO> getParentListById(@PathVariable("id") Long id);

    /**
     * 通过名称查询部门
     *
     * @param name
     *
     * @return
     */
    @GetMapping("/authenticated/organizations/name")
    OrganizationDTO getOrganizationsByName(@RequestParam(value = "name") String name);

    @PostMapping("/authenticated/organizations/map/ids")
    Map<Long, OrganizationDTO> postOrganizationsMapByIds(@RequestBody OrganizationQueryParam param);

    @PostMapping("/authenticated/organizations/list/ids")
    List<OrganizationDTO> getOrganizationsListByIds(@RequestBody OrganizationQueryParam param);

    /**
     * 新增组织机构
     * R
     *
     * @param param 新增参数
     */
    @PostMapping("/organization")
    void addOrganization(@RequestBody @Valid OrganizationModifyParam param);

    /**
     * 修改组织机构
     *
     * @param id    组织机构主键
     * @param param 更新参数
     */
    @PutMapping("/organization/{id}")
    void updateOrganization(@PathVariable("id") @Min(value = 1, message = "组织机构主键不能小于1") Long id,
                            @RequestBody @Valid OrganizationModifyParam param
                           );

    /**
     * 删除组织机构
     *
     * @param id 组织机构主键
     */
    @DeleteMapping("/organization/{id}")
    void deleteOrganization(@PathVariable("id") Long id);
}
