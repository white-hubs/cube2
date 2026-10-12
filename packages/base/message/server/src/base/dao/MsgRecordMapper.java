package com.guhecloud.cube.base.message2.server.base.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecord;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryParam;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordApplicationTotalVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordPageVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordTypeTotalVO;
import com.guhecloud.cube.base.message2.server.bff.param.MsgCustomGroupParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 消息记录接口
 *
 * @author lanchun
 * @date 2021/11/9 19:13
 */
@Mapper
public interface MsgRecordMapper extends BaseMapper<MsgRecord> {

    Page<MsgRecordPageVO> page(Page<?> page, @Param("param") MsgRecordQueryParam param, @Param("terminal") String terminal,
                               @Param("userId") Long userId
                              );

    @InterceptorIgnore(tenantLine = "true")
    Page<MsgRecordPageVO> pageByTenant(Page<?> page, @Param("param") MsgRecordQueryParam param, @Param("terminal") String terminal,
                                       @Param("userId") Long userId
                                      );

    List<MsgRecordTypeTotalVO> selectMsgTypeUnreadTotal(@Param("channelType") ChannelEnum channelType,
                                                        @Param("terminal") String terminal, @Param("userId") Long userId
                                                       );

    List<MsgRecordApplicationTotalVO> selectMsgApplicationUnreadTotal(@Param("channelType") ChannelEnum channelType,
                                                                      @Param("terminal") String terminal, @Param("userId") Long userId,
                                                                      @Param("customGroups") Set<MsgCustomGroupParam> msgCustomGroupParams
                                                                     );

    List<MsgRecordApplicationTotalVO> selectMsgApplication(@Param("channelType") ChannelEnum channelType,
                                                           @Param("terminal") String terminal, @Param("userId") Long userId
                                                          );

    Integer selectUnReadMsgRecordCount(@Param("channelType") ChannelEnum channelType, @Param("terminal") String terminal,
                                       @Param("userId") Long userId, @Param("customGroup") MsgCustomGroupParam msgCustomGroupParam
                                      );

    MsgRecordPageVO selectLastMsgRecords(@Param("channelType") ChannelEnum channelType, @Param("terminal") String terminal,
                                         @Param("userId") Long userId, @Param("customGroup") MsgCustomGroupParam msgCustomGroupParam
                                        );

    MsgRecordPageVO selectAppLastMsgRecords(@Param("channelType") ChannelEnum channelType, @Param("terminal") String terminal,
                                            @Param("userId") Long userId, @Param("applicationCode") String applicationCode,
                                            @Param("customGroups") Set<MsgCustomGroupParam> msgCustomGroupParams
                                           );

    int deletedByBusinessIds(@Param("businessIds") Set<String> businessIds, @Param("deleteTimestamp") Long deleteTimestamp);
}

