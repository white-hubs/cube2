-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_item_template' AND column_name='image_flag') AS column_exists;
ALTER TABLE inspect_item_template CHANGE image_flag image_result VARCHAR(200) NULL COMMENT '要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)';
ALTER TABLE inspect_item_template MODIFY COLUMN image_result VARCHAR(200) NULL COMMENT '要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)';
-- update inspect_item_template set image_result = 'ANORMAL' where image_result = '1';
-- update inspect_standard_item set image_result = null where image_result = '0';

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_item_template' AND column_name='remark_result') AS column_exists;
ALTER TABLE inspect_item_template ADD remark_result varchar(200) NULL COMMENT '要填写描述的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)';
ALTER TABLE inspect_item_template CHANGE remark_result remark_result varchar(200) NULL COMMENT '要填写描述的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)' AFTER result_item_value;


-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_standard_item' AND column_name='image_flag') AS column_exists;
ALTER TABLE inspect_standard_item CHANGE image_flag image_result VARCHAR(200) NULL COMMENT '要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)';
ALTER TABLE inspect_standard_item MODIFY COLUMN image_result VARCHAR(200) NULL COMMENT '要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)';
-- update inspect_standard_item set image_result = 'ANORMAL' where image_result = '1';
-- update inspect_standard_item set image_result = null where image_result = '0';

-- changeset chx:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_standard_item' AND column_name='remark_result') AS column_exists;
ALTER TABLE inspect_standard_item ADD remark_result varchar(200) NULL COMMENT '要填写描述的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)';
ALTER TABLE inspect_standard_item CHANGE remark_result remark_result varchar(200) NULL COMMENT '要填写描述的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)' AFTER result_item_value;


-- changeset chx:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record_item' AND column_name='item_image_flag') AS column_exists;
ALTER TABLE inspect_record_item CHANGE item_image_flag item_image_result VARCHAR(200) NULL COMMENT '要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)';
ALTER TABLE inspect_record_item MODIFY COLUMN item_image_result VARCHAR(200) NULL COMMENT '要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)';
-- update inspect_record_item set image_result = 'ANORMAL' where image_result = '1';
-- update inspect_standard_item set image_result = null where image_result = '0';

-- changeset chx:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record_item' AND column_name='remark_result') AS column_exists;
ALTER TABLE inspect_record_item ADD remark_result varchar(200) NULL COMMENT '要填写描述的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)';
ALTER TABLE inspect_record_item CHANGE remark_result remark_result varchar(200) NULL COMMENT '要填写描述的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)' AFTER error_value_flag;
