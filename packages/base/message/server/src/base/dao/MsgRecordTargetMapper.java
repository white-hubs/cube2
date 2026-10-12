package com.guhecloud.cube.base.message2.server.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTarget;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.bff.param.MsgCustomReadAllParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 消息记录对象接口
 *
 * @author lanchun
 * @date 2021/11/9 19:12
 */
@Mapper
public interface MsgRecordTargetMapper extends BaseMapper<MsgRecordTarget> {

    List<MsgRecordTarget> failList(@Param("channelType") ChannelEnum channelType);

    void readAll(@Param("userId") Long userId, @Param("channelType") ChannelEnum ChannelEnum,
                 @Param("param") MsgCustomReadAllParam readAllParam
                );

    void readAllMsgType(@Param("userId") Long userId, @Param("channelType") ChannelEnum ChannelEnum,
                        @Param("msgType") String msgType
                       );

    void readAllApplicationMsg(@Param("userId") Long userId, @Param("channelType") ChannelEnum ChannelEnum,
                               @Param("applicationCode") String applicationCode
                              );

    void readBatch(@Param("userId") Long userId, @Param("msgRecordIds") List<Long> msgRecordIds);

    int deletedByBusinessIds(@Param("businessIds") Set<String> businessIds, @Param("deleteTimestamp") Long deleteTimestamp);
}
