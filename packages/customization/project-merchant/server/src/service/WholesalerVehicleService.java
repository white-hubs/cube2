package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleImportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerVehicle;
import com.guhecloud.cube.deliver.project.merchant.dto.WholeSpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerVehicleDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 商户档案-所有车辆（批发商交通工具表） 服务类
 * </p>
 *
 * @author wangliang
 * @since 2021-12-31
 */
public interface WholesalerVehicleService extends IService<WholesalerVehicle> {

     String VEHICLE_TYPE = "vehicleType"; // 车辆类型
     String WHOLESALER_VEHICLE = "wholesaler-vehicle";
     String SHEET_NAME = "车辆备案模板";

     /**
      *  下载模板
      * @param request  HttpServletRequest
      * @param response HttpServletResponse
      */
     void getVehicleTemplate(HttpServletRequest request, HttpServletResponse response);
     /**
      * 首先根据stallId获取stall_Name,companyId,companyName,将其set到dto中
      * @param stallId 当前操作人（档主）的档位ID
      * @param dto 车辆DTO
      * @return 添加成功后的ID
      */
     String save(WholesalerVehicleDTO dto);
     /**
      * 修改DTO ，并且还要对File做修改
      * @param dto 修改后的DTO
      */
     void update(WholesalerVehicleDTO dto);

     /**
      * 根据ID删除，并且将对应的FILE remove掉
      * @param id id
      */
     void remove(String id);

     /**
      * 导入excel中的车辆数据到数据库
      * @param dtos EXCEL中的车辆信息
      * @return 错误信息
      */
     List<ErrorExcelDTO> importData(String projectId, List<WholesalerVehicleImportExcelDTO> dtos);

    List<WholeSpaceDTO> getWholesalerStalls(String projectId);
}
