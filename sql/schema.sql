-- =============================================================
-- 医疗系统数据库建表脚本（Java Web 实验二）
-- 目标库：medical_system    字符集：utf8mb4    引擎：InnoDB

CREATE DATABASE IF NOT EXISTS medical_system
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE medical_system;

SET NAMES utf8mb4;

DROP TABLE IF EXISTS prescription;
DROP TABLE IF EXISTS registration;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS department;

-- -------------------------------------------------------------
-- 1. 科室表 department
-- -------------------------------------------------------------
CREATE TABLE department (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '科室主键',
  dept_code   VARCHAR(32)  NOT NULL COMMENT '科室编码',
  dept_name   VARCHAR(64)  NOT NULL COMMENT '科室名称',
  location    VARCHAR(128)          DEFAULT NULL COMMENT '所在楼栋/楼层',
  phone       VARCHAR(20)           DEFAULT NULL COMMENT '科室联系电话',
  description VARCHAR(255)          DEFAULT NULL COMMENT '科室简介',
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_dept_code (dept_code),
  UNIQUE KEY uk_dept_name (dept_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='科室表';

-- -------------------------------------------------------------
-- 2. 医生表 doctor（只存 dept_id，不冗余科室名称）
-- -------------------------------------------------------------
CREATE TABLE doctor (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '医生主键',
  dept_id    BIGINT UNSIGNED NOT NULL COMMENT '所属科室外键',
  doctor_no  VARCHAR(32) NOT NULL COMMENT '医生工号',
  name       VARCHAR(32) NOT NULL COMMENT '姓名',
  id_card    VARCHAR(18) NOT NULL COMMENT '身份证号（18 位）',
  phone      VARCHAR(11) NOT NULL COMMENT '手机号（11 位）',
  gender     TINYINT     NOT NULL COMMENT '性别：1-男，2-女',
  title      VARCHAR(32)          DEFAULT NULL COMMENT '职称：主任医师/副主任医师/主治医师/住院医师',
  created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_doctor_no (doctor_no),
  UNIQUE KEY uk_doctor_id_card (id_card),
  UNIQUE KEY uk_doctor_phone (phone),
  KEY idx_doctor_name (name),
  CONSTRAINT fk_doctor_dept FOREIGN KEY (dept_id) REFERENCES department (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医生表';

-- -------------------------------------------------------------
-- 3. 患者表 patient
-- -------------------------------------------------------------
CREATE TABLE patient (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '患者主键',
  patient_no VARCHAR(32) NOT NULL COMMENT '就诊卡号',
  name       VARCHAR(32) NOT NULL COMMENT '姓名',
  id_card    VARCHAR(18) NOT NULL COMMENT '身份证号（18 位）',
  phone      VARCHAR(11) NOT NULL COMMENT '手机号（11 位）',
  gender     TINYINT     NOT NULL COMMENT '性别：1-男，2-女',
  address    VARCHAR(255)         DEFAULT NULL COMMENT '联系地址',
  created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_patient_no (patient_no),
  UNIQUE KEY uk_patient_id_card (id_card),
  UNIQUE KEY uk_patient_phone (phone),
  KEY idx_patient_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='患者表';

-- -------------------------------------------------------------
-- 4. 挂号记录表 registration
--    患者姓名、医生姓名、科室名称均不在此冗余存储；
--    科室由 doctor.dept_id 传递得到，故不设 dept_id（否则违反 3NF）。
-- -------------------------------------------------------------
CREATE TABLE registration (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '挂号记录主键',
  reg_no     VARCHAR(32)   NOT NULL COMMENT '挂号单号',
  patient_id BIGINT UNSIGNED NOT NULL COMMENT '患者外键',
  doctor_id  BIGINT UNSIGNED NOT NULL COMMENT '医生外键',
  reg_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '挂号时间',
  status     TINYINT       NOT NULL DEFAULT 0 COMMENT '状态：0-已挂号，1-已就诊，2-已取消',
  fee        DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '挂号费',
  symptom    VARCHAR(255)           DEFAULT NULL COMMENT '主诉',
  created_at DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_reg_no (reg_no),
  KEY idx_reg_time (reg_time),
  CONSTRAINT fk_reg_patient FOREIGN KEY (patient_id) REFERENCES patient (id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_reg_doctor FOREIGN KEY (doctor_id) REFERENCES doctor (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挂号记录表';

-- -------------------------------------------------------------
-- 5. 电子处方表 prescription
--    只保存处方自身单值属性；医生与患者通过 registration 关联获得，
--    避免传递依赖与冗余字段；药品明细为多值属性，留待后续实验拆表。
-- -------------------------------------------------------------
CREATE TABLE prescription (
  id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '处方主键',
  presc_no        VARCHAR(32)   NOT NULL COMMENT '处方编号',
  registration_id BIGINT UNSIGNED NOT NULL COMMENT '关联挂号记录外键',
  diagnosis       VARCHAR(255)  NOT NULL COMMENT '诊断结论',
  advice          VARCHAR(500)           DEFAULT NULL COMMENT '医嘱（单值文本）',
  status          TINYINT       NOT NULL DEFAULT 0 COMMENT '状态：0-草稿，1-已开具，2-已作废',
  created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开具时间',
  updated_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_presc_no (presc_no),
  CONSTRAINT fk_presc_registration FOREIGN KEY (registration_id) REFERENCES registration (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电子处方表';

-- =============================================================
-- 测试数据（按外键依赖顺序插入，供分页与多条件查询演示）
-- =============================================================
INSERT INTO department (dept_code, dept_name, location, phone, description) VALUES
('D001', '内科',   '门诊楼 2F', '0743-8561001', '呼吸、消化、心血管等内科常见病诊疗'),
('D002', '外科',   '门诊楼 3F', '0743-8561002', '普外、骨科等外科诊疗'),
('D003', '儿科',   '门诊楼 1F', '0743-8561003', '儿童常见病与保健'),
('D004', '眼科',   '门诊楼 4F', '0743-8561004', '眼科检查与治疗');

INSERT INTO doctor (dept_id, doctor_no, name, id_card, phone, gender, title) VALUES
(1, 'DOC001', '张明', '430101198001010001', '13800000001', 1, '主任医师'),
(1, 'DOC002', '李芳', '430101198001010002', '13800000002', 2, '副主任医师'),
(2, 'DOC003', '王强', '430101198001010003', '13800000003', 1, '主治医师'),
(2, 'DOC004', '赵敏', '430101198001010004', '13800000004', 2, '住院医师'),
(3, 'DOC005', '陈静', '430101198001010005', '13800000005', 2, '副主任医师'),
(4, 'DOC006', '刘洋', '430101198001010006', '13800000006', 1, '主治医师');

INSERT INTO patient (patient_no, name, id_card, phone, gender, address) VALUES
('P0001', '杨伟', '430101199001010001', '13900000001', 1, '吉首市乾州街道 1 号'),
('P0002', '黄丽', '430101199001010002', '13900000002', 2, '吉首市乾州街道 2 号'),
('P0003', '周涛', '430101199001010003', '13900000003', 1, '吉首市峒河街道 3 号'),
('P0004', '吴芳', '430101199001010004', '13900000004', 2, '吉首市峒河街道 4 号'),
('P0005', '徐鹏', '430101199001010005', '13900000005', 1, '吉首市镇溪街道 5 号'),
('P0006', '孙丽', '430101199001010006', '13900000006', 2, '吉首市镇溪街道 6 号'),
('P0007', '马超', '430101199001010007', '13900000007', 1, '吉首市石家冲街道 7 号'),
('P0008', '朱婷', '430101199001010008', '13900000008', 2, '吉首市石家冲街道 8 号'),
('P0009', '胡军', '430101199001010009', '13900000009', 1, '吉首市双塘街道 9 号'),
('P0010', '郭静', '430101199001010010', '13900000010', 2, '吉首市双塘街道 10 号'),
('P0011', '何强', '430101199001010011', '13900000011', 1, '吉首市吉凤街道 11 号'),
('P0012', '高敏', '430101199001010012', '13900000012', 2, '吉首市吉凤街道 12 号');

INSERT INTO registration (reg_no, patient_id, doctor_id, reg_time, status, fee, symptom) VALUES
('REG0001', 1,  1, '2026-11-10 08:30:00', 1, 20.00, '咳嗽、发热两天'),
('REG0002', 2,  2, '2026-11-10 09:10:00', 1, 20.00, '胸闷、心悸'),
('REG0003', 3,  3, '2026-11-10 10:05:00', 0, 30.00, '右膝外伤'),
('REG0004', 4,  4, '2026-11-10 10:40:00', 2, 30.00, '术前复查'),
('REG0005', 5,  5, '2026-11-11 08:15:00', 1, 15.00, '儿童发热'),
('REG0006', 6,  6, '2026-11-11 09:20:00', 0, 25.00, '视物模糊'),
('REG0007', 7,  1, '2026-11-11 14:00:00', 0, 20.00, '咽痛'),
('REG0008', 8,  3, '2026-11-11 15:30:00', 1, 30.00, '复诊换药');

INSERT INTO prescription (presc_no, registration_id, diagnosis, advice, status) VALUES
('PRE0001', 1, '急性上呼吸道感染', '多饮水，注意休息，按时复诊', 1),
('PRE0002', 2, '心律失常待查', '完善心电图检查，避免剧烈运动', 1),
('PRE0003', 5, '小儿发热', '物理降温，密切观察体温变化', 1),
('PRE0004', 8, '术后恢复期', '保持伤口干燥，定期换药', 1);


