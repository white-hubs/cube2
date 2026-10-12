package com.guhecloud.cube.srv.gen.personnel.management.server.event.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DelStaffMessage {
    public static final String TOPIC = "DEL_MESSAGE";

    private Long staffId;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 租户。
     */
    private Integer tenant;

    public DelStaffMessage(Long staffId) {
        this.staffId = staffId;
    }
}
