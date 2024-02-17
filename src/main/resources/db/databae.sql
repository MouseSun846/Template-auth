SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;
SET character_set_client = utf8;
SET character_set_results = utf8;
SET character_set_connection = utf8;
CREATE SCHEMA IF NOT EXISTS document_database;
use document_database;

CREATE TABLE IF NOT EXISTS `db_user`
(
    `account`      VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL PRIMARY KEY COMMENT '账户',
    `password`     VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
    `avatar_url`   VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '头像',
    `token`        VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '身份信息',
    `role`         VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色',
    `ip` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录ip地址',
    `created_time` BIGINT(20) COMMENT '创建时间',
    `updated_time` BIGINT(20) COMMENT '更新时间'
    );
