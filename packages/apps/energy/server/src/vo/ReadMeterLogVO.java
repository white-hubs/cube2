package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import com.guhecloud.platform.base.util.DateUtils;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadMeterLogVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant time;//influxdb的时间戳 修改的唯一键 只能通过时间覆盖
    //设备编号
    private String deviceId;
    //设备编码
    private String deviceCode;
    //设备名称
    private String deviceName;
    //区域ID
    private String areaId;
    private String tenant;
    //设备类型
    private String instrumentType;

    //当前读取时间
    private Long curTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime curDateTime;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal readValue;
    //上次读取时间
    private Long preTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime preDateTime;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal preReadValue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal usage;//用量
    //采集方式
    private String collectType;

    public LocalDateTime getCurDateTime() {
        curDateTime = curTime != null ? DateUtils.parse(curTime) : curDateTime;
        return curDateTime;
    }

    public LocalDateTime getPreDateTime() {
        preDateTime = preTime != null ? DateUtils.parse(preTime) : preDateTime;
        return preDateTime;
    }

    /**
     * 创建Point
     *
     * @return
     */
    public Point createPointForMeterLog(ReadMeterLogVO readMeterLog) {
        //表名称
        String measurement = "readMeterLog";
        //查询采集记录数据
        return Point.measurement(measurement)
                .addTag("deviceId", readMeterLog.getDeviceId())//设备id
                .addTag("tenant", readMeterLog.getTenant())
                .addTag("instrumentType", readMeterLog.getInstrumentType())
                .addTag("areaId", readMeterLog.getAreaId())
                .addField("deviceCode", readMeterLog.getDeviceCode())
                .addField("deviceName", readMeterLog.getDeviceName())
                .addField("curTime", readMeterLog.getCurTime())
                .addField("readValue", readMeterLog.getReadValue().setScale(2, RoundingMode.HALF_UP))
                .addField("preTime", readMeterLog.getPreTime() != null ? readMeterLog.getPreTime() : null)
                .addField("preReadValue",
                          readMeterLog.getPreReadValue() != null ? readMeterLog.getPreReadValue().setScale(2, RoundingMode.HALF_UP) : null)
                .addField("usage", readMeterLog.getUsage() != null ? readMeterLog.getUsage().setScale(2, RoundingMode.HALF_UP) : null)//用量
                .addField("collectType", StringUtils.isNotEmpty(readMeterLog.getCollectType()) ? readMeterLog.getCollectType() : "auto")
                .time(readMeterLog.getTime(), WritePrecision.S);
    }
}
