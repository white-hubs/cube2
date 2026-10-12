package com.guhecloud.platform.security.permission.process;

public abstract class AbstractPermissionFilterChainProcess {

    protected boolean preCheck() {
        return false;
    }

    public boolean doProcess(String permission) {
        if (this.preCheck()) {
            return true;
        }
        return postProcess(permission);
    }

    protected abstract boolean postProcess(String permission);
}
