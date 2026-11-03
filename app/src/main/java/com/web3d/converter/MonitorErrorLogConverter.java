package com.web3d.converter;

import com.web3d.entity.dto.ReportDto;
import com.web3d.entity.po.MonitorErrorLogPo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorErrorLogConverter {
    MonitorErrorLogPo toPo(ReportDto reportDto);
}
