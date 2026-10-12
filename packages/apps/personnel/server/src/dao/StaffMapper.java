package com.guhecloud.cube.srv.gen.personnel.management.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.StaffNotInParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffChangeVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffDetailVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffExchangeStatisticsVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffStateVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query.StaffQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author bq
 * @date 2022/11/08
 */
@Mapper
public interface StaffMapper extends BaseMapper<StaffPO> {

    Page<StaffPO> getStaffPageNotIn(Page<?> page, @Param("param") StaffNotInParam param);

    List<StaffDetailVO> getList(@Param("param") StaffQueryParam staffQueryParam);

    Page<StaffDetailVO> getPage(Page<?> page, @Param("param") StaffQueryParam staffQueryParam);

    Page<StaffChangeVO> getStaffChange(Page<?> page, @Param("param") StaffQueryParam staffQueryParam);

    @Select("SELECT * FROM `staff` WHERE TIMESTAMPDIFF(YEAR,`pay_social_insurance_first_date`,now())>=#{workingYear}")
    Set<Long> selectListWithWorkingYear(@Param("workingYear") Integer workingYear);

    @Select("SELECT * FROM `staff` WHERE TIMESTAMPDIFF(YEAR,`induction_date`,now()) >= #{timeOfInduction}")
    Set<Long> selectListTimeOfInduction(@Param("timeOfInduction") Integer timeOfInduction);

    List<StaffExchangeStatisticsVO> getPeriodStaffNum(@Param("param") StaffQueryParam param);

    /**
     * 根据工作状态查询
     *
     * @param param
     *
     * @return
     */
    Page<StaffStateVO> getPageByState(Page<?> page, @Param("param") StaffQueryParam param);

    /**
     * 适用于通讯录，按照名称的ABC顺序进行排列
     *
     * @param param
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<StaffStateVO> getPageByABC(@Param("param") StaffQueryParam param, @Param("tenant") Integer tenant);

    /**
     * 恢复已经删除的员工，并修改其部分信息（只修改与ouaa互通的信息）
     *
     * @param staffPO
     */
    @InterceptorIgnore(tenantLine = "true")
    void updatePartInfoByDeleted(@Param("staffPO") StaffPO staffPO);

    /**
     * 恢复已经删除的员工，并修改其全部信息
     *
     * @param staffPO
     */
    void updateAllInfoByDeleted(@Param("staffPO") StaffPO staffPO);

}
