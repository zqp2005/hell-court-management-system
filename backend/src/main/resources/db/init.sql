CREATE DATABASE IF NOT EXISTS hell_court DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hell_court;

-- ==================== RBAC 角色表 ====================
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(30) NOT NULL UNIQUE COMMENT '角色编码',
    `name` VARCHAR(30) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(100) COMMENT '角色描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

INSERT INTO `role` (`id`, `code`, `name`, `description`) VALUES
(1, 'WUCHANG', '无常', '牛头马面/黑白无常，勾魂索命'),
(2, 'JUDGE',   '判官', '崔判官，审判善恶定赏罚'),
(3, 'KING',    '阎王', '十殿阎王，复核判决掌轮回'),
(4, 'ADMIN',   '管理员', '阎罗王，统管系统不插手业务');

-- ==================== 用户表 ====================
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `real_name` VARCHAR(50) NOT NULL,
    `role_id` BIGINT NOT NULL COMMENT 'FK -> role.id',
    `status` TINYINT DEFAULT 1 COMMENT '0-禁用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ==================== 魂魄表 ====================
CREATE TABLE IF NOT EXISTS `soul` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `gender` TINYINT DEFAULT 0 COMMENT '0-男 1-女',
    `birth_date` DATETIME NOT NULL,
    `death_date` DATETIME,
    `lifespan` INT DEFAULT 0,
    `good_deeds` INT DEFAULT 0,
    `evil_deeds` INT DEFAULT 0,
    `soul_status` TINYINT DEFAULT 0 COMMENT '0-待勾魂 1-已到殿 2-审判中 3-已判决 4-已轮回',
    `arrival_date` DATETIME,
    `description` TEXT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_name` (`name`),
    INDEX `idx_status` (`soul_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='魂魄表';

-- ==================== 判官审判表 ====================
CREATE TABLE IF NOT EXISTS `judgment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `soul_id` BIGINT NOT NULL,
    `judge_id` BIGINT NOT NULL,
    `judgment_detail` TEXT,
    `good_reward` VARCHAR(255),
    `evil_punishment` VARCHAR(255),
    `status` TINYINT DEFAULT 0 COMMENT '0-待复核 1-已复核 2-已驳回',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_soul` (`soul_id`),
    INDEX `idx_judge` (`judge_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='判官审判表';

-- ==================== 阎王复核表 ====================
CREATE TABLE IF NOT EXISTS `review` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `judgment_id` BIGINT NOT NULL,
    `king_id` BIGINT NOT NULL,
    `review_opinion` TEXT,
    `result` TINYINT NOT NULL COMMENT '1-通过 2-驳回',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_judgment` (`judgment_id`),
    INDEX `idx_king` (`king_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阎王复核表';

-- ==================== 轮回记录表 ====================
CREATE TABLE IF NOT EXISTS `rebirth` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `soul_id` BIGINT NOT NULL,
    `path` TINYINT NOT NULL COMMENT '1-天道 2-阿修罗道 3-人道 4-畜生道 5-饿鬼道 6-地狱道',
    `rebirth_date` DATETIME NOT NULL,
    `destination` VARCHAR(100),
    `operator_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_soul` (`soul_id`),
    INDEX `idx_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮回记录表';

-- ==================== 操作日志表 ====================
CREATE TABLE IF NOT EXISTS `operation_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT,
    `action` VARCHAR(100) NOT NULL,
    `target_type` VARCHAR(50),
    `target_id` BIGINT,
    `detail` TEXT,
    `ip_address` VARCHAR(50),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user` (`user_id`),
    INDEX `idx_action` (`action`),
    INDEX `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ==================== RBAC 权限表 ====================
CREATE TABLE IF NOT EXISTS `permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '权限编码',
    `name` VARCHAR(50) NOT NULL COMMENT '权限名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ==================== RBAC 角色-权限关联表 ====================
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `role_id` BIGINT NOT NULL COMMENT 'FK -> role.id',
    `permission_code` VARCHAR(50) NOT NULL,
    UNIQUE KEY `uk_rp` (`role_id`, `permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- ==================== 权限种子数据 ====================
INSERT INTO `permission` (`code`, `name`) VALUES
('soul:read',    '查看魂魄'),
('soul:write',   '新增/修改魂魄'),
('soul:delete',  '删除魂魄'),
('soul:summon',  '勾魂'),
('judgment:read',  '查看审判'),
('judgment:write', '录入审判'),
('judgment:submit','提交复核'),
('review:read',    '查看复核'),
('review:approve', '审批复核'),
('rebirth:read',   '查看轮回'),
('rebirth:assign', '分配轮回'),
('user:manage',    '用户管理'),
('log:view',       '查看日志');

-- ==================== 角色-权限映射 ====================

-- 无常(1): 创建魂魄 + 勾魂 + 查看
INSERT INTO `role_permission` (`role_id`, `permission_code`) VALUES
(1, 'soul:read'), (1, 'soul:write'), (1, 'soul:summon');

-- 判官(2): 查看魂魄 + 审判全部
INSERT INTO `role_permission` (`role_id`, `permission_code`) VALUES
(2, 'soul:read'),
(2, 'judgment:read'), (2, 'judgment:write'), (2, 'judgment:submit');

-- 阎王(3): 查看魂魄 + 查看审判 + 复核 + 轮回（不建魂魄、不录审判）
INSERT INTO `role_permission` (`role_id`, `permission_code`) VALUES
(3, 'soul:read'), (3, 'judgment:read'),
(3, 'review:read'), (3, 'review:approve'),
(3, 'rebirth:read'), (3, 'rebirth:assign');

-- 管理员(4): 只读所有业务 + 系统管理（不参与业务操作）
INSERT INTO `role_permission` (`role_id`, `permission_code`) VALUES
(4, 'soul:read'), (4, 'judgment:read'), (4, 'review:read'), (4, 'rebirth:read'),
(4, 'user:manage'), (4, 'log:view');

-- ==================== 默认用户 ====================
INSERT INTO `user` (`username`, `password`, `real_name`, `role_id`) VALUES
('admin',    '$2a$10$5/4zzF3YlYPdbkoBNfUO/O8AnElO0l8ozsF2fKzKK8.7wbUGOrBfK', '阎罗王', 4),
('judge1',   '$2a$10$5/4zzF3YlYPdbkoBNfUO/O8AnElO0l8ozsF2fKzKK8.7wbUGOrBfK', '钟馗',   2),
('king1',    '$2a$10$5/4zzF3YlYPdbkoBNfUO/O8AnElO0l8ozsF2fKzKK8.7wbUGOrBfK', '秦广王', 3),
('wuchang1', '$2a$10$5/4zzF3YlYPdbkoBNfUO/O8AnElO0l8ozsF2fKzKK8.7wbUGOrBfK', '黑无常', 1);
