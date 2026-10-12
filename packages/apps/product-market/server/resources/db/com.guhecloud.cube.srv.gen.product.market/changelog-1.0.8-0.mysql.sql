-- liquibase formatted sql

-- 删除商品品类名称唯一约束
-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'market_goods_category' AND index_name = 'idx_goods_category_tenant_deleted_name') AS column_exists;
ALTER TABLE market_goods_category DROP INDEX idx_goods_category_tenant_deleted_name;