package com.guhecloud.cube.base.message2.server.sat.process.pipeline;

import java.util.List;

/**
 * 处理器模板配置(作用是将一组业务处理器放在同一个模板下)
 *
 * @author lanchun
 * @date 2023/5/8 11:43
 */
public class ProcessTemplate<T extends ProcessModel> {

    private List<BusinessProcess<T>> processes;

    public List<BusinessProcess<T>> getProcesses() {
        return processes;
    }

    public void setProcesses(List<BusinessProcess<T>> processes) {
        this.processes = processes;
    }
}
