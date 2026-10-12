package com.guhecloud.cube.base.flow.flowable.definition.domain.entity;

import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.CopyUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/8/30 10:03
 * @description 抄送节点
 */
@NoArgsConstructor
public class Copy {

    /**
     * 抄送对象 -- EL表达式
     */
    @Setter
    @Getter
    private String copyUserExpression;

    public Copy(String copyUserExpression) {
        this.copyUserExpression = copyUserExpression;
    }

    private Set<CopyUser> copyUsers;

    public void addCopyUsers(Set<CopyUser> copyUsers) {

    }

    public Set<CopyUser> getCopyUsers() {

        return copyUsers;
    }
}
