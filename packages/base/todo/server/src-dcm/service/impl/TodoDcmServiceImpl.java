package com.guhecloud.cube.biz.todo.dcm.service.impl;

import com.guhecloud.cube.biz.todo.dcm.service.TodoDcmService;
import com.guhecloud.cube.biz.todo.dcm.support.TodoBusinessUrlDcmHelper;
import com.guhecloud.cube.biz.todo.dcm.support.TodoTagDcmHelper;
import com.guhecloud.cube.biz.todo.dcm.support.TodoTagReferBusinessTypeDcmHelper;
import com.guhecloud.cube.biz.todo.server.service.TodoBusinessUrlService;
import com.guhecloud.cube.biz.todo.server.service.TodoTagReferBusinessTypeService;
import com.guhecloud.cube.biz.todo.server.service.TodoTagService;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.guhecloud.platform.dcm.support.DcmSupport.applyDcmConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchRuntimeConfig;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TodoDcmServiceImpl implements TodoDcmService {

    private TodoTagService todoTagService;

    private TodoTagReferBusinessTypeService todoTagReferBusinessTypeService;

    private final TodoBusinessUrlService todoBusinessUrlService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeTodoTagConfig() {
        return fetchRuntimeConfig(todoTagService, new TodoTagDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyTodoTagConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, todoTagService, new TodoTagDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeTodoTagRefBusinessTypeConfig() {
        return fetchRuntimeConfig(todoTagReferBusinessTypeService, new TodoTagReferBusinessTypeDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyTodoTagRefBusinessTypeConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, todoTagReferBusinessTypeService, new TodoTagReferBusinessTypeDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeTodoBusinessUrConfig() {
        return fetchRuntimeConfig(todoBusinessUrlService, new TodoBusinessUrlDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyTodoBusinessUrlConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, todoBusinessUrlService, new TodoBusinessUrlDcmHelper());
    }
}
