package com.web3d.mapper;

import com.web3d.entity.bo.ReportBo;
import com.web3d.entity.po.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {

    MonitorErrorFingerprintPo searchMonitorErrorFingerprint(MonitorErrorFingerprintPo monitorErrorFingerprintPo);
    MonitorSourcemapPo searchMonitorSourcemap(MonitorSourcemapPo monitorSourcemapPo);
    MonitorProjectPo searchMonitorProject(MonitorProjectPo monitorProjectPo);

    void insertMonitorErrorLog(MonitorErrorLogPo monitorErrorLogPo);

    void insertMonitorErrorAlert(MonitorErrorAlertPo monitorErrorAlertPo);

    void insertMonitorErrorFingerprint(MonitorErrorFingerprintPo monitorErrorFingerprintPo);

    void insertMonitorProject(MonitorProjectPo monitorProjectPo);

    void insertMonitorSourcemap(MonitorSourcemapPo monitorSourcemapPo);

    void updateMonitorErrorFingerprint(MonitorErrorFingerprintPo monitorErrorFingerprintPo);


}
