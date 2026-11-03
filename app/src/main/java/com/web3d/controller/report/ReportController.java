package com.web3d.controller.report;

import com.web3d.annotation.JwtToken;
import com.web3d.entity.dto.ReportDto;
import com.web3d.model.Result;
import com.web3d.model.ResultCodeEnum;
import com.web3d.service.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Tag(name = "错误上报接口")
@RestController
@RequestMapping(value = "/api/monitor")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "错误上报")
    @JwtToken
    @PostMapping("/report")
    public Result<Void> report(@Valid @RequestBody ReportDto reportDto){

        reportService.report(reportDto);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
