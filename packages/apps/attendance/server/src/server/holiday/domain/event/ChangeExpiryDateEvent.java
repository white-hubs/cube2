package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.event;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 修改假期类型
 */
@Getter
public class ChangeExpiryDateEvent extends ApplicationEvent {
    private final Long holidayTypeId;
    private final ExpiryDate expiryDate;

    public ChangeExpiryDateEvent(Object source, Long holidayTypeId, ExpiryDate expiryDate) {
        super(source);
        this.holidayTypeId = holidayTypeId;
        this.expiryDate = expiryDate;
    }
}
