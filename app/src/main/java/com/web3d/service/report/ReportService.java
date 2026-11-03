package com.web3d.service.report;

import com.web3d.entity.dto.ReportDto;
import com.web3d.entity.po.MonitorProjectPo;

public interface ReportService {

    void report(ReportDto reportDto);

    MonitorProjectPo queryMonitorProjectPo(MonitorProjectPo monitorProjectPo);

    boolean exitMonitorProjectByProjectId(MonitorProjectPo monitorProjectPo);



}
