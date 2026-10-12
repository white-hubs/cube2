package com.guhecloud.cube.base.flow.flowable.data;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/19 16:19
 * @description 任务表单数据对象
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskFormDataObject {
    private Long formInstanceId;
    private List<String> values = new ArrayList<>(2);

    public TaskFormDataObject(Long formInstanceId) {
        this.formInstanceId = formInstanceId;
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
