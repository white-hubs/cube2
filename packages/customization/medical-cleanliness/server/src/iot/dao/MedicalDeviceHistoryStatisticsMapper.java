package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalDeviceHistoryStatisticsVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-24  18:12
 * @Description:
 * @Version: 1.0
 */
public class MedicalDeviceHistoryStatisticsMapper implements RowMapper<MedicalDeviceHistoryStatisticsVO> {
    @Override
    public MedicalDeviceHistoryStatisticsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        MedicalDeviceHistoryStatisticsVO medicalOverrunAlarmRecord = new MedicalDeviceHistoryStatisticsVO();
        medicalOverrunAlarmRecord.setDeviceId(rs.getLong("device_id"));
        medicalOverrunAlarmRecord.setDeviceName(rs.getString("device_name"));
        medicalOverrunAlarmRecord.setMonitoringTime(rs.getString("monitoring_time"));
        medicalOverrunAlarmRecord.setStatus(rs.getString("status"));
        medicalOverrunAlarmRecord.setNumber(rs.getInt("number"));
        return medicalOverrunAlarmRecord;
    }
}
