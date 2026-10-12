package com.guhecloud.cube.base.message2.server.bff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.message2.server.base.dao.MsgRecordMapper;
import com.guhecloud.cube.base.message2.server.base.dao.MsgRecordTargetMapper;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryParam;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordApplicationTotalVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordPageVO;
import com.guhecloud.cube.base.message2.server.bff.constant.BffConstants;
import com.guhecloud.cube.base.message2.server.bff.param.MsgCustomGroupParam;
import com.guhecloud.cube.base.message2.server.bff.param.MsgCustomReadAllParam;
import com.guhecloud.cube.base.message2.server.bff.vo.MsgCustomGroupVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgRecordBffService {

    private final MsgRecordMapper msgRecordMapper;

    private final MsgRecordTargetMapper msgRecordTargetMapper;

    public List<MsgCustomGroupVO> getMsgCustomGroups(ChannelEnum channelType, Set<MsgCustomGroupParam> msgCustomGroupParams) {
        List<MsgCustomGroupVO> msgCustomGroupVOS = new ArrayList<>();
        String terminal = SessionContextHolder.getRequester().getTerminal();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        msgCustomGroupParams.stream().forEach(msgCustomGroupParam -> {
            MsgRecordPageVO msgRecordPageVO = this.msgRecordMapper.selectLastMsgRecords(channelType, terminal, userId, msgCustomGroupParam);
            Integer unReadMsgCount = this.msgRecordMapper.selectUnReadMsgRecordCount(channelType, terminal, userId, msgCustomGroupParam);
            MsgCustomGroupVO msgCustomGroupVO = MsgCustomGroupVO.builder()
                    .type(msgCustomGroupParam.getType())
                    .typeName(msgCustomGroupParam.getTypeName())
                    .category(BffConstants.CUSTOM)
                    .unReadMsgCount(unReadMsgCount)
                    .lastMsgRecord(msgRecordPageVO)
                    .build();
            msgCustomGroupVOS.add(msgCustomGroupVO);
        });
        //按应用分组统计(过滤掉已在自定义分组中的数据)
        List<MsgRecordApplicationTotalVO> msgRecordApplicationTotalVOS = this.msgRecordMapper.selectMsgApplicationUnreadTotal(channelType, terminal, userId, msgCustomGroupParams);
        msgRecordApplicationTotalVOS.stream().forEach(msgRecordApplicationTotalVO -> {
            MsgRecordPageVO msgRecordPageVO = this.msgRecordMapper.selectAppLastMsgRecords(channelType, terminal, userId, msgRecordApplicationTotalVO.getApplicationCode(), msgCustomGroupParams);
            MsgCustomGroupVO msgCustomGroupVO = MsgCustomGroupVO.builder()
                    .type(msgRecordApplicationTotalVO.getApplicationCode())
                    .typeName(msgRecordApplicationTotalVO.getApplicationName())
                    .category(BffConstants.APPLICATION)
                    .unReadMsgCount(msgRecordApplicationTotalVO.getTotal())
                    .lastMsgRecord(msgRecordPageVO)
                    .build();
            msgCustomGroupVOS.add(msgCustomGroupVO);
        });
        return msgCustomGroupVOS;
    }

    public Pagination<MsgRecordPageVO> listMsgRecords(MsgRecordQueryParam param) {
        Page<MsgRecordPageVO> msgRecordPageVOPage = this.msgRecordMapper.page(PagingUtils.convert(param), param, SessionContextHolder
                .getRequester().getTerminal(), SecurityContextHolder.getPrincipal().getId());
        return PagingUtils.convert(msgRecordPageVOPage);
    }

    public void readAll(ChannelEnum channelType, MsgCustomReadAllParam readAllParam) {
        this.msgRecordTargetMapper.readAll(SecurityContextHolder.getPrincipal().getId(), channelType, readAllParam);
    }

}
