package net.cnki.common.fw;

/**
 * 日志类型
 * @author: lizhizhong
 * CreatedDate: 2018/12/04.
 */

public enum LogType {

    /**
     * 默认0操作
     */
    OPERATION,

    /**
     * 1登录
     */
    LOGIN,

    /**
     * 2 删除
     */
    DELETE,

    /**
     * 3 更改
     */
    UPDATE,

    /**
     * 审核
     */
    AUDITING,

    /**
     * 检测
     */
    DETECTION,

    /**
     * 课题相似性
     */
    TOPIC_SIMILARITY
}
