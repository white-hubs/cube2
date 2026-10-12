package com.guhecloud.platform.base.model;

public record Pair<L, R>(L left, R right) {
    /**
     * @deprecated 为替代 lang3 Pair 写的兼容方法，后续会删除
     */
    @Deprecated
    public L getLeft() {
        return left;
    }

    /**
     * @deprecated 为替代 lang3 Pair 写的兼容方法，后续会删除
     */
    public L getKey() {
        return left;
    }

    /**
     * @deprecated 为替代 lang3 Pair 写的兼容方法，后续会删除
     */
    @Deprecated
    public R getRight() {
        return right;
    }

    /**
     * @deprecated 为替代 lang3 Pair 写的兼容方法，后续会删除
     */
    public R getValue() {
        return right;
    }

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
