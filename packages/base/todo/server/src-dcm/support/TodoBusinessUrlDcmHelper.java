package com.guhecloud.cube.biz.todo.dcm.support;

import com.guhecloud.cube.biz.todo.dcm.entity.DcmTodoBusinessUrl;
import com.guhecloud.cube.biz.todo.server.entity.TodoBusinessUrl;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.biz.todo.dcm.support.TodoDcmSupport.CONFIG_TYPE_TODO_BUSINESS_URL;

public class TodoBusinessUrlDcmHelper extends AbstractSimpleDcmHelper<TodoBusinessUrl, DcmTodoBusinessUrl> {

    public TodoBusinessUrlDcmHelper() {
        super(TodoBusinessUrl.class, DcmTodoBusinessUrl.class);
    }

    public static IDcmRciConverter<DcmTodoBusinessUrl> createDcmRciConverter() {
        return new TodoBusinessUrlDcmHelper();
    }

    @Override
    public String getBizKey(TodoBusinessUrl todoBusinessUrl) {
        return String.format("%s:%s", todoBusinessUrl.getBusinessType(), todoBusinessUrl.getTerminal());
    }

    @Override
    public void updateBizWithDcm(TodoBusinessUrl todoBusinessUrl, DcmTodoBusinessUrl dcmTodoBusinessUrl) {
        todoBusinessUrl.setBusinessTypeName(
                dcmTodoBusinessUrl.getBusinessTypeName() == null ? "" : dcmTodoBusinessUrl.getBusinessTypeName());
        todoBusinessUrl.setUrl(dcmTodoBusinessUrl.getUrl() == null ? "" : dcmTodoBusinessUrl.getUrl());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_TODO_BUSINESS_URL;
    }
}
