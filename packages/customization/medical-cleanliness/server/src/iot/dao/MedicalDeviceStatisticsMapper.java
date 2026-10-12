package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalOverrunAlarmRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-24  18:12
 * @Description:
 * @Version: 1.0
 */
public class MedicalDeviceStatisticsMapper implements RowMapper<MedicalOverrunAlarmRecord> {
    @Override
    public MedicalOverrunAlarmRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        MedicalOverrunAlarmRecord medicalOverrunAlarmRecord = new MedicalOverrunAlarmRecord();
        medicalOverrunAlarmRecord.setDeviceId(rs.getLong("device_id"));
        medicalOverrunAlarmRecord.setDeviceName(rs.getString("device_name"));
        medicalOverrunAlarmRecord.setMonitoringTime(rs.getTimestamp("monitoring_time").toLocalDateTime());
        medicalOverrunAlarmRecord.setStatus(rs.getString("status"));
        medicalOverrunAlarmRecord.setNumber(rs.getInt("number"));
        return medicalOverrunAlarmRecord;
    }
}
