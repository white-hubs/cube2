package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.AcreageDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.BimInfoDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.HistoryArea;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.SpaceMapper;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyUserService;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceDTO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space> implements SpaceService {

    private final SpaceMapper spaceMapper;
    private final CompanyMapper companyMapper;
    //    private final BimInfoService bimInfoService;
    private final CompanyUserService companyUserService;
    //    private final AreaService areaService;
    //    private final HistoryAreaService historyAreaService;
    //    private final ElementApi elementApi;
    //    private final SysDeptApi sysDeptApi;

    @Override
    public List<SpaceDTO> getSpacesByUserId(String userId) {
        List<CompanyUser> companyUserList = companyUserService.lambdaQuery()
                .eq(CompanyUser::getUserId, userId)
                .eq(CompanyUser::getOwner, 1)
                .list();
        if (companyUserList.isEmpty()) {
            throw new ServiceException("该用户不是任何档位的档主");
        }
        List<SpaceDTO> dtos = new ArrayList<>(16);
        companyUserList.forEach(companyUser -> {
            dtos.add(new SpaceDTO(this.lambdaQuery().eq(Space::getId, companyUser.getStallId()).one()));
        });
        return dtos;
    }

    @Override
    public void save(SpaceDTO spaceDTO) {
        List<String> areaIds = spaceDTO.getAreaIds();
        spaceDTO.setCode(BizCodeUtils.generate("KJ"));
        if (areaIds == null || areaIds.size() == 0) {
            throw new ServiceException("区域不能为空");
        }
        spaceDTO.setAreaId1(areaIds.get(0));
        spaceDTO.setAreaId2(areaIds.size() > 1 ? areaIds.get(1) : "");
        spaceDTO.setAreaId3(areaIds.size() > 2 ? areaIds.get(2) : "");
        Space space = spaceDTO.convert();
        space.setDeleted(false);
        this.setCompanyName(space, spaceDTO.getCompanyId());
        spaceMapper.insert(space);
        // 保存bim绑定信息
        this.saveBimInfo(space.getId(), spaceDTO.getProjectId(), spaceDTO.getBimInfo());
        // 修改面积
        this.saveAcreage(spaceDTO);
        spaceDTO.setId(space.getId());
        this.saveHistoryAcreage(spaceDTO, null);
    }

    @Override
    public void update(SpaceDTO spaceDTO) {
        Space saved = spaceMapper.selectById(spaceDTO.getId());
        if (saved == null) {
            throw new ServiceException("空间不存在");
        }
        List<String> areaIds = spaceDTO.getAreaIds();
        Space space = spaceDTO.convert();
        if (areaIds == null || areaIds.size() == 0) {
            throw new ServiceException("区域不能为空");
        }
        space.setAreaId1(areaIds.get(0));
        space.setAreaId2(areaIds.size() > 1 ? areaIds.get(1) : "");
        space.setAreaId3(areaIds.size() > 2 ? areaIds.get(2) : "");
        // 解除与商户的绑定
        if (StringUtils.isEmpty(spaceDTO.getCompanyId())) {
            space.setCompanyId("");
            space.setCompanyName("");
            // 解除档位与用户的关联；删除档位的打卡信息
            if (StringUtils.isNotEmpty(saved.getCompanyId())) {
                List<CompanyUser> companyUsers = companyUserService.lambdaQuery().eq(CompanyUser::getStallId, spaceDTO.getId()).list();
                companyUsers.forEach(companyUser -> {
                    companyUserService.removeUser("", spaceDTO.getId(), companyUser.getUserId(), "");
                });
            }
        } else {
            this.setCompanyName(space, spaceDTO.getCompanyId());
        }
        space.setDeleted(false);
        spaceMapper.updateById(space);
        // 保存bim绑定信息
        this.saveBimInfo(saved.getId(), spaceDTO.getProjectId(), spaceDTO.getBimInfo());
        // 修改面积
        this.updateAcreage(spaceDTO, saved);
        this.saveHistoryAcreage(spaceDTO, saved);
    }

    private void setCompanyName(Space space, String companyId) {
        Company company = companyMapper.selectById(companyId);
        space.setCompanyName(company == null ? "" : company.getName());
        space.setCompanyId(company == null ? "" : company.getId());
    }

    @Override
    public void remove(String id) {
        Space saved = spaceMapper.selectById(id);
        if (saved == null) {
            throw new ServiceException(id + ":空间ID不存在");
        }
        // 判断是否绑定了商户
        if (StringUtils.isNotEmpty(spaceMapper.selectById(id).getCompanyId())) {
            throw new ServiceException("该空间绑定了商铺，不能删除");
        }
        //        bimInfoService.remove(BUSINESS_TYPE, id);
        //        historyAreaService.remove(Wrappers.<HistoryArea>lambdaQuery().eq(HistoryArea::getObjectType, BUSINESS_TYPE).eq(HistoryArea::getObjectId, id));
        spaceMapper.deleteById(id);
        // 修改面积
        SpaceDTO dto = new SpaceDTO();
        dto.setAreaId1(saved.getAreaId1());
        dto.setAreaId2(saved.getAreaId2());
        dto.setAreaId3(saved.getAreaId3());
        this.updateAcreage(dto, saved);
    }

    @Override
    public void removeBatch(String[] ids) {
        for (String id : ids) {
            Space saved = spaceMapper.selectById(id);
            if (saved == null) {
                throw new ServiceException("【" + id + "】ID不存在");
            }
            if (StringUtils.isNotEmpty(saved.getCompanyId())) {
                throw new ServiceException("【" + saved.getName() + "】空间绑定了商铺，不能删除");
            }
            //            bimInfoService.remove(BUSINESS_TYPE, id);
            //            historyAreaService.remove(Wrappers.<HistoryArea>lambdaQuery().eq(HistoryArea::getObjectType, BUSINESS_TYPE).eq(HistoryArea::getObjectId, id));
            spaceMapper.deleteById(id);
        }

    }

    @Override
    public void importData(String projectId, List<SpaceExcelDTO> dtoSpaces) {
        Map<String, String> deptNameIdMap = new HashMap<>();

        List<ErrorExcelDTO> dtos = new ArrayList<>();
        int i = 2;
        for (SpaceExcelDTO dtoSpace : dtoSpaces) {
            this.getIdOrCode(dtoSpace);

            dtoSpace.setProjectId(projectId);
            Space space = dtoSpace.convert();// 获得部门ID

            space.setDeptId(this.getDeptId(deptNameIdMap, projectId, space.getDeptName()));
            if (StringUtils.isEmpty(space.getDeptId())) {
                dtos.add(new ErrorExcelDTO(i, "*部门名", "部门不存在"));
            }

            //先判断名称是否重名； ID不为空修改，否则新增
            Space spaceByName = spaceMapper.selectOne(Wrappers.<Space>lambdaQuery().eq(Space::getProjectId, projectId)
                                                              .eq(Space::getName, dtoSpace.getName()));

            /*if (spaceByName != null) {
                this.updateSpace(spaceByName, space);
                space.setId(spaceByName.getId());
            } else {
                spaceMapper.insert(space);
            }*/

            if (StringUtils.isNotBlank(space.getId())) {
                Space spaceById = spaceMapper.selectById(space.getId());
                if (spaceById != null) {
                    if (spaceByName != null && !spaceByName.getId().equals(space.getId())) {
                        dtos.add(new ErrorExcelDTO(i, "*名称", "名称已存在"));
                    } else {
                        spaceMapper.updateById(space);
                    }
                } else {
                    dtos.add(new ErrorExcelDTO(i, "ID", "ID不存在"));
                }

            } else {
                if (spaceByName == null) {
                    space.setCode(BizCodeUtils.generate("KJ"));
                    spaceMapper.insert(space);
                } else {
                    dtos.add(new ErrorExcelDTO(i, "*名称", "名称已存在"));
                }
            }

            try {
                if (StringUtils.isNotEmpty(dtoSpace.getEcode())) {
                    this.saveBimInfo(space.getId(), projectId, dtoSpace.getEcode());
                }
            } catch (Exception e) {
                dtos.add(new ErrorExcelDTO(i, "构建编码", e.getMessage()));
            }

            i++;
        }

        if (dtos.size() > 0) {
            throw new ServiceException("导入数据异常");
        }
    }

    private void getIdOrCode(SpaceExcelDTO space) {
        String[] areaId1 = space.getAreaId1().split("_");
        space.setAreaId1(areaId1[areaId1.length - 1]);
        String[] areaId2 = space.getAreaId2().split("_");
        space.setAreaId2(areaId2[areaId2.length - 1]);
        String[] areaId3 = space.getAreaId3().split("_");
        space.setAreaId3(areaId3[areaId3.length - 1]);

        String[] attribute = space.getAttribute().split("_", 3);
        space.setAttribute(attribute[attribute.length - 1]);
        String[] tradeModel = space.getTradeModel().split("_", 3);
        space.setTradeModel(tradeModel[tradeModel.length - 1]);
        String[] type = space.getType().split("_", 3);
        space.setType(type[type.length - 1]);
    }

    private void saveBimInfo(String id, String projectId, BimInfoDTO bimInfoDTO) {
        if (bimInfoDTO != null) {
            bimInfoDTO.setProjectId(projectId);
            bimInfoDTO.setBusinessType(BUSINESS_TYPE);
            bimInfoDTO.setBusinessKey(id);
            //            bimInfoService.saveOrUpdate(bimInfoDTO);
        }
    }

    private void saveBimInfo(String id, String projectId, String ecode) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(ecode)) {
            return;
        }
        BimInfoDTO dto = new BimInfoDTO();
        dto.setProjectId(projectId);
        dto.setBusinessType(BUSINESS_TYPE);
        dto.setBusinessKey(id);
        dto.setEcode(ecode);
        dto.setIsImport(true);

        try {
            //            ElementDTO elementDTO = elementApi.getByCode(ecode);
            //            if (elementDTO != null) dto.setObjectId(elementDTO.getObjectId());
        } catch (Exception e) {
            log.error("根据Ecode查询构建信息异常", e);
        }

        //        bimInfoService.saveOrUpdate(dto);
    }

    //    // TODO 在空间表中增加商户名称
    //    @PostConstruct
    //    private void insertSpaceToCompanyName(){
    //        List<Space> spaces = spaceMapper.selectList(Wrappers.<Space>lambdaQuery().ne(Space::getCompanyId, ""));
    //        spaces.forEach(space -> {
    //            Company company = companyMapper.selectById(space.getCompanyId());
    //            if (company != null && !company.getName().equals(space.getCompanyName())) {
    //                space.setCompanyName(company.getName());
    //                spaceMapper.updateById(space);
    //            }
    //        });
    //    }

    private String getDeptId(Map<String, String> deptNameIdMap, String projectId, String deptName) {
        String deptId = deptNameIdMap.getOrDefault(deptName, "");
        //        if (com.ghy.iops.service.commons.utils.StringUtils.isEmpty(deptId)) {
        //            try {
        //                DeptDTO deptDTO = sysDeptApi.getByName(projectId, deptName);
        //                if (deptDTO != null) {
        //                    deptId = deptDTO.getId();
        //                    deptNameIdMap.put(deptName, deptId);
        //                }
        //            } catch (Exception e) {
        //                log.error("通过部门名查ID异常", e);
        //                throw new ServiceException("查询部门异常");
        //            }
        //        }
        return deptId;
    }

    private void saveAcreage(SpaceDTO spaceDTO) {
        if (StringUtils.isNotEmpty(spaceDTO.getAreaId2())) {
            AcreageDTO dto = new AcreageDTO();
            BeanUtils.copyProperties(spaceDTO, dto);
            //            areaService.updateAcreage(spaceDTO.getAreaId2(), dto);
        }
    }

    private void updateAcreage(SpaceDTO spaceDTO, Space saved) {
        if (StringUtils.isNotEmpty(spaceDTO.getAreaId2())) {
            AcreageDTO dto = new AcreageDTO();
            dto.setLeaseArea(spaceDTO.getLeaseArea() - saved.getLeaseArea());
            dto.setLeaseRealArea(spaceDTO.getLeaseRealArea() - saved.getLeaseRealArea());
            dto.setLeasePublicArea(spaceDTO.getLeasePublicArea() - saved.getLeasePublicArea());
            dto.setCleanArea(spaceDTO.getCleanArea() - saved.getCleanArea());
            dto.setFireArea(spaceDTO.getFireArea() - saved.getFireArea());
            dto.setParkingArea(spaceDTO.getParkingArea() - saved.getParkingArea());
            //            areaService.updateAcreage(spaceDTO.getAreaId2(), dto);
        }
    }

    private void saveHistoryAcreage(SpaceDTO spaceDTO, Space saved) {
        if (null != saved) {
            AcreageDTO oldAreas = new AcreageDTO();
            BeanUtils.copyProperties(saved, oldAreas);
            AcreageDTO newAreas = new AcreageDTO();
            BeanUtils.copyProperties(spaceDTO, newAreas);
            if (!oldAreas.equals(newAreas)) {
                this.buildHistory(spaceDTO);
            }
        } else {
            this.buildHistory(spaceDTO);
        }
    }

    private void buildHistory(SpaceDTO spaceDTO) {
        HistoryArea historyArea = new HistoryArea();
        BeanUtils.copyProperties(spaceDTO, historyArea);
        historyArea.setId("");
        historyArea.setObjectType(BUSINESS_TYPE);
        historyArea.setObjectId(spaceDTO.getId());
        historyArea.setProjectId(spaceDTO.getProjectId());
        historyArea.setCreateBy(null);
        historyArea.setCreateTime(null);
        //        historyAreaService.save(historyArea);
    }

}
