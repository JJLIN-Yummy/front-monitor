package com.web3d.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "错误上报的请求参数")
public class ReportDto {

    @NotBlank(message = "不能为空")
    @Size(max = 64, message = "长度不能超过64")
    @Schema(description = "项目id", example = "ai-explore-app")
    private String projectId;

    @NotBlank(message = "不能为空")
    @Size(max = 32, message = "长度不能超过32")
    @Schema(description = "环境：prod / dev", example = "prod")
    private String env;

    @NotBlank(message = "不能为空")
    @Size(max = 32, message = "长度不能超过32")
    @Schema(description = "错误类型，如js_error", example = "js_error")
    private String errorType;

    @NotBlank(message = "不能为空")
    @Schema(description = "错误信息", example = "Cannot read properties of undefined (reading 'xyz')")
    private String errorMsg;

    @NotBlank(message = "不能为空")
    @Size(max = 2000, message = "长度不能超过2000")
    @Schema(description = "错误堆栈", example = "Error: Cannot read properties of undefined\n    at render (app.xxxx.js:1:234)")
    private String stack;

    @NotBlank(message = "不能为空")
    @Size(max = 1024, message = "长度不能超过1024")
    @Pattern(regexp = "^https?://[^\\s<>]*$", message = "格式不合法")
    @Schema(description = "页面完整url", example = "https://xxx.com/home")
    private String pageUrl;

    @NotBlank(message = "不能为空")
    @Size(max = 255, message = "长度不能超过255")
    @Schema(description = "路由path", example = "/home")
    private String routePath;

    @Schema(description = "用户标识")
    @Size(max = 128, message = "长度不能超过128")
    private String userIdentify;

    @NotBlank(message = "不能为空")
    @Size(max = 128, message = "长度不能超过128")
    @Schema(description = "浏览器信息", example = "Chrome 128")
    private String browser;

    @NotBlank(message = "不能为空")
    @Size(max = 128, message = "长度不能超过128")
    @Schema(description = "操作系统", example = "Windows 10")
    private String os;

    @NotBlank(message = "不能为空")
    @Size(max = 128, message = "长度不能超过128")
    @Schema(description = "设备类型 pc/mobile", example = "pc")
    private String device;

    @NotBlank(message = "不能为空")
    @Size(max = 45, message = "长度不能超过45")
    @Schema(description = "项目版本", example = "1.0.2")
    private String projectVersion;

    @NotBlank(message = "不能为空")
    @Size(max = 64, message = "长度不能超过64")
    @Schema(description = "客户端指纹", example = "abcd123456")
    private String fingerprint;

    @NotBlank(message = "不能为空")
    @Size(max = 45, message = "长度不能超过45")
    @Schema(description = "来源", example = "WEB")
    private String sdkType;

    @NotBlank(message = "不能为空")
    @Size(max = 45, message = "长度不能超过45")
    @Schema(description = "指纹类型", example = "web_canvas")
    private String fingerprintType;

    @NotBlank(message = "不能为空")
    @Schema(description = "错误级别", example = "error")
    private String level;

}