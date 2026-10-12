package com.guhecloud.cube.biz.daily.task.migration.convert.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.BasicTenantTreeEntity;
import com.guhecloud.platform.database.entity.Deletable;

public abstract class MigrationBaseConvert<S, T> {

    /**
     * 转换
     *
     * @return 新数据
     */
    public abstract Pagination<T> getAndConvertDataPage(Long current);

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    public abstract IService<T> getService();

    public void setBasicTenant(S s, T t) {
        if (s instanceof BasicTenantEntity<?> source && t instanceof BasicTenantEntity<?> target) {
            target.setCreateBy(source.getCreateBy());
            target.setTenant(source.getTenant());
            target.setUpdateBy(source.getUpdateBy());
            target.setCreateTime(source.getCreateTime());
            target.setUpdateTime(source.getUpdateTime());
            target.setDeleted(source.getDeleted());
        } else if (s instanceof BasicTenantEntity<?> source && t instanceof BasicTenantTreeEntity<?> target) {
            target.setCreateBy(source.getCreateBy());
            target.setTenant(source.getTenant());
            target.setUpdateBy(source.getUpdateBy());
            target.setCreateTime(source.getCreateTime());
            target.setUpdateTime(source.getUpdateTime());
            target.setDeleted(source.getDeleted());
        } else if (s instanceof Deletable<?> source && t instanceof BasicTenantEntity<?> target) {
            target.setDeleted(source.getDeleted());
        }
    }

    /**
     * 清除映射数据
     * 由子类自己决定要不要实现清除逻辑(一般不需要实现)
     */
    public void mapClear() {}
}
