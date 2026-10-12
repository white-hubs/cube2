package com.guhecloud.cube.srv.gen.adapter.information.server.itc.qo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * itc发送节目参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItcSendProgramParam {
    /**
     * 资源、布局、web页面或分屏id（根据从布局或资源或web页面界面进来获取其自增id）
     */
    @JsonProperty("layout_resource_id")
    private Integer layoutResourceId;

    /**
     * 播放类型（1=周期播放，2=连续播放，3=插播，4=垫片播放）
     */
    @JsonProperty("play_type")
    private String playType;

    /**
     * 播放方式（1=在线，2=离线）
     */
    @JsonProperty("is_online")
    private Integer isOnline;

    /**
     * 发布类型（1=节目（包括H5、直播流、播放列表、资讯），2=布局（已废弃），3=web页面，4=分屏）
     */
    @JsonProperty("push_type")
    private Integer pushType;

    /**
     * 设备id（多个逗号分隔）
     */
    @JsonProperty("device_ids")
    private String deviceIds;

    /**
     * 开始日期（年-月-日）（如：2020-10-23）
     */
    @JsonProperty("start_date")
    private String startDate;

    /**
     * 开始时间（时:分）（如：18:30）
     */
    @JsonProperty("start_time")
    private String startTime;

    /**
     * 结束日期（年-月-日）（如：2020-10-23）
     */
    @JsonProperty("end_date")
    private String endDate;

    /**
     * 结束时间（时:分）（如：18:30）
     */
    @JsonProperty("end_time")
    private String endTime;

    /**
     * 星期几（逗号分隔）（如：1,2,3 表示周1，周2，周3） 非必填
     */
    @JsonProperty("week_day")
    private String weekDay;

    /**
     * PDF自动播放(1=开启，0=关闭)，不传默认1    非必填
     */
    @JsonProperty("auto_play")
    private Integer autoPlay;

    /**
     * PDF播放方式（1=滚动，0=翻页），不传默认1    非必填
     */
    @JsonProperty("play_show_type")
    private Integer playShowType;

    /**
     * PDF播放速度（1-10，由快到慢），不传默认10    非必填
     */
    @JsonProperty("play_speed")
    private Integer playSpeed;
}
