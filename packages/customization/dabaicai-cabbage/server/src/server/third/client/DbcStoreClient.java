package com.guhecloud.cube.srv.gen.cabbage.server.third.client;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.cube.srv.gen.cabbage.server.third.utils.DbcHttpUtils;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcAllStoreListDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcAreaDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcDeptDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreTypeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 挡位client
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcStoreClient {
    private final DbcHttpUtils dbcHttpUtils;

    /**
     * 获取挡位列表
     *
     * @param marketId 市场id
     *
     * @return 挡位列表
     */
    public List<DbcStoreDTO> dbcStoreList(Integer marketId) {
        // 挡位请求地址
        String storeUrl = "openApi/base/getStoreList";
        // 入参
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("marketId", String.valueOf(marketId));
        // 请求
        Object obj = dbcHttpUtils.sendDBCPost(storeUrl, dataMap);
        // 处理返回
        List<DbcStoreDTO> resultList = new ArrayList<>();
        if (obj == null) {
            return resultList;
        }
        JSONObject dataObj = (JSONObject) obj;
        Object listObj = dataObj.get("list");
        if (listObj == null) {
            return resultList;
        }
        JSONArray jsonArray = (JSONArray) listObj;
        resultList = jsonArray.toJavaList(DbcStoreDTO.class);
        return resultList;
    }

    /**
     * 获取挡位所有内容列表
     * 包含 挡位/挡位类型/部门/区域
     *
     * @param marketId 市场id
     *
     * @return 挡位所有内容集合对象
     */
    public DbcAllStoreListDTO dabAllStoreDTOList(Integer marketId) {
        DbcAllStoreListDTO result = new DbcAllStoreListDTO();

        List<DbcStoreDTO> storeDTOList = dbcStoreList(marketId);
        if (storeDTOList == null || storeDTOList.isEmpty()) {
            return result;
        }
        result.setStoreList(storeDTOList);

        Map<String, DbcStoreTypeDTO> storeTypeDTOMap = new HashMap<>();
        Map<String, DbcDeptDTO> deptDTOMap = new HashMap<>();
        Map<String, DbcAreaDTO> areaDTOMap = new HashMap<>();
        for (DbcStoreDTO dbcStoreDTO : storeDTOList) {
            dbcStoreDTO.setMarketId(marketId);
            if (StringUtils.isNotBlank(dbcStoreDTO.getStorefrontTypeCode())) {
                DbcStoreTypeDTO typeDTO = new DbcStoreTypeDTO();
                typeDTO.setStorefrontTypeCode(dbcStoreDTO.getStorefrontTypeCode());
                typeDTO.setStorefrontTypeName(dbcStoreDTO.getStorefrontTypeName());
                storeTypeDTOMap.put(typeDTO.getStorefrontTypeCode(), typeDTO);
            }

            if (StringUtils.isNotBlank(dbcStoreDTO.getDepartmentCode())) {
                DbcDeptDTO deptDTO = new DbcDeptDTO();
                deptDTO.setDepartmentCode(dbcStoreDTO.getDepartmentCode());
                deptDTO.setDepartmentName(dbcStoreDTO.getDepartmentName());
                deptDTOMap.put(deptDTO.getDepartmentCode(), deptDTO);
            }

            if (StringUtils.isNotBlank(dbcStoreDTO.getAreaCode())) {
                DbcAreaDTO areaDTO = new DbcAreaDTO();
                areaDTO.setAreaCode(dbcStoreDTO.getAreaCode());
                areaDTO.setAreaName(dbcStoreDTO.getAreaName());
                areaDTOMap.put(areaDTO.getAreaCode(), areaDTO);
            }
        }

        result.setStoreTypeList(new ArrayList<>(storeTypeDTOMap.values()));
        result.setDeptList(new ArrayList<>(deptDTOMap.values()));
        result.setAreaList(new ArrayList<>(areaDTOMap.values()));
        return result;
    }

    /**
     * 获取挡位包含的挡位类型列表
     *
     * @param marketId 市场id
     *
     * @return 挡位类型集合
     */
    public List<DbcStoreTypeDTO> dbcStoreTypeList(Integer marketId) {
        return dabAllStoreDTOList(marketId).getStoreTypeList();
    }

    /**
     * 获取挡位包含的部门列表
     *
     * @param marketId 市场id
     *
     * @return 部门集合
     */
    public List<DbcDeptDTO> dbcDeptList(Integer marketId) {
        return dabAllStoreDTOList(marketId).getDeptList();
    }

    /**
     * 获取挡位包含的区域列表
     *
     * @param marketId 市场id
     *
     * @return 区域集合
     */
    public List<DbcAreaDTO> dbcAreaList(Integer marketId) {
        return dabAllStoreDTOList(marketId).getAreaList();
    }

}
