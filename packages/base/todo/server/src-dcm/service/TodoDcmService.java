package com.guhecloud.cube.biz.todo.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface TodoDcmService {

    List<RequestedConfigItem> fetchRuntimeTodoTagConfig();

    List<RequestedConfigItem> applyTodoTagConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeTodoTagRefBusinessTypeConfig();

    List<RequestedConfigItem> applyTodoTagRefBusinessTypeConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeTodoBusinessUrConfig();

    List<RequestedConfigItem> applyTodoBusinessUrlConfig(List<RequestedConfigItem> diffs2Apply);
}
