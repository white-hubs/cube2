package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.Wholesaler;
import com.guhecloud.cube.deliver.project.merchant.enums.UserTypeEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyUserMapper;
import com.guhecloud.cube.deliver.project.merchant.service.*;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.guhecloud.cube.deliver.project.merchant.entity.*;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerServiceImpl extends ServiceImpl<WholesalerMapper, Wholesaler> implements WholesalerService {

    //    private final FileApi fileApi;
    private final SpaceService spaceService;
    private final CompanyUserService companyUserService;
    private final UserDeliverService userDeliverService;
    private final CompanyUserMapper mapper;
    private final CompanyMapper companyMapper;
    private final MerchantCompanyService merchantCompanyService;
    private final MerchantAreaService merchantAreaService;

    //    private final AreaDeliver
    private final UserClient userClient;

    @Override
    public WholesalerDTO getWholesaler(String id) {
        Wholesaler wholesaler = this.lambdaQuery().eq(Wholesaler::getId, id).one();
        WholesalerDTO dto = new WholesalerDTO(wholesaler);
        this.extend(dto);
        return dto;
    }

    @Override
    public List<CompanyUserDTO> getWholesalerUsers(WholesalerUserQueryDTO queryDto) {
        //首先获取company-user表中的所有记录
        List<CompanyUserDTO> users = this.setUserInfor(new CompanyUserDTO(companyUserService.lambdaQuery()
                                                                                  .eq(StrUtil.isNotBlank(queryDto.getProjectId()), CompanyUser::getProjectId, queryDto.getProjectId())
                                                                                  .eq(StrUtil.isNotBlank(queryDto.getCompanyId()), CompanyUser::getCompanyId, queryDto.getCompanyId())
                                                                                  .eq(StrUtil.isNotBlank(queryDto.getStallId()), CompanyUser::getStallId, queryDto.getStallId())
                                                                                  .list()).list());
        //根据筛选条件进行筛选，
        return this.filterUser(users, queryDto);
    }

    @Override
    public String save(CompanyUserDTO dto, LoginUser user) {
        //需要先将user insert到user表中才能获取到userId
        dto.setCreateBy(user.getUsername());
        String userId = this.saveUser(dto, user.getUsername());
        CompanyUser companyUser = new CompanyUser();
        Space space = spaceService.lambdaQuery().eq(Space::getId, dto.getStallId()).one();
        companyUser.setCompanyId(space.getCompanyId());
        companyUser.setStallId(dto.getStallId());
        companyUser.setOwner(dto.getOwner());
        companyUser.setUserId(userId);
        companyUser.setProjectId(dto.getProjectId());

        //新增对应关系到company_user表中
        mapper.insert(companyUser);
        return userId;
    }

    @Override
    public String saveUserByApp(String stallId, CompanyUserDTO dto, LoginUser user) {
        Space space = spaceService.lambdaQuery().eq(Space::getId, stallId).one();
        if (space == null) {
            throw new ServiceException("没有对应的档位，错误档位ID" + stallId);
        }
        String userId = saveUser(dto, user.getUsername());
        CompanyUser companyUser = new CompanyUser();
        companyUser.setStallId(stallId);
        companyUser.setProjectId(space.getProjectId());
        companyUser.setUserId(userId);
        companyUser.setCompanyId(space.getCompanyId());
        companyUser.setOwner(false);
        mapper.insert(companyUser);
        return userId;
    }

    @Override
    public void update(CompanyUserDTO dto) {
        if (StrUtil.isNotBlank(dto.getStallId())) {
            // 首先看companyUser表中是否有该档位的关系
            CompanyUser user = companyUserService.lambdaQuery()
                    .eq(CompanyUser::getCompanyId, dto.getCompanyId())
                    .eq(CompanyUser::getStallId, dto.getStallId())
                    .eq(CompanyUser::getUserId, dto.getUserId())
                    .one();
            if (user != null) {
                Space space = spaceService.getById(dto.getStallId());
                if (space == null) {
                    throw new ServiceException("该档位不存在，请确认");
                }
                // 如果该用户想成为档主，则先查看档位是否已经绑定档主
                if (dto.getOwner() != null && dto.getOwner()) {
                    CompanyUser owner = companyUserService.lambdaQuery()
                            .eq(CompanyUser::getStallId, dto.getStallId())
                            .eq(CompanyUser::getOwner, true)
                            .one();
                    if (owner != null && !owner.getUserId().equals(user.getUserId())) {
                        throw new ServiceException("该档位已经绑定了档主!");
                    }
                }
                // 修改
                user.setStallId(dto.getStallId());
                user.setCompanyId(dto.getCompanyId());
                user.setOwner(dto.getOwner());
                companyUserService.updateById(user);
            } else {
                companyUserService.save(dto.convert());
            }

        }
    }

    @Override
    public void remove(String id) {
        //暂时不删除用户，只解除商户和人员之间的联系,让界面不会显示就行
        mapper.deleteById(id);
    }

    @Override
    public List<WholesalerShortDTO> getWholesalerList(String projectId) {
        //获取中间表中的company_id
        List<Wholesaler> wholesalerList = this.lambdaQuery().eq(Wholesaler::getProjectId, projectId).list();
        if (wholesalerList.isEmpty()) {
            throw new ServiceException("该项目ID：" + projectId + " 下没有任何商户，请确认。");
        }
        return new WholesalerShortDTO(wholesalerList).list();
    }

    @Override
    public List<WholeSpaceDTO> getWholesalerStalls(String projectId) {
        Map<String, String> areaIdNames = this.getAreaIdNameMap(projectId);
        List<Space> spaceList = spaceService.lambdaQuery().eq(Space::getProjectId, projectId).list();
        List<WholeSpaceDTO> dtos = new ArrayList<>();
        spaceList.forEach(space -> {
            WholeSpaceDTO dto = new WholeSpaceDTO(space);
            CompanyUser user = companyUserService.lambdaQuery().eq(CompanyUser::getOwner, 1)
                    .eq(CompanyUser::getStallId, space.getId()).one();
            if (user != null) {
                UserDeliver userInfoDTO = userDeliverService.getById(user.getUserId());
                AreaDeliver area1 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId1()).one();
                AreaDeliver area2 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId2()).one();
                AreaDeliver area3 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId3()).one();
                if (ObjectUtil.isNotEmpty(areaIdNames)) {
                    dto.setArea(Arrays.asList(areaIdNames.getOrDefault(area1.getId(), ""),
                                              areaIdNames.getOrDefault(area2 == null ? "" : area2.getId(), ""),
                                              areaIdNames.getOrDefault(area3 == null ? "" : area3.getId(), "")));
                }
                if (ObjectUtil.isNotEmpty(userInfoDTO)) {
                    dto.setMobile(userInfoDTO.getMobile());
                    dto.setNickname(userInfoDTO.getNickname());
                    dto.setRentStatus(1);
                } else {
                    dto.setRentStatus(0);
                }
            } else {
                dto.setRentStatus(0);
            }

            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public WholeSpaceDTO getWholesalerStalls(String projectId, Long id) {
        //        Map<String, String> areaIdNames = this.getAreaIdNameMap(projectId);
        List<Space> spaceList = spaceService.lambdaQuery().eq(Space::getProjectId, projectId).eq(Space::getId, id).list();
        List<WholeSpaceDTO> dtos = new ArrayList<>();
        spaceList.forEach(space -> {
            WholeSpaceDTO dto = new WholeSpaceDTO(space);
            CompanyUser user = companyUserService.lambdaQuery().eq(CompanyUser::getOwner, 1)
                    .eq(CompanyUser::getStallId, space.getId()).one();
            if (user != null) {
                UserDeliver userInfoDTO = userDeliverService.getById(user.getUserId());
                AreaDeliver area1 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId1()).one();
                AreaDeliver area2 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId2()).one();
                AreaDeliver area3 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId3()).one();
                List<String> arrayList = new ArrayList<>();
                if (ObjectUtil.isNotEmpty(area1)) {
                    arrayList.add(area1.getName());
                }
                if (ObjectUtil.isNotEmpty(area2)) {
                    arrayList.add(area1.getName());
                }
                if (ObjectUtil.isNotEmpty(area3)) {
                    arrayList.add(area1.getName());
                }
                dto.setArea(arrayList);
                if (ObjectUtil.isNotEmpty(userInfoDTO)) {
                    dto.setMobile(userInfoDTO.getMobile());
                    dto.setNickname(userInfoDTO.getNickname());
                    dto.setRentStatus(1);
                } else {
                    dto.setRentStatus(0);
                }
            } else {
                dto.setRentStatus(0);
            }

            dtos.add(dto);
        });
        return dtos.get(0);
    }

    @Override
    public List<WholesalerListDTO> getWholesalerPage(WholesalerQueryDTO queryDTO) {
        //获取所有记录
        List<Wholesaler> list = this.lambdaQuery()
                .eq(Wholesaler::getProjectId, queryDTO.getProjectId())
                .and(StrUtil.isNotBlank(queryDTO.getSearch()), q -> q
                        .like(Wholesaler::getName, queryDTO.getSearch())
                        .or()
                        .like(Wholesaler::getOwner, queryDTO.getSearch())
                        .or()
                        .like(Wholesaler::getMobile, queryDTO.getSearch())
                        .or()
                        .like(Wholesaler::getCode, queryDTO.getSearch())
                    )
                .list();
        List<Space> spaceList = spaceService.lambdaQuery().eq(Space::getProjectId, queryDTO.getProjectId()).list();
        List<WholesalerListDTO> dtos = new WholesalerListDTO(list).list();
        dtos.forEach(dto -> dto.setStallCount((int) spaceList.stream().filter(space -> space.getCompanyId().equals(dto.getId())).count()));
        return dtos;
    }

    @Override
    public String saveWholesaler(CompanyDTO dto) {
        long count = merchantCompanyService.lambdaQuery().eq(Company::getName, dto.getName()).count();
        if (count > 0) {
            throw new ServiceException("该商户名称已经存在");
        }
        String companyId = merchantCompanyService.save(dto);
        //如果传了档位，就将对应的档位进行标记
        if (!dto.getSpaceIds().isEmpty()) {
            dto.getSpaceIds().forEach(id -> {
                spaceService.lambdaUpdate().eq(Space::getId, id)
                        .set(Space::getCompanyId, companyId)
                        .set(Space::getCompanyName, dto.getName())
                        .update();

            });
        }
        return companyId;
    }

    @Override
    public WholesalerListDTO getWholesalerByStallId(String projectId, String stallId) {
        Space space = spaceService.lambdaQuery().eq(Space::getProjectId, projectId)
                .eq(Space::getId, stallId).one();
        WholesalerListDTO dto = new WholesalerListDTO();
        dto.setName(space.getName());
        dto.setId(space.getCompanyId());
        return dto;
    }

    /**
     * 商户详情（包括人员）
     *
     * @param dto 商户DTO
     */
    private void extend(WholesalerDTO dto) {
        // 查询商铺关联的空间
        dto.setSpaces(new SpaceDTO(spaceService.lambdaQuery().eq(Space::getCompanyId, dto.getId()).list()).list());
        // 查询关联的安全协议，安全培训，消火栓
        getMarkFile(dto, MerchantCompanyService.BUSINESS_TYPE_SECURITY);
        getMarkFile(dto, MerchantCompanyService.BUSINESS_TYPE_PHOTO);
        getMarkFile(dto, MerchantCompanyService.BUSINESS_TYPE_LOGO);
        //查询关联的人员列表(1:现在company_user表中根据companyID，找到对应的userID)
        List<CompanyUserDTO> wholesalerUsers = new CompanyUserDTO(companyUserService.lambdaQuery()
                                                                          .eq(CompanyUser::getCompanyId, dto.getId()).list()).list();
        dto.setCompanyUserDtos(setUserInfor(wholesalerUsers));
    }

    /**
     * 获取评论DTO关联的文件
     * 暂时包括：安全协议，安全培训，消火栓
     */
    private void getMarkFile(WholesalerDTO dto, String type) {
        List<FileVO> FileVOs;
        switch (type) {
            case MerchantCompanyService.BUSINESS_TYPE_SECURITY:
                //                FileVOs = fileApi.getList(CompanyService.BUSINESS_TYPE_SECURITY, dto.getId());
                //                if (FileVOs != null) {
                //                    dto.setSecurityFileIds(FileVOs.stream().map(FileVO::getId).collect(Collectors.toList()));
                //                    dto.setSecurityFileVOs(FileVOs);
                //                }
                break;
            case MerchantCompanyService.BUSINESS_TYPE_PHOTO:
                //                FileVOs = fileApi.getList(CompanyService.BUSINESS_TYPE_PHOTO, dto.getId());
                //                if (FileVOs != null) {
                //                    dto.setTrainingPhotoFileIds(FileVOs.stream().map(FileVO::getId).collect(Collectors.toList()));
                //                    dto.setTrainingPhotoFileVOs(FileVOs);
                //                }
                break;
            case MerchantCompanyService.BUSINESS_TYPE_LOGO:
                //                FileVOs = fileApi.getList(CompanyService.BUSINESS_TYPE_LOGO, dto.getId());
                //                if (FileVOs != null) {
                //                    dto.setFireHydrantFileIds(FileVOs.stream().map(FileVO::getId).collect(Collectors.toList()));
                //                    dto.setFireHydrantFileVOs(FileVOs);
                //                }
                break;
            default:
                throw new ServiceException("business-type不正确");
        }
    }

    /**
     * 查到companyUser后，将人员信息set进去
     *
     * @param wholesalerUsers 查出来的companyuser
     *
     * @return 带有档位，人员信息的dto
     */
    private List<CompanyUserDTO> setUserInfor(List<CompanyUserDTO> wholesalerUsers) {
        List<CompanyUserDTO> userDTOS = new ArrayList<>();
        wholesalerUsers.forEach(wholesalerUser -> {
            //根据userId获取个人信息
            UserDeliver userInfoDTO = userDeliverService.getById(wholesalerUser.getUserId());
            if (userInfoDTO != null) {
                wholesalerUser.setNickname(userInfoDTO.getNickname());
                wholesalerUser.setGender(userInfoDTO.getGender());
                wholesalerUser.setMobile(userInfoDTO.getMobile());
                wholesalerUser.setIdentityCard(userInfoDTO.getIdentityCard());
                wholesalerUser.setCreateBy(userInfoDTO.getCreateBy());
                wholesalerUser.setCreateTime(userInfoDTO.getCreateTime());
                //根据档位ID获取档位名称和company名称
                List<SpaceDTO> spaceDTOS = new ArrayList<>();
                Space space = spaceService.lambdaQuery().eq(Space::getId, wholesalerUser.getStallId()).one();
                wholesalerUser.setCompanyName(space.getCompanyName());
                wholesalerUser.setStallName(space.getName());
                spaceDTOS.add(new SpaceDTO(space));
                wholesalerUser.setSpaceDTOS(spaceDTOS);
                if (userDTOS.parallelStream().noneMatch(u -> u.getUserId().equals(userInfoDTO.getId()))) {
                    userDTOS.add(wholesalerUser);
                }
            }
        });
        return userDTOS;
    }

    /**
     * 根据名称，电话，性别，身份证，是否档主，档位，companyName进行筛选，
     * 用了很多if，很不美观，但是感觉也没啥办法。
     *
     * @param users    被筛选的集合
     * @param queryDto 筛选条件
     *
     * @return 筛选后的集合
     */
    private List<CompanyUserDTO> filterUser(List<CompanyUserDTO> users, WholesalerUserQueryDTO queryDto) {

        if (StrUtil.isNotBlank(queryDto.getSearch())) {
            users = users.stream().filter(user ->
                                                  user.getNickname().contains(queryDto.getSearch()) |
                                                  user.getMobile().contains(queryDto.getSearch()) |
                                                  user.getStallName().contains(queryDto.getSearch()) |
                                                  user.getCompanyName().contains(queryDto.getSearch())
                                         ).collect(Collectors.toList());
        }
        if (queryDto.getGender() != null) {
            users = users.stream().filter(user ->
                                                  user.getGender().equals(queryDto.getGender())).collect(Collectors.toList());
        }
        if (queryDto.getOwner() != null) {
            users = users.stream().filter(user ->
                                                  user.getOwner().equals(queryDto.getOwner())).collect(Collectors.toList());
        }
        if (StrUtil.isNotBlank(queryDto.getCompanyId())) {
            users = users.stream().filter(user ->
                                                  user.getCompanyId().equals(queryDto.getCompanyId())).collect(Collectors.toList());
        }
        return users;
    }

    private String saveUser(CompanyUserDTO dto, String username) {
        UserDeliverDTO userDTO = new UserDeliverDTO();
        if (dto == null) {
            throw new ServiceException("传入的用户信息为空");
        }

        // 向ouaa插入用户
        UserModifyParam userModifyParam = new UserModifyParam();
        userModifyParam.setName(dto.getNickname());
        userModifyParam.setUsername(dto.getMobile());
        userModifyParam.setMobile(dto.getMobile());
        userModifyParam.setGender(Gender.FEMALE);
        userModifyParam.setTenant(SessionContextHolder.getRequester().getTenant());
        UserDTO user = userClient.save(userModifyParam);

        userDTO.setId(user.getId().toString());
        userDTO.setType(UserTypeEnum.USER_TYPE_COMPANY.getCode());
        userDTO.setUsername(dto.getMobile());
        userDTO.setNickname(dto.getNickname());
        userDTO.setGender(dto.getGender());
        userDTO.setMobile(dto.getMobile());
        userDTO.setIdentityCard(dto.getIdentityCard());
        userDTO.setEnabled(true);
        //不知道为啥，返回的带了双引号，这里将前后的""其去掉
        return StrUtil.strip(userDeliverService.saveCompanyUser(userDTO), "\"");
    }

    /**
     * @param projectId 项目ID
     *
     * @return 返回的是一个MAP, ID 和名称对应
     */
    private Map<String, String> getAreaIdNameMap(String projectId) {
        List<AreaDTO> allAreas = this.getAreaDTOSAll(projectId);
        if (allAreas != null) {
            return this.getAreaIdNameMap(allAreas);
        }
        return Collections.emptyMap();
    }

    private Map<String, String> getAreaIdNameMap(List<AreaDTO> areaDTOS) {
        if (areaDTOS != null) {
            return areaDTOS.stream().collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
        }
        return Collections.emptyMap();
    }

    private List<AreaDTO> getAreaDTOSAll(String projectId) {
        try {
            //            return areaApi.getListall(projectId);
        } catch (Exception e) {
            log.error("", e);
        }
        return Collections.emptyList();
    }
}
