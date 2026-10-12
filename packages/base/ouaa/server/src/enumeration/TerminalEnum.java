package com.guhecloud.cube.biz.ouaa.server.enumeration;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.UnimplementedException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TerminalEnum {
    WEB("bimops-manage-web"),
    APP("bimops-manage-app"),
    MP("bimops-manage-mp");

    private final String terminal;

    public static boolean isWeb() {
        String curTerminal = SessionContextHolder.getRequester().getTerminal();
        return WEB.getTerminal().equals(curTerminal);
    }

    public static TerminalEnum build(String terminal) {
        for (TerminalEnum value : values()) {
            if (value.getTerminal().equals(terminal)) {
                return value;
            }
        }
        throw new UnimplementedException("不支持的终端！");
    }
}
