package com.ipd.jmq.common.cluster;

/**
 * 集群角色
 */
public enum ClusterRole {
    /**
     * 未知
     */
    NONE,
    /**
     * 主
     */
    MASTER,
    /**
     * 从
     */
    SLAVE,
    /**
     * 备份，不参与选举
     */
    BACKUP;

    public static ClusterRole valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }

    /**
     * 根据端口来判断角色
     *
     * @param port 端口
     * @return 角色
     */
    public static ClusterRole getRoleByPort(int port) {
        if (port >= 50000) {
            // 通过端口规范获取
            boolean isEven = port % 2 == 0;
            if (isEven) {
                return ClusterRole.MASTER;
            } else if (port % 10000 <= 5535) {
                return ClusterRole.SLAVE;
            } else {
                return ClusterRole.BACKUP;
            }
        }
        return null;
    }

    /**
     * 根据别名获取角色
     *
     * @param alias 别名
     * @return 角色
     */
    public static ClusterRole getRoleByAlias(String alias) {
        if (alias == null || alias.isEmpty()) {
            return null;
        }
        // 通过别名获取
        char type = 'n';
        int pos = alias.lastIndexOf('_');
        if (pos > 0) {
            String tmp = alias.substring(pos + 1);
            if (!tmp.isEmpty()) {
                type = Character.toLowerCase(tmp.charAt(0));
            }
        }
        switch (type) {
            case 'm':
                return ClusterRole.MASTER;
            case 's':
                return ClusterRole.SLAVE;
            case 'b':
                return ClusterRole.BACKUP;
            default:
                return null;
        }
    }

    /**
     * 主从选举候选者
     *
     * @return
     */
    public boolean isCandidate() {
        switch (this) {
            case MASTER:
                return true;
            case SLAVE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 是否可读
     *
     * @return 可读标示
     */
    public boolean readable() {
        return this != NONE;
    }

    /**
     * 是否可写
     *
     * @return 可写标示
     */
    public boolean writable() {
        return this == MASTER;
    }

}