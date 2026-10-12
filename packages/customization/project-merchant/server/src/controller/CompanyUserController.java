package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyService;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyUserService;
import com.guhecloud.cube.deliver.project.merchant.service.UserDeliverService;

import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * <p>
 * 商铺-用户关联 前端控制器
 * </p>
 *
 * @author bq
 * @since 2021-01-13
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyUserController {

    private final CompanyUserService companyUserService;
    private final CompanyService companyService;
    private final UserDeliverService userDeliverService;

    @GetMapping("/companies/users/list")
    public List<CompanyUserDTO> getUserList(@RequestParam(name = "stallId", defaultValue = "") String stallId,
                                            @RequestParam(name = "companyId", defaultValue = "") String companyId
                                           ) {
        List<CompanyUser> companyUsers = new ArrayList<>();
        companyUsers = companyUserService.lambdaQuery().eq(CompanyUser::getStallId, stallId).list();
        if (companyUsers.size() == 0) {
            companyUsers = companyUserService.lambdaQuery().eq(CompanyUser::getCompanyId, companyId).list();
            companyUsers = companyUsers.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CompanyUser::getUserId))), ArrayList::new));
        }
        List<CompanyUserDTO> companyUserDTOS = new CompanyUserDTO(companyUsers).list();
        companyUserDTOS.parallelStream().forEach(dto -> {
            UserDeliver userInfoDTO = userDeliverService.getById(dto.getUserId());
            if (userInfoDTO != null) {
                dto.setUsername(userInfoDTO.getUsername());
                dto.setNickname(userInfoDTO.getNickname());
                dto.setGender(userInfoDTO.getGender());
                dto.setMobile(userInfoDTO.getMobile());
            }
        });
        companyUserDTOS.sort(Comparator.comparing(CompanyUserDTO::getOwner).reversed());
        return companyUserDTOS;
    }

    @GetMapping("/companies/all-users")
    public List<CompanyUserDTO> getAllUsersByCompany(@RequestParam("companyId") String companyId) {
        return this.getUserByCompanyOrStall("", companyId).stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CompanyUserDTO::getUserId))), ArrayList::new));
    }

    @GetMapping("/stalls/all-users")
    public List<CompanyUserDTO> getAllUsersByStall(@RequestParam("stallId") String stallId) {
        return this.getUserByCompanyOrStall(stallId, "");
    }

    @GetMapping("/companies/users/query")
    public List<String> getAllUsersBySearch(CompanyUserQueryDTO queryDTO) {
        if (StringUtils.isEmpty(queryDTO.getProjectId())) {
            return new ArrayList<>();
        }
        return companyUserService.queryCompanyUsers(queryDTO);
    }

    @GetMapping("/companies/users/{id}")
    public boolean getOwner(@PathVariable("id") String id, @RequestParam(name = "companyId", defaultValue = "") String companyId,
                            @RequestParam(name = "stallId", defaultValue = "") String stallId
                           ) {
        // TODO 兼容没有绑档位的情况
        CompanyUser companyUser = null;
        boolean result = false;
        if (StringUtils.isNotEmpty(stallId)) {
            companyUser = companyUserService.lambdaQuery()
                    .eq(CompanyUser::getStallId, stallId)
                    .eq(CompanyUser::getUserId, id)
                    .one();
        }
        if (companyUser == null) {
            List<CompanyUser> companyUsers = companyUserService.lambdaQuery()
                    .eq(StringUtils.isNotEmpty(companyId), CompanyUser::getCompanyId, companyId)
                    .eq(CompanyUser::getUserId, id)
                    .list();
            result = companyUsers.size() > 0 ? companyUsers.get(0).getOwner() : false;
        } else {
            result = companyUser.getOwner();
        }
        return result;
    }

    @GetMapping("/companies/user/{userId}")
    public List<CompanyDTO> getUserCompany(@PathVariable("userId") String userId,
                                           @RequestParam(name = "projectId", defaultValue = "") String projectId
                                          ) {
        List<CompanyDTO> result = Collections.synchronizedList(new ArrayList<>());
        if (StringUtils.isEmpty(projectId)) {
            return result;
        }
        List<CompanyUser> companyUsers = companyUserService.lambdaQuery().eq(CompanyUser::getUserId, userId).list();
        companyUsers.parallelStream().forEach(companyUser -> {
            Company company = companyService.getById(companyUser.getCompanyId());
            if (company != null && company.getProjectId().equals(projectId)) {
                CompanyDTO dto = new CompanyDTO(company);
                dto.setIsOwner(companyUser.getOwner());
                result.add(dto);
            }
        });
        return result;
    }

    @GetMapping("/company-users")
    public List<CompanyUserDTO> getByUserId(@RequestParam("userId") String userId) {
        return new CompanyUserDTO(companyUserService.lambdaQuery().eq(CompanyUser::getUserId, userId).list()).list();
    }

    @PostMapping("/companies/users/ids")
    public Map<String, List<String>> getCompanyUserIds(
            @RequestBody List<String> companyIds
                                                      ) {
        Map<String, List<String>> result = new HashMap<>();
        companyIds.parallelStream().forEach(companyId -> {
            result.put(companyId, companyUserService.lambdaQuery().eq(CompanyUser::getCompanyId, companyId).list().stream()
                    .map(CompanyUser::getUserId).collect(Collectors.toList()));
        });
        return result;
    }

    @PostMapping("/companies/user-company")
    public Map<String, List<String>> getUserCompanies(
            @RequestBody List<String> userIds
                                                     ) {
        Map<String, List<String>> result = new HashMap<>();
        userIds.forEach(userId -> {
            List<CompanyUser> companyUsers = companyUserService.lambdaQuery().eq(CompanyUser::getUserId, userId).list();
            if (companyUsers.size() > 0) {
                String companyId = companyUsers.get(0).getCompanyId();
                if (result.containsKey(companyId)) {
                    result.get(companyId).add(userId);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(userId);
                    result.put(companyId, list);
                }
            }
        });
        return result;
    }

    @PostMapping("/companies/user-num")
    public int getCompanyUserNum(
            @RequestBody List<String> companyIds
                                ) {
        return (int) companyIds.parallelStream()
                .mapToLong(storeId -> companyUserService.lambdaQuery().eq(CompanyUser::getCompanyId, storeId).list().stream()
                        .map(CompanyUser::getUserId).distinct().count()).sum();
    }

    @PostMapping("/public/companies/user")
    public void addUser(@Valid @RequestBody CompanyUserDTO dto) {
        companyUserService.save(dto);
    }

    @PostMapping("/companies/user")
    public void addUserPc(@Valid @RequestBody CompanyUserDTO dto) {
        companyUserService.save(dto);
    }

    @DeleteMapping("/companies/user")
    public void removeUser(
            LoginUser loginUser,
            @RequestParam(name = "companyId", defaultValue = "") String companyId,
            @RequestParam(name = "stallId", defaultValue = "") String stallId,
            @RequestParam(name = "userId") String userId
                          ) {
        loginUser.setId(SecurityContextHolder.getPrincipal().getId());
        companyUserService.removeUser(companyId, stallId, userId, loginUser.getId().toString());
    }

    private List<CompanyUserDTO> getUserByCompanyOrStall(String stallId, String companyId) {
        List<CompanyUserDTO> userDTOS = new CompanyUserDTO(companyUserService.lambdaQuery()
                                                                   .eq(StringUtils.isNotEmpty(stallId), CompanyUser::getStallId, stallId)
                                                                   .eq(StringUtils.isNotEmpty(companyId), CompanyUser::getCompanyId, companyId)
                                                                   .list()).list();
        userDTOS.parallelStream().forEach(dto -> {
            UserDeliver userInfoDTO = userDeliverService.getById(dto.getUserId());
            dto.setNickname(userInfoDTO == null ? "" : userInfoDTO.getNickname());
        });
        return userDTOS;
    }

    @DeleteMapping("/users/{id}")
    public void remove(@PathVariable("id") String id) {
        userDeliverService.removeById(Long.parseLong(id));
    }
}

