package com.guhecloud.cube.biz.todo.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.biz.todo.dcm.service.TodoDcmService;
import com.guhecloud.cube.biz.todo.dcm.support.TodoBusinessUrlDcmHelper;
import com.guhecloud.cube.biz.todo.dcm.support.TodoTagDcmHelper;
import com.guhecloud.cube.biz.todo.dcm.support.TodoTagReferBusinessTypeDcmHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.biz.todo.dcm.support.TodoDcmSupport.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.biz.todo.dcm.support.TodoDcmSupport.CONFIG_TYPE_TODO_BUSINESS_URL;
import static com.guhecloud.cube.biz.todo.dcm.support.TodoDcmSupport.CONFIG_TYPE_TODO_TAG;
import static com.guhecloud.cube.biz.todo.dcm.support.TodoDcmSupport.CONFIG_TYPE_TODO_TAG_REF_BUSINESS_TYPE;
import static com.guhecloud.cube.biz.todo.dcm.support.TodoDcmSupport.PROVIDER_TODO;

@Slf4j
@RestController
@RequestMapping("/todo/dcm/config")
public class TodoDcmController extends AbstractBaseDcmController {

    private final TodoDcmService todoDcmService;

    public TodoDcmController(TodoDcmService todoDcmService) {
        this.todoDcmService = todoDcmService;
    }

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-declaration-todo.yml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_TODO, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerSystemDcmCaps(PROVIDER_TODO, CONFIG_TYPE_TODO_TAG,
                              todoDcmService::fetchRuntimeTodoTagConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, TodoTagDcmHelper.createDcmRciConverter()),
                              todoDcmService::applyTodoTagConfig);
        registerSystemDcmCaps(PROVIDER_TODO, CONFIG_TYPE_TODO_TAG_REF_BUSINESS_TYPE,
                              todoDcmService::fetchRuntimeTodoTagRefBusinessTypeConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, TodoTagReferBusinessTypeDcmHelper.createDcmRciConverter()),
                              todoDcmService::applyTodoTagRefBusinessTypeConfig);
        registerSystemDcmCaps(PROVIDER_TODO, CONFIG_TYPE_TODO_BUSINESS_URL,
                              todoDcmService::fetchRuntimeTodoBusinessUrConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, TodoBusinessUrlDcmHelper.createDcmRciConverter()),
                              todoDcmService::applyTodoBusinessUrlConfig);
    }

}
