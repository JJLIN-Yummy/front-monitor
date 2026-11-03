package com.web3d.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor // 这个注解必须加，生成全参数构造
public class LimitEvent {
    private String ip;
    private String userId;
    private String path;
    private LocalDateTime time;
}
