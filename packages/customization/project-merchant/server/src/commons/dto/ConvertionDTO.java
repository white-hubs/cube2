package com.guhecloud.cube.deliver.project.merchant.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class ConvertionDTO<K, V> {

    @JsonIgnore
    public static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    @JsonIgnore
    private Class<K> kClass;
    @JsonIgnore
    private Class<V> vClass;

    @JsonIgnore
    private boolean nullTarget;
    @JsonIgnore
    private List<K> list;

    @SuppressWarnings("unchecked")
	protected ConvertionDTO() {
        Type[] params = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        kClass = (Class<K>) params[0];
        vClass = (Class<V>) params[1];
    }

    protected void map(V v) {
        if (v == null) {
            nullTarget = true;
        } else {
        	MAPPER_FACTORY.getMapperFacade().map(v, this);
        }
    }

    protected void map(List<V> list) {
        this.list = new ArrayList<>();
        list.forEach(v -> {
            this.list.add(MAPPER_FACTORY.getMapperFacade().map(v, kClass));
        });
    }

    public List<K> list() {
        return this.list;
    }

    public V convert() {
        return MAPPER_FACTORY.getMapperFacade().map(this, vClass);
    }

    public boolean isNullTarget() {
        return nullTarget;
    }

}
