package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.Wholesaler;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description
 */
public interface WholesalerService extends IService<Wholesaler> {

    /**
     * 根据ID得到批发商的基本信息(包括人员信息)
     * @param id 批发商ID
     * @return 返回批发商基本信息（包含图片，文件，ID等）
     */
    WholesalerDTO getWholesaler(String id);

    /**
     * 根据company_user表中的数据，联合sapce和user表，
     * 再加上筛选条件，最终找出对应的人员列表
     * @param queryDto 查询条件
     * @return 返回user的集合
     */
    List<CompanyUserDTO> getWholesalerUsers(WholesalerUserQueryDTO queryDto);

    /**
     * 要在conpany-user,user两张表里面添加内容
     * @param dto 新增的company—user DTO
     * @return 新增的ID
     */
    String save (CompanyUserDTO dto, LoginUser user);

    /**
     * 档主在手机上面保存从业人员
     * @param stallId 当前添加用户的档主所在档位ID
     * @param dto 添加的从业人员信息
     * @return 新增的ID
     */
    String saveUserByApp(String stallId, CompanyUserDTO dto,LoginUser user);

    /**
     * 修改商户下的人员信息
     * 只能修改关联的档位信息，人员信息要从总部改
     * @param dto
     */
    void update(CompanyUserDTO dto);

    /**
     * 根据ID删除
     * @param id 商户（批发商）下头人员的ID
     */
    void remove(String id );

    List<WholesalerShortDTO> getWholesalerList(String projectId);

    List<WholeSpaceDTO> getWholesalerStalls(String projectId);
    WholeSpaceDTO getWholesalerStalls(String projectId,Long id);


    List<WholesalerListDTO> getWholesalerPage(WholesalerQueryDTO queryDTO);

    String saveWholesaler(CompanyDTO dto);

    WholesalerListDTO getWholesalerByStallId(String projectId,String stallId);
}
