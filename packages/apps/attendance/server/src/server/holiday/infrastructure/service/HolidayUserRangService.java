package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayUserRangMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayUserRangPO;
import org.springframework.stereotype.Service;

/**
 * 只是为了方便数据库操作,没有业务方法,还是用JPA好啊
 */
@Service
public class HolidayUserRangService extends ServiceImpl<HolidayUserRangMapper, HolidayUserRangPO> {
}
