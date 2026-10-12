package com.guhecloud.cube.base.device.center.metadata.util;

/**
 * tuple 2<br/>
 * 不支持反射
 *
 * @author fl
 * @date 2022/11/3
 **/
public class Tuple2<T1, T2> {
    public final T1 t1;
    public final T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

}
