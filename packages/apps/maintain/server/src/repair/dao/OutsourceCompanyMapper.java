package com.guhecloud.cube.srv.gen.maintain.server.repair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceCompanyVO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/5/18 15:50
 * @description
 */
@Mapper
public interface OutsourceCompanyMapper extends BaseMapper<OutsourceCompanyPO> {

    List<OutsourceCompanyVO> list(@Param("productCategoryCode") String productCategoryCode);
}
