package com.web3d.entity.po;

import lombok.Data;

@Data
public class MonitorErrorLogPo extends BasePO{
    private String projectId;
    private String projectVersion;
    private String env;
    private String fingerprint;
    private String errorType;
    private String errorMsg;
    private String stack;
    private String pageUrl;
    private String routePath;
    private String userIdentify;
    private String browser;
    private String os;
    private String device;
}
