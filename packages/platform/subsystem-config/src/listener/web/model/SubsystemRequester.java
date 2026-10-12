package com.guhecloud.subsystem.config.listener.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SubsystemRequester implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子系统编号
     */
    private String subsystemCode;

    /**
     * 厂家编号
     */
    private String manufacturer;
}
