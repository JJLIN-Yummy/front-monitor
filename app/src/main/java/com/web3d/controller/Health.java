package com.web3d.controller;

import com.web3d.model.Result;
import com.web3d.model.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Tag(name = "登录接口")
@RestController
@RequestMapping(value = "/api")
public class Health {

    @Operation(summary = "心跳检查")
    @GetMapping(value = "/health", produces = "application/json;charset=UTF-8")
    public Result<String> health(){
        log.info("健康检查...");
        return Result.build("ok", ResultCodeEnum.SUCCESS);
    }
}
