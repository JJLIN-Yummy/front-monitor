package com.web3d.entity.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitorErrorFingerprintPo extends BasePO {
    private String projectId;
    private String projectVersion;
    private String env;
    private String fingerprint;
    private String fingerprintType;
    private String errorType;
    private String errorMsg;
    private LocalDateTime firstOccurTime;
    private LocalDateTime lastOccurTime;
    private Integer count;
    private String sourcemapFile;
    private Integer isResolved;
    private LocalDateTime resolveTime;
    private String level;
    private String remark;
    private String sdkType;
}
