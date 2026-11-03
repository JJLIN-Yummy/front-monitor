package com.web3d.converter;

import com.web3d.entity.dto.ReportDto;
import com.web3d.entity.po.MonitorErrorAlertPo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorErrorAlertConverter {

    MonitorErrorAlertPo toPo(ReportDto reportDto);
}
