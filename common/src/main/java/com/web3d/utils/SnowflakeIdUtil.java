package com.web3d.utils;

/**
 * 雪花算法 ID 生成器 (Snowflake)
 * 生成 18 位的全局唯一 Long 型数字
 */
public class SnowflakeIdUtil {

    // 起始的时间戳 (2020-01-01 00:00:00)，用于计算相对时间
    private final static long START_STAMP = 1577808000000L;

    // 每一部分占用的位数
    private final static long SEQUENCE_BIT = 12;   // 序列号占用的位数 (支持同一毫秒内生成 4096 个 ID)
    private final static long MACHINE_BIT = 5;     // 机器标识占用的位数 (支持 32 台机器)
    private final static long DATACENTER_BIT = 5;  // 数据中心占用的位数 (支持 32 个数据中心)

    // 每一部分的最大值
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT); // 31
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);       // 31
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);         // 4095

    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;                   // 12
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;  // 17
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT; // 22

    private final long datacenterId;  // 数据中心 ID (0~31)
    private final long machineId;     // 机器 ID (0~31)
    private long sequence = 0L;       // 毫秒内序列 (0~4095)
    private long lastStamp = -1L;     // 上次生成 ID 的时间戳

    /**
     * 构造函数
     * @param datacenterId 数据中心 ID (0~31)
     * @param machineId    机器 ID (0~31)
     */
    public SnowflakeIdUtil(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId 不能大于 " + MAX_DATACENTER_NUM + " 或者小于 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId 不能大于 " + MAX_MACHINE_NUM + " 或者小于 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 核心方法：生成下一个 ID (使用 synchronized 保证线程安全)
     * @return 全局唯一的 18 位 ID
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();

        if (currStamp < lastStamp) {
            throw new RuntimeException("时钟回拨异常，拒绝生成 ID");
        }

        if (currStamp == lastStamp) {
            // 同一毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大 (4095)
            if (sequence == 0L) {
                // 阻塞到下一个毫秒，获得新的时间戳
                currStamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为 0
            sequence = 0L;
        }

        lastStamp = currStamp;

        // 移位并通过或运算拼到一起组成 64 位的 ID
        return (currStamp - START_STAMP) << TIMESTAMP_LEFT // 时间戳部分
                | datacenterId << DATACENTER_LEFT       // 数据中心部分
                | machineId << MACHINE_LEFT             // 机器标识部分
                | sequence;                             // 序列号部分
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     */
    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    /**
     * 获取当前时间戳
     */
    private long getNewStamp() {
        return System.currentTimeMillis();
    }

    // ================= 提供全局单例调用 =================

    // 默认单例（如果你的项目部署在多台服务器，请给每台机器分配不同的 machineId）
    private static final SnowflakeIdUtil INSTANCE = new SnowflakeIdUtil(1, 1);

    /**
     * 静态调用方法，直接获取 ID
     */
    public static long generateId() {
        return INSTANCE.nextId();
    }
}