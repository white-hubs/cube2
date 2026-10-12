package com.guhecloud.cube.iot.mgt.core.def;

/**
 * 时间范围感知接口
 * 用于统一处理查询对象的默认时间范围设置
 */
public interface TimeRangeAware {

    /**
     * 获取开始时间
     *
     * @return 开始时间字符串
     */
    String getStartTime();

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间字符串
     */
    void setStartTime(String startTime);

    /**
     * 获取结束时间
     *
     * @return 结束时间字符串
     */
    String getEndTime();

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间字符串
     */
    void setEndTime(String endTime);
}
