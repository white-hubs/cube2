package com.guhecloud.cube.srv.gen.conferencemanage.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookIntervalRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookRoomMapper extends BaseMapper<BookRoom> {
    String getConferenceDevice(@Param("conferenceId") Long conferenceId);

    List<BookIntervalRecord> getBookIntervalRecords(@Param("date") String date, @Param("ids") List<Long> ids);

    BookRoomVo getBookRoomVo(@Param("bookId") Long bookId);

    Page<BookRoomAppVo> getAppMyAppointment(Page page, @Param("userId") Long userId);

    List<BookRoomAppVo> getAppMyAppointment(@Param("userId") Long userId);
}
