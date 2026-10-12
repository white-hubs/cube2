package com.guhecloud.cube.srv.gen.cabbage.server.vo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 挡位所有对象集合
 * 包含，挡位/挡位类型/部门/区域
 */
@Data
public class DbcAllStoreListDTO {

    private List<DbcStoreDTO> storeList = new ArrayList<>();
    private List<DbcStoreTypeDTO> storeTypeList = new ArrayList<>();
    private List<DbcDeptDTO> deptList = new ArrayList<>();
    private List<DbcAreaDTO> areaList = new ArrayList<>();
}
