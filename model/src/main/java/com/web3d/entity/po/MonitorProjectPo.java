package com.web3d.entity.po;

import lombok.Data;

@Data
public class MonitorProjectPo extends BasePO{
    private String projectId;
    private String projectVersion;
    private String projectName;
    private String owner;
    private String contact;
}
