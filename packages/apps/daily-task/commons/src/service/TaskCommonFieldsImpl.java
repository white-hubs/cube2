//package com.guhecloud.cube.biz.daily.task.commons.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TaskCommonFieldsImpl {
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    /**
//     * 查询公共字段 - 基于接口
//     */
//    public <T extends TaskCommonFields> List<T> queryCommonFields(Class<T> entityClass, CommonQueryParam param) {
//        BaseMapper<T> mapper = getMapper(entityClass);
//        QueryWrapper<T> wrapper = buildCommonWrapper(param);
//
//        // 选择公共字段
//        wrapper.select(getCommonFieldColumns());
//
//        return mapper.selectList(wrapper);
//    }
//
//    /**
//     * 分页查询公共字段
//     */
//    public <T extends TaskCommonFields> Page<T> queryCommonFieldsPage(Class<T> entityClass, CommonQueryParam param, Page<T> page) {
//        BaseMapper<T> mapper = getMapper(entityClass);
//        QueryWrapper<T> wrapper = buildCommonWrapper(param);
//
//        // 选择公共字段
//        wrapper.select(getCommonFieldColumns());
//
//        return mapper.selectPage(page, wrapper);
//    }
//
//    /**
//     * 构建查询条件
//     */
//    private <T extends TaskCommonFields> QueryWrapper<T> buildCommonWrapper(CommonQueryParam param) {
//        QueryWrapper<T> wrapper = new QueryWrapper<>();
//
//        if (StringUtils.isNotBlank(param.getName())) {
//            wrapper.like("name", param.getName());
//        }
//
//        if (StringUtils.isNotBlank(param.getCode())) {
//            wrapper.eq("code", param.getCode());
//        }
//
//        if (param.getStartTime() != null) {
//            wrapper.ge("create_time", param.getStartTime());
//        }
//
//        if (param.getEndTime() != null) {
//            wrapper.le("create_time", param.getEndTime());
//        }
//
//        return wrapper;
//    }
//
//    /**
//     * 获取公共字段的数据库列名
//     */
//    private String[] getCommonFieldColumns() {
//        return new String[]{
//                "id", "name", "code", "description", "create_time"
//        };
//    }
//
//    @SuppressWarnings("unchecked")
//    private <T> BaseMapper<T> getMapper(Class<T> entityClass) {
//        String mapperBeanName = entityClass.getSimpleName() + "Mapper";
//        return (BaseMapper<T>) applicationContext.getBean(mapperBeanName);
//    }
//}
