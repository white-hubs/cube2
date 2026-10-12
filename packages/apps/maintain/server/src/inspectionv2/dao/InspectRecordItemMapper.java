package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordItemStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CheckTheMismatchDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemResultVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 巡检记录-结果项	 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectRecordItemMapper extends BaseMapper<InspectRecordItemPO> {
    @Select("SELECT id,item_id as 'itemId',error_value_flag as 'resultFlag' from inspect_record_item WHERE record_id = #{recordId};")
    List<InspectionItemResultVO> selectResultList(@Param("recordId") Long recordId);

    @Select("select a.item_name as 'name',b.target_name as 'inspectionObj',b.nickname as 'checkPerson',a.item_result_name as 'checkResult' from inspect_record_item a " +
            "left join inspect_record b on a.record_id = b.id " +
            "WHERE a.id = #{id}"
    )
    CheckTheMismatchDetailVO queryCheckDetail(@Param("id") Long id);

    @Select("SELECT a.item_result_name as 'result',a.item_result_name as 'reportResult',b.nickname as 'principal',a.item_name as 'content' from inspect_record_item a " +
            "left join inspect_record b on a.record_id = b.id " +
            "WHERE a.id = #{id}"
    )
    CheckTheMismatchDetailVO queryInspectionDetail(@Param("id") Long id);

    /**
     * 获取巡检记录错误描述
     * 注意：这里只查了record_id和error_desc这两个字段
     *
     * @return 错误描述
     */
    List<InspectRecordItemPO> listRecordErrorDesc(@Param("recordIdList") List<Long> recordIdList);

    /**
     * 批量查询异常巡检项的记录 id。
     *
     * @param recordIdList 巡检记录 id
     *
     * @return 异常巡检项记录
     */
    List<InspectRecordItemPO> listAbnormalRecordIdsByRecordIds(@Param("recordIdList") List<Long> recordIdList);

    /**
     * 批量获取巡检记录的记录项
     *
     * @param recordIdList 巡检记录id
     *
     * @return 记录项列表
     */
    List<InspectRecordItemVO> getListByRecordIdList(@Param("recordIdList") List<Long> recordIdList,
                                                    @Param("errorValueFlag") Integer errorValueFlag
                                                   );

    /**
     * 巡检记录项总部数据采集
     *
     * @param tenant 租户
     * @param period 时间段
     *
     * @return 集合
     */
    @InterceptorIgnore(tenantLine = "on")
    List<InspectRecordItemPO> getRecordItemCollectData(@Param("tenant") Integer tenant, @Param("period") Period period);

    /**
     * 巡检记录项统计
     *
     * @param param
     *
     * @return
     */
    List<InspectRecordItemStatisticsVO> getInspectRecordItemsStatistics(@Param("param") InspectRecordItemQueryParam param);
}
