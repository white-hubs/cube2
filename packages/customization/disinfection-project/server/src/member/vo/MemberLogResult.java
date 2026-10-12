package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberLogResult {
    private Long id;
    private String title;
    private String detail;
    private MemberLogTypeEnum type;
    private LocalDateTime createTime;
}
