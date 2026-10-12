package com.guhecloud.cube.srv.gen.official.management.server.enumeration;

import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.DocumentCirculationDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.DocumentReceivedDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkReplyDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkUpdateDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.KeyWorkDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.RegulationDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.SealApprovalDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.TopicForecastDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.UrgentWorkDTO;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 补卡类型枚举
 *
 * @author appet
 * @date 2022/11/28
 */
@Getter
public enum OfficialApprovalBizEnum {
    OFFICIAL_REGULATION("制度征求", RegulationDTO.class),
    OFFICIAL_SEAL("用印申请", SealApprovalDTO.class),
    OFFICIAL_TOPIC("议题预报", TopicForecastDTO.class),
    OFFICIAL_DOCUMENT_CIRCULATION("文件传阅", DocumentCirculationDTO.class),
    OFFICIAL_DOCUMENT_RECEIVED("来文处理", DocumentReceivedDTO.class),
    OFFICIAL_KEY_WORK("重点工作", KeyWorkDTO.class),
    OFFICIAL_URGENT_WORK("催办工作", UrgentWorkDTO.class),
    OFFICIAL_FOCUS_WORK("督办事项", FocusWorkDTO.class),
    OFFICIAL_FOCUS_WORK_REPLY("督办事项反馈", FocusWorkReplyDTO.class),
    OFFICIAL_FOCUS_WORK_UPDATE("督办事项更新", FocusWorkUpdateDTO.class),
    TENDER_REVIEW("招标审议", null),
    TENDER_APPROVAL("招标审批", null),
    TENDER_CONFIRMATION("招标确认", null),
    SEAL_BORROWING("借章申请", null),
    ;

    final String desc;
    final Class<?> clazz;

    OfficialApprovalBizEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }

    public static List<String> getStatEnumList() {
        return Stream.of(OFFICIAL_REGULATION.name(), OFFICIAL_SEAL.name(),
                         OFFICIAL_TOPIC.name(), OFFICIAL_DOCUMENT_CIRCULATION.name(),
                         OFFICIAL_DOCUMENT_RECEIVED.name(), OFFICIAL_KEY_WORK.name(),
                         OFFICIAL_URGENT_WORK.name(), OFFICIAL_FOCUS_WORK.name()
                        )
                .collect(Collectors.toList());
    }

}
