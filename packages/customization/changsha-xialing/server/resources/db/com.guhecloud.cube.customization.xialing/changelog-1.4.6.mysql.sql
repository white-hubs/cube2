-- liquibase formatted sql

-- changeset changsha:warehouse-location-card-quality-inspection-add-extra-fields-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_quality_inspection' AND column_name = 'impurity';
ALTER TABLE `warehouse_location_card_quality_inspection`
  ADD COLUMN `impurity` decimal(24,6) DEFAULT NULL COMMENT '杂质，单位%' AFTER `moisture`,
  MODIFY COLUMN `roughness` decimal(24,6) DEFAULT NULL COMMENT '出糙率，单位%',
  ADD COLUMN `mixed_rate` decimal(24,6) DEFAULT NULL COMMENT '互混率，单位%' AFTER `color_smell`,
  ADD COLUMN `head_rice_rate` decimal(24,6) DEFAULT NULL COMMENT '整精米率，单位%' AFTER `mixed_rate`,
  ADD COLUMN `out_of_paddy_brown_rice_rate` decimal(24,6) DEFAULT NULL COMMENT '谷外糙米，单位%' AFTER `head_rice_rate`,
  ADD COLUMN `yellow_rice_rate` decimal(24,6) DEFAULT NULL COMMENT '黄粒米，单位%' AFTER `out_of_paddy_brown_rice_rate`,
  ADD COLUMN `taste_score` decimal(24,6) DEFAULT NULL COMMENT '品尝分值' AFTER `yellow_rice_rate`,
  MODIFY COLUMN `fatty_acid` decimal(24,6) DEFAULT NULL COMMENT '脂肪酸值，单位mgKOH/100g',
  ADD COLUMN `cadmium` decimal(24,6) DEFAULT NULL COMMENT '镉，单位mg/kg' AFTER `fatty_acid`,
  ADD COLUMN `aflatoxin_b1` decimal(24,6) DEFAULT NULL COMMENT '黄曲霉毒素B1，单位μg/kg' AFTER `cadmium`,
  ADD COLUMN `lead` decimal(24,6) DEFAULT NULL COMMENT '铅，单位mg/kg' AFTER `aflatoxin_b1`,
  ADD COLUMN `mercury` decimal(24,6) DEFAULT NULL COMMENT '汞，单位mg/kg' AFTER `lead`,
  ADD COLUMN `inorganic_arsenic` decimal(24,6) DEFAULT NULL COMMENT '无机砷，单位mg/kg' AFTER `mercury`;
