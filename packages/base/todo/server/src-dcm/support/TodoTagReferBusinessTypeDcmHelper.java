package com.guhecloud.cube.biz.todo.dcm.support;

import com.guhecloud.cube.biz.todo.dcm.entity.DcmTodoTagReferBusinessType;
import com.guhecloud.cube.biz.todo.server.entity.TodoTagReferBusinessType;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static com.guhecloud.cube.biz.todo.dcm.support.TodoDcmSupport.KW_KEY_TODO_TAG_CODE;

public class TodoTagReferBusinessTypeDcmHelper extends AbstractGroupedDcmHelper<TodoTagReferBusinessType, DcmTodoTagReferBusinessType> {

    public TodoTagReferBusinessTypeDcmHelper() {
        super(TodoTagReferBusinessType.class, DcmTodoTagReferBusinessType.class);
    }

    public static IDcmRciConverter<DcmTodoTagReferBusinessType> createDcmRciConverter() {
        return new TodoTagReferBusinessTypeDcmHelper();
    }

    @Override
    public String getBizKey(TodoTagReferBusinessType todoTagReferBusinessType) {
        return String.format("%s:%s", todoTagReferBusinessType.getTodoTagCode(), todoTagReferBusinessType.getBusinessType());
    }

    @Override
    public void updateBizWithDcm(TodoTagReferBusinessType todoTagReferBusinessType,
                                 DcmTodoTagReferBusinessType dcmTodoTagReferBusinessType
                                ) {

    }

    @Override
    protected String getConfigType() {
        return TodoDcmSupport.CONFIG_TYPE_TODO_TAG_REF_BUSINESS_TYPE;
    }

    @Override
    protected Object generateGroupKey(DcmTodoTagReferBusinessType dcmTodoTagReferBusinessType) {
        return dcmTodoTagReferBusinessType.getTodoTag();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey,
                                                            Collection<? extends DcmTodoTagReferBusinessType> dcmEntities
                                                           ) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setKwargs(KW_KEY_TODO_TAG_CODE, (String) groupKey);
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmTodoTagReferBusinessType> dcmEntities, RequestedConfigItem rci) {
        String todoTag = rci.getKwargs(KW_KEY_TODO_TAG_CODE, null);
        Objects.requireNonNull(todoTag, "todoTag code is null");
        dcmEntities.forEach(dcm -> dcm.setTodoTag(todoTag));
    }

    @Override
    protected void updateBizGroupInfoWithDcm(TodoTagReferBusinessType todoTagReferBusinessType,
                                             DcmTodoTagReferBusinessType dcmTodoTagReferBusinessType
                                            ) {
        todoTagReferBusinessType.setTodoTagCode(dcmTodoTagReferBusinessType.getTodoTag());
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmTodoTagReferBusinessType dcmTodoTagReferBusinessType,
                                             TodoTagReferBusinessType todoTagReferBusinessType
                                            ) {
        dcmTodoTagReferBusinessType.setTodoTag(todoTagReferBusinessType.getTodoTagCode());
    }

    @Override
    protected DcmTodoTagReferBusinessType dcmFromString(String object) {
        DcmTodoTagReferBusinessType dcmTodoTagReferBusinessType = new DcmTodoTagReferBusinessType();
        dcmTodoTagReferBusinessType.setBusinessType(object);
        return dcmTodoTagReferBusinessType;
    }

    @Override
    public DcmTodoTagReferBusinessType convertBiz2Dcm(TodoTagReferBusinessType todoTagReferBusinessType) {
        return new DcmTodoTagReferBusinessType(todoTagReferBusinessType.getTodoTagCode(), todoTagReferBusinessType.getBusinessType());
    }

    @Override
    public TodoTagReferBusinessType convertDcm2Biz(DcmTodoTagReferBusinessType dcmEntity) {
        return TodoTagReferBusinessType.builder()
                .todoTagCode(dcmEntity.getTodoTag())
                .businessType(dcmEntity.getBusinessType())
                .build();
    }
}
