package com.guhecloud.cube.srv.gen.energymanagement.server.util;

import java.math.BigDecimal;

/**
 * 仪表读数用量计算，统一处理普通递增、表盘回卷和单次用量异常。
 */
public final class MeterUsageCalculator {
    public static final String REASON_MISSING_READING = "MISSING_READING";
    public static final String REASON_MISSING_ROLLOVER_VALUE = "MISSING_ROLLOVER_VALUE";
    public static final String REASON_NEGATIVE_USAGE = "NEGATIVE_USAGE";
    public static final String REASON_EXCEED_MAX_STEP_USAGE = "EXCEED_MAX_STEP_USAGE";

    private MeterUsageCalculator() {
    }

    public static Result calculate(BigDecimal previous,
                                   BigDecimal current,
                                   BigDecimal rolloverValue,
                                   BigDecimal maxStepUsage
                                  ) {
        if (previous == null || current == null) {
            return Result.abnormal(BigDecimal.ZERO, REASON_MISSING_READING);
        }

        BigDecimal usage;
        if (current.compareTo(previous) >= 0) {
            usage = current.subtract(previous);
        } else {
            if (rolloverValue == null) {
                return Result.abnormal(BigDecimal.ZERO, REASON_MISSING_ROLLOVER_VALUE);
            }
            usage = rolloverValue.subtract(previous).add(current);
        }

        if (usage.compareTo(BigDecimal.ZERO) < 0) {
            return Result.abnormal(BigDecimal.ZERO, REASON_NEGATIVE_USAGE);
        }
        if (maxStepUsage != null && usage.compareTo(maxStepUsage) > 0) {
            return Result.abnormal(BigDecimal.ZERO, REASON_EXCEED_MAX_STEP_USAGE);
        }
        return Result.normal(usage);
    }

    public static final class Result {
        private final BigDecimal usage;
        private final boolean abnormal;
        private final String reason;

        private Result(BigDecimal usage, boolean abnormal, String reason) {
            this.usage = usage;
            this.abnormal = abnormal;
            this.reason = reason;
        }

        public static Result normal(BigDecimal usage) {
            return new Result(usage, false, null);
        }

        public static Result abnormal(BigDecimal usage, String reason) {
            return new Result(usage, true, reason);
        }

        public BigDecimal getUsage() {
            return usage;
        }

        public boolean isAbnormal() {
            return abnormal;
        }

        public String getReason() {
            return reason;
        }
    }
}
