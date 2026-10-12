package com.guhecloud.cube.srv.gen.conferencemanage.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookRoomService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookJob {
    @Autowired
    private BookRoomService bookRoomService;

    /**
     * 预约结束Job
     */
    @JobAnnotation(name = "bookEndJob", cron = "0 0/2 * * * ?", desc = "预约结束Job")
    public void executeBookEndJob(String param) {
        var date = DateUtils.now();
        var time = DateUtils.format(date, "yyyy-MM-dd HH:mm:00");
        try {
            var list = bookRoomService.list(Wrappers.<BookRoom>lambdaQuery()
                                                    .eq(BookRoom::getEndBookTime, time)
                                                    .eq(BookRoom::getStatus, BookRoomEnum.RESERVED.name()));
            list.forEach(s -> {
                s.setStatus(BookRoomEnum.FINISH);
            });
            bookRoomService.updateBatchById(list);
        } catch (Exception e) {
            log.error("预约结束job执行失败", e);
        }
    }
}
