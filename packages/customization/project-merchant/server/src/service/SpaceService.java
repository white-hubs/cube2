package com.guhecloud.cube.deliver.project.merchant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceExcelDTO;

import java.util.List;

public interface SpaceService extends IService<Space> {
	
	public static final String BUSINESS_TYPE = "space";

    public static final String STORE_TYPE = "storeType";             // 类型
	public static final String ATTR = "spaceAttr";                   // 空间属性
	public static final String ATTR_BOOTH = "spaceAttr_booth";       // 档位
	public static final String ATTR_OFFICE = "spaceAttr_office";     // 办公房

    public static final String TRADE_MODEL = "spaceTradeModel";   //空间经营类型
    public static final String TRADE_MODEL_OPERATION = "spaceTradeModel_operation";   //运营
    public static final String TRADE_MODEL_PERSON = "spaceTradeModel_personal";   //自用
    public static final String TRADE_MODEL_PUBLIC = "spaceTradeModel_public";   //公用

    /**
     * 根据USERID获取其下的档位信息(USERID是档主)  王量2022/1/8
     * @param userId 用户ID
     * @return 返回一个List的档位信息
     */
    List<SpaceDTO> getSpacesByUserId(String userId);

    void save(SpaceDTO spaceDTO);

    void update(SpaceDTO spaceDTO);

    void remove(String id);

    void removeBatch(String[] ids);

    void importData(String projectId, List<SpaceExcelDTO> dtoSpaces);

}
