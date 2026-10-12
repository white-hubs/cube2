package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AlarmRules;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyAlarmRule;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：报警规则设备绑定 Repository接口
 *
 * @author lrj
 * @date 2022-07-07 15:12:36
 */
@Repository
public interface EnergyAlarmRuleMapper extends BaseMapper<EnergyAlarmRule> {

    @Delete("Delete FROM energy_alarm_rule where device_id = #{deviceId}")
    void deleteByDeviceId(Long deviceId);

    @Delete("Delete FROM energy_alarm_rule where rules_id = #{rulesId}")
    void deleteByRulesId(Long rulesId);

    @Select("SELECT\n" +
            "\tb.* \n" +
            "FROM\n" +
            "\tenergy_alarm_rule a\n" +
            "\tLEFT JOIN alarm_rules b ON a.rules_id = b.id \n" +
            "WHERE\n" +
            "\tb.enabled = true \n" +
            "\tAND a.tenant = #{tenant}\n" +
            "\t\n" +
            "\tAND a.device_id = #{id}"
    )
    List<AlarmRules> getDeviceRules(@Param("id") Long id, @Param("tenant") Integer tenant);
}