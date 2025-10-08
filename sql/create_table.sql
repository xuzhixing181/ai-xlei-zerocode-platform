# 数据库初始化
-- 创建库
CREATE DATABASE IF NOT EXISTS ai_xlei_zerocode;-- 切换库
USE ai_xlei_zerocode;-- 用户表
-- 以下是建表语句
-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT auto_increment COMMENT 'id' PRIMARY KEY,
    userAccount VARCHAR ( 256 ) NOT NULL COMMENT '账号',
    userPassword VARCHAR ( 512 ) NOT NULL COMMENT '密码',
    userName VARCHAR ( 256 ) NULL COMMENT '用户昵称',
    userAvatar VARCHAR ( 1024 ) NULL COMMENT '用户头像',
    userProfile VARCHAR ( 512 ) NULL COMMENT '用户简介',
    userRole VARCHAR ( 256 ) DEFAULT 'user' NOT NULL COMMENT '用户角色：user/admin',
    vipExpireTime datetime NULL COMMENT '会员过期时间',
    vipCode VARCHAR ( 128 ) NULL COMMENT '会员兑换码',
    vipNumber BIGINT NULL COMMENT '会员编号' editTime datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '编辑时间',
    createTime datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除',
    UNIQUE KEY uk_userAccount ( userAccount ),
    INDEX idx_userName ( userName )
    ) COMMENT '用户' COLLATE = utf8mb4_unicode_ci;


-- 应用表
CREATE TABLE IF NOT EXISTS t_app (
    id           bigint auto_increment comment 'id' primary key,
    app_name      varchar(256)                       null comment '应用名称',
    cover        varchar(512)                       null comment '应用封面',
    init_prompt   text                               null comment '应用初始化的 prompt',
    code_gen_type  varchar(64)                        null comment '代码生成类型（枚举）',
    deploy_key    varchar(64)                        null comment '部署标识',
    deployed_time datetime                           null comment '部署时间',
    priority     int      default 0                 not null comment '优先级',
    userId       bigint                             not null comment '创建用户id',
    edit_time     datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    UNIQUE KEY uk_deployKey (deploy_key), -- 确保部署标识唯一
    INDEX idx_appName (app_name),         -- 提升基于应用名称的查询性能
    INDEX idx_userId (userId)            -- 提升基于用户 ID 的查询性能
    ) comment '应用' collate = utf8mb4_unicode_ci;

-- 对话历史表
create table t_chat_history
(
    id          bigint auto_increment comment 'id' primary key,
    message     text                               not null comment '消息',
    message_type varchar(32)                        not null comment 'user/ai',
    appId       bigint                             not null comment '应用id',
    userId      bigint                             not null comment '创建用户id',
    parentId    bigint  null comment '父消息id（用于上下文关联）',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    INDEX idx_appId (appId),                       -- 提升基于应用的查询性能
    INDEX idx_createTime (create_time),             -- 提升基于时间的查询性能
    INDEX idx_appId_createTime (appId, create_time) -- 游标查询核心索引
) comment '对话历史' collate = utf8mb4_unicode_ci;