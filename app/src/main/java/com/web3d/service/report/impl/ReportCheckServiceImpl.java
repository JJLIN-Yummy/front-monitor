package com.web3d.service.report.impl;

import com.web3d.enums.EnvEnum;
import com.web3d.service.report.ReportCheckService;
import org.springframework.stereotype.Service;

@Service
public class ReportCheckServiceImpl implements ReportCheckService {
    @Override
    public boolean envCheck(String env) {
       return EnvEnum.isValid(env);
    }

}
