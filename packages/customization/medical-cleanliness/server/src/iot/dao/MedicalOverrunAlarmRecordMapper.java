package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalOverrunAlarmRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-14  16:13
 * @Description:
 * @Version: 1.0
 */
@Mapper
public class MedicalOverrunAlarmRecordMapper implements RowMapper<MedicalOverrunAlarmRecord> {

    @Override
    public MedicalOverrunAlarmRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        MedicalOverrunAlarmRecord medicalOverrunAlarmRecord = new MedicalOverrunAlarmRecord();
        medicalOverrunAlarmRecord.setId(rs.getLong("id"));
        medicalOverrunAlarmRecord.setMonitoringTime(rs.getTimestamp("monitoring_time").toLocalDateTime());
        medicalOverrunAlarmRecord.setStatus(rs.getString("status"));
        medicalOverrunAlarmRecord.setDepId(rs.getLong("dep_id"));
        medicalOverrunAlarmRecord.setDepName(rs.getString("dep_name"));
        medicalOverrunAlarmRecord.setTenant(rs.getInt("tenant"));
        medicalOverrunAlarmRecord.setTenantName(rs.getString("tenant_name"));
        medicalOverrunAlarmRecord.setDeviceId(rs.getLong("device_id"));
        medicalOverrunAlarmRecord.setDeviceName(rs.getString("device_name"));
        medicalOverrunAlarmRecord.setRecordData(rs.getString("record_data"));
        return medicalOverrunAlarmRecord;
    }
}
