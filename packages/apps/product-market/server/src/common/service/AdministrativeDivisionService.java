package com.guhecloud.cube.srv.gen.product.market.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.AdministrativeDivision;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.AdministrativeDivisionTreeVO;

import java.util.List;

/**
 * 描述：行政区划服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface AdministrativeDivisionService extends IService<AdministrativeDivision> {

    List<AdministrativeDivisionTreeVO> getAdministrativeDivisionTree();
}