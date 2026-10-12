package com.guhecloud.cube.biz.todo.dcm.support;

import com.guhecloud.cube.biz.todo.dcm.entity.DcmTodoTag;
import com.guhecloud.cube.biz.todo.server.entity.TodoTag;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

public class TodoTagDcmHelper extends AbstractSimpleDcmHelper<TodoTag, DcmTodoTag> {

    public TodoTagDcmHelper() {
        super(TodoTag.class, DcmTodoTag.class);
    }

    public static IDcmRciConverter<DcmTodoTag> createDcmRciConverter() {
        return new TodoTagDcmHelper();
    }

    @Override
    public String getBizKey(TodoTag todoTag) {
        return todoTag.getCode();
    }

    @Override
    public void updateBizWithDcm(TodoTag todoTag, DcmTodoTag dcmTodoTag) {
        todoTag.setName(dcmTodoTag.getName() == null ? "" : dcmTodoTag.getName());
    }

    @Override
    protected String getConfigType() {
        return TodoDcmSupport.CONFIG_TYPE_TODO_TAG;
    }
}
