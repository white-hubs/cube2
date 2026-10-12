package com.guhecloud.cube.biz.mdata.server.purchaser.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.Purchaser;
import com.guhecloud.cube.biz.mdata.server.purchaser.vo.PurchaserAuthenticationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author lzy
 * @date 2021/12/24
 */
@Mapper
public interface PurchaserMapper extends BaseMapper<Purchaser> {

    @Select("select pa.`id`,p.`user_id`,p.`user_name`,p.`id` as `purchaser_id`,pa.`delegate_name`,pa.`delegate_identity_card`,pa.`status`,pa.`audit_time`,pa.`submit_time`," +
            "pa.`type`,pa.`type_name`,pa.`type_code`,pa.`license_id`,pa.`front_identity_card_id`,pa.`back_identity_card_id`,pa.`remark`,pa.`source` from purchaser p " +
            "LEFT JOIN `purchaser_authentication` pa on pa.`purchaser_id` = p.`id` and pa.`status` != 'INVALID' ${ew.customSqlSegment}"
    )
    Page<PurchaserAuthenticationVO> pageForPurchaser(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
