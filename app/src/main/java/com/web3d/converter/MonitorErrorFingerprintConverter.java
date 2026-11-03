package com.web3d.converter;

import com.web3d.entity.dto.ReportDto;
import com.web3d.entity.po.MonitorErrorFingerprintPo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorErrorFingerprintConverter {
    MonitorErrorFingerprintPo toPo(ReportDto reportDto);
}
