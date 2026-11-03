package com.web3d.converter;

import com.web3d.entity.dto.ReportDto;
import com.web3d.entity.po.MonitorProjectPo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorProjectConverter {
    MonitorProjectPo toPo(ReportDto reportDto);
}
