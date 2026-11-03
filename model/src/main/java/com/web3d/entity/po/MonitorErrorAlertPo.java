package com.web3d.entity.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitorErrorAlertPo extends BasePO{
    private String projectId;
    private String projectVersion;
    private String env;
    private String fingerprint;
    private String alertContent;
    private LocalDateTime alertTime;
    private Integer alertStatus;
}
