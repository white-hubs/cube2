package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceImportExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDevice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 商户档案-所有设备（批发商的设备） 服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-04
 */
public interface WholesalerDeviceService extends IService<WholesalerDevice> {
    String SHEET_NAME = "设备备案模板";
    String WHOLESALER_DEVICE = "wholesaler-device";

    /**
     * 首先根据stallId获取stall_Name,companyId,companyName,将其set到dto中
     * @param stallId 当前操作人（档主）的档位ID
     * @param dto 设备DTO
     * @return 添加成功后的ID
     */
    String save( WholesalerDeviceDTO dto);

    /**
     * 获取车辆导入的模板
     * @param request request
     * @param response response
     */
    void getDeviceTemplate(HttpServletRequest request, HttpServletResponse response);

    /**
     * 修改DTO ，并且还要对File做修改(只能修改数量和所属档位)
     * @param dto 修改后的DTO
     */
    void update(WholesalerDeviceDTO dto);

    /**
     * 根据ID删除，并且将对应的FILE remove掉
     * @param id id
     */
    void remove(String id);

    /**
     * 导入设备信息数据
     *
     * @param projectId
     * @param dtos excel中读取的数据
     * @return 导入的错误信息
     */
    List<ErrorExcelDTO> importData(String projectId, List<WholesalerDeviceImportExcelDTO> dtos);
}
