package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 使Function获取序列化能力
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {
 
}