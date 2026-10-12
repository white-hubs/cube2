package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 查询条件类 - 用于构建自定义查询条件
 */
@Data
public class QueryCondition {
    private final String whereClause;
    private final List<Object> params;

    public QueryCondition(String whereClause, List<Object> params) {
        this.whereClause = whereClause == null ? "" : whereClause;
        this.params = params == null ? new ArrayList<>() : params;
    }

    /**
     * 创建空的查询条件
     */
    public static QueryCondition empty() {
        return new QueryCondition("", new ArrayList<>());
    }

    /**
     * 构建查询条件构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 查询条件构建器
     */
    public static class Builder {
        private StringBuilder whereClause = new StringBuilder();
        private List<Object> params = new ArrayList<>();
        private boolean hasWhere = false;

        /**
         * 添加时间范围条件
         */
        public Builder addTimeRange(String startTime, String endTime, String timeField) {
            if (startTime != null && endTime != null) {
                appendCondition(timeField + " >= ? and " + timeField + " <= ?");
                params.add(startTime);
                params.add(endTime);
            } else if (startTime != null) {
                appendCondition(timeField + " >= ?");
                params.add(startTime);
            } else if (endTime != null) {
                appendCondition(timeField + " <= ?");
                params.add(endTime);
            }
            return this;
        }

        /**
         * 添加 IN 条件
         */
        public Builder addInCondition(String field, Set<?> values) {
            if (values != null && !values.isEmpty()) {
                StringBuilder inClause = new StringBuilder();
                inClause.append(field).append(" in (");
                boolean first = true;
                for (Object value : values) {
                    if (!first) {
                        inClause.append(",");
                    }
                    inClause.append("?");
                    params.add(value);
                    first = false;
                }
                inClause.append(")");
                appendCondition(inClause.toString());
            }
            return this;
        }

        /**
         * 添加等于条件
         */
        public Builder addEqCondition(String field, Object value) {
            if (value != null) {
                appendCondition(field + " = ?");
                params.add(value);
            }
            return this;
        }

        /**
         * 添加模糊查询条件（LIKE %keyword%）
         */
        public Builder addLikeCondition(String field, String keyword) {
            if (StringUtils.isNotBlank(keyword)) {
                String trimmed = keyword.trim();
                if (!trimmed.isEmpty()) {
                    appendCondition(field + " like ?");
                    params.add("%" + trimmed + "%");
                }
            }
            return this;
        }

        /**
         * 添加自定义条件
         */
        public Builder addCustomCondition(String clause, Object... paramValues) {
            if (clause != null && !clause.trim().isEmpty()) {
                appendCondition(clause);
                if (paramValues != null) {
                    for (Object value : paramValues) {
                        params.add(value);
                    }
                }
            }
            return this;
        }

        /**
         * 构建查询条件
         */
        public QueryCondition build() {
            String finalClause = whereClause.toString().trim();
            if (!finalClause.isEmpty() && !finalClause.toLowerCase().startsWith("where")) {
                finalClause = "where " + finalClause;
            }
            return new QueryCondition(finalClause, params);
        }

        private void appendCondition(String clause) {
            if (hasWhere) {
                whereClause.append(" and ");
            } else {
                hasWhere = true;
            }
            whereClause.append(clause);
        }
    }
}
