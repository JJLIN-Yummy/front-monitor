package com.web3d.entity.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitorSourcemapPo extends BasePO{
    private String projectId;
    private String env;
    private String projectVersion;
    private String fileName;
    private String ossPath;
    private LocalDateTime uploadTime;
}
