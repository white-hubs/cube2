package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NfcDetailsPageParam extends Paging {
    /**
     * 芯片/蓝牙编号
     */
    private String keyword;

}
