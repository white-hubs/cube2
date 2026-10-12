package com.guhecloud.cube.deliver.project.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 设备相关通知（目前只有到期通知）
 * </p>
 *
 * @author bq
 * @since 2022-08-31
 */
@Mapper
public interface DeviceNoticeMapper extends BaseMapper<DeviceNotice> {

}
