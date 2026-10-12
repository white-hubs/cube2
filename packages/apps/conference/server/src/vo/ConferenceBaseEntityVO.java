package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceBaseEntityVO {
    private Long id;
    private String name;
    private Boolean status;
}
