-- =============================================================
-- 医疗系统数据库 · 实验三增量建表脚本
-- 在实验二 schema.sql 生成的 5 张表基础上执行本脚本
-- 新增：医生排班、药品库存、处方明细；挂号表追加排班外键与防重复约束
-- 目标库：medical_system    字符集：utf8mb4    引擎：InnoDB
-- =============================================================

USE medical_system;
SET NAMES utf8mb4;

-- -------------------------------------------------------------
-- 1. 医生排班表 doctor_schedule（号源管理的核心）
--    剩余号源 remaining 用带条件 UPDATE 扣减，防止超卖
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS doctor_schedule (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '排班主键',
  doctor_id   BIGINT UNSIGNED NOT NULL COMMENT '医生外键',
  work_date   DATE         NOT NULL COMMENT '出诊日期',
  time_slot   TINYINT      NOT NULL COMMENT '时段：1-上午，2-下午',
  total_quota INT          NOT NULL COMMENT '总号源',
  remaining   INT          NOT NULL COMMENT '剩余号源',
  fee         DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '该排班挂号费',
  status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-可约，0-停诊',
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_sched (doctor_id, work_date, time_slot),
  CONSTRAINT fk_sched_doctor FOREIGN KEY (doctor_id) REFERENCES doctor (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医生排班表';

-- -------------------------------------------------------------
-- 2. 药品表 medicine（库存扣减的载体）
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS medicine (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '药品主键',
  medicine_no VARCHAR(32)  NOT NULL COMMENT '药品编码',
  name        VARCHAR(64)  NOT NULL COMMENT '药品名称',
  spec        VARCHAR(64)           DEFAULT NULL COMMENT '规格',
  price       DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
  stock       INT          NOT NULL DEFAULT 0 COMMENT '库存数量',
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_medicine_no (medicine_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='药品表';

-- -------------------------------------------------------------
-- 3. 处方明细表 prescription_item（处方与药品的多对多拆表）
--    实验二 prescription 注释里"药品明细留待后续实验拆表"即指此表
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS prescription_item (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '明细主键',
  presc_id    BIGINT UNSIGNED NOT NULL COMMENT '处方外键',
  medicine_id BIGINT UNSIGNED NOT NULL COMMENT '药品外键',
  quantity    INT          NOT NULL COMMENT '数量',
  unit_price  DECIMAL(10,2) NOT NULL COMMENT '开方时单价（快照，避免后续调价影响历史处方）',
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_presc_med (presc_id, medicine_id),
  CONSTRAINT fk_item_presc FOREIGN KEY (presc_id) REFERENCES prescription (id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_item_medicine FOREIGN KEY (medicine_id) REFERENCES medicine (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='处方明细表';

-- -------------------------------------------------------------
-- 4. 挂号表追加排班外键 + 防重复挂号唯一键
--    MySQL 用 ALTER TABLE ... ADD 做增量，不重建表、不丢已有数据
-- -------------------------------------------------------------
ALTER TABLE registration
  ADD COLUMN schedule_id BIGINT UNSIGNED DEFAULT NULL COMMENT '排班外键' AFTER doctor_id;

ALTER TABLE registration
  ADD CONSTRAINT fk_reg_schedule FOREIGN KEY (schedule_id) REFERENCES doctor_schedule (id)
    ON DELETE RESTRICT ON UPDATE CASCADE;

-- 同一患者对同一排班只能挂一次号：重复挂号由数据库约束兜底
ALTER TABLE registration
  ADD UNIQUE KEY uk_reg_patient_sched (patient_id, schedule_id);

-- =============================================================
-- 测试数据
-- =============================================================

-- 排班：每个医生上午/下午各一班，号源 5 个，便于演示"号源已满"
INSERT INTO doctor_schedule (doctor_id, work_date, time_slot, total_quota, remaining, fee, status) VALUES
(1, CURDATE(),          1, 5, 5, 20.00, 1),
(1, CURDATE(),          2, 5, 5, 20.00, 1),
(2, CURDATE(),          1, 5, 5, 20.00, 1),
(3, CURDATE(),          1, 3, 3, 30.00, 1),
(4, CURDATE(),          2, 5, 5, 30.00, 1),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 5, 5, 15.00, 1);

-- 药品：库存故意留一个较小的（VC 片 3 盒），便于演示"库存不足"
INSERT INTO medicine (medicine_no, name, spec, price, stock) VALUES
('M0001', '阿莫西林胶囊', '0.25g × 24粒', 18.50, 50),
('M0002', '维生素C片',   '0.1g × 100片',  6.80,  3),
('M0003', '布洛芬缓释胶囊', '0.3g × 20粒', 22.00, 30),
('M0004', '生理盐水',     '250ml',         4.50, 100);

-- -------------------------------------------------------------
-- 验证用 SQL（截图时可直接在数据库工具里跑）
-- -------------------------------------------------------------
-- SELECT id, doctor_id, work_date, time_slot, total_quota, remaining FROM doctor_schedule;
-- SELECT id, medicine_no, name, stock FROM medicine;
-- SHOW CREATE TABLE registration;
