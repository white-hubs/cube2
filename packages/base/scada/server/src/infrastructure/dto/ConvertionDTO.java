package com.guhecloud.kaihe.scada.server.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class ConvertionDTO<K, V> {

    @JsonIgnore
    private MapperFactory mapperFactory;

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
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.configurer(mapperFactory);
    }

    protected void configurer(MapperFactory mapperFactory) {
    }

    protected void map(V v) {
        if (v == null) {
            nullTarget = true;
        } else {
            mapperFactory.getMapperFacade().map(v, this);
        }
    }

    protected void map(List<V> list) {
        this.list = new ArrayList<>();
        list.forEach(v -> {
            this.list.add(mapperFactory.getMapperFacade().map(v, kClass));
        });
    }

    public List<K> list() {
        return this.list;
    }

    public V convert() {
        return mapperFactory.getMapperFacade().map(this, vClass);
    }

    public boolean isNullTarget() {
        return nullTarget;
    }

}
