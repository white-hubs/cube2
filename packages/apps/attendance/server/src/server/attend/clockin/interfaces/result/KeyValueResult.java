package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-27  09:27
 * @Description:
 * @Version: 1.0
 */
@Data
public class KeyValueResult {
    private String name;
    private String value;

    public KeyValueResult(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
