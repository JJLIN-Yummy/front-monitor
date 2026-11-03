package com.web3d.service.report.impl;

import com.web3d.converter.MonitorErrorAlertConverter;
import com.web3d.converter.MonitorErrorFingerprintConverter;
import com.web3d.converter.MonitorErrorLogConverter;
import com.web3d.entity.dto.ReportDto;
import com.web3d.entity.po.MonitorErrorFingerprintPo;
import com.web3d.entity.po.MonitorErrorLogPo;
import com.web3d.entity.po.MonitorProjectPo;
import com.web3d.entity.po.MonitorSourcemapPo;
import com.web3d.exception.BusinessException;
import com.web3d.mapper.ReportMapper;
import com.web3d.model.ResultCodeEnum;
import com.web3d.service.report.ReportCheckService;
import com.web3d.service.report.ReportService;
import com.web3d.utils.RedisUtil;
import com.web3d.utils.SnowflakeIdUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MonitorErrorFingerprintConverter monitorErrorFingerprintConverter;

    @Autowired
    private MonitorErrorLogConverter monitorErrorLogConverter;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ReportCheckService reportCheckService;


    @Override
    @Transactional
    public void report(ReportDto reportDto) {
        //todo: 注意单向数据流
        reportDto.setFingerprint(this.genErrorSign(reportDto.getErrorType(),reportDto.getErrorMsg()));
        //1.1检查是否存在projectId
        MonitorProjectPo monitorProjectPo = new MonitorProjectPo();
        monitorProjectPo.setProjectId(reportDto.getProjectId());
        if(!this.exitMonitorProjectByProjectId(monitorProjectPo)){
            throw new BusinessException(ResultCodeEnum.NOT_EXIST,"projectId");
        }
        //1.2 验证传来的env是否合法
        if(!reportCheckService.envCheck(reportDto.getEnv())){
            throw new BusinessException(ResultCodeEnum.NOT_EXIST,"env");
        }

//        log.info("===========reportDto{}", reportDto.getFingerprintType());
        long snowKey = SnowflakeIdUtil.generateId();
        //2.插入日志表 MonitorErrorLog
        MonitorErrorLogPo monitorErrorLogPo = monitorErrorLogConverter.toPo(reportDto);
        monitorErrorLogPo.setId(snowKey);
        reportMapper.insertMonitorErrorLog(monitorErrorLogPo);

        //3.插入错误指纹表
        //3.1 db查询有没有这个错误指纹
        MonitorErrorFingerprintPo monitorErrorFingerprintPo = monitorErrorFingerprintConverter.toPo(reportDto);
//        log.info("=========monitorErrorFingerprintPo{}", monitorErrorFingerprintPo.getFingerprintType());
        MonitorErrorFingerprintPo monitorErrorFingerprintPoFromDb = reportMapper.searchMonitorErrorFingerprint(monitorErrorFingerprintPo);
        //3.2 如果查询出来的错误指纹不存在
        if(monitorErrorFingerprintPoFromDb == null){
            //3.3 db查找sourcemap信息
            MonitorSourcemapPo monitorSourcemapPo = new MonitorSourcemapPo();
            monitorSourcemapPo.setProjectId(reportDto.getProjectId());
            monitorSourcemapPo.setProjectVersion(reportDto.getProjectVersion());
            monitorSourcemapPo.setEnv(reportDto.getEnv());
            MonitorSourcemapPo monitorSourcemapPoFromDb = reportMapper.searchMonitorSourcemap(monitorSourcemapPo);

            monitorErrorFingerprintPo.setId(snowKey);
            monitorErrorFingerprintPo.setIsResolved(0);
            monitorErrorFingerprintPo.setIsValid(1);
            monitorErrorFingerprintPo.setCount(1);
            monitorErrorFingerprintPo.setFirstOccurTime(LocalDateTime.now());
            monitorErrorFingerprintPo.setLastOccurTime(LocalDateTime.now());
            monitorErrorFingerprintPo.setLevel(reportDto.getLevel());

            //3.4判断是否存在sourceMap文件
            if(monitorSourcemapPoFromDb != null){
                monitorErrorFingerprintPo.setSourcemapFile(monitorSourcemapPoFromDb.getFileName());
            }

            reportMapper.insertMonitorErrorFingerprint(monitorErrorFingerprintPo);
            return;
        }
        //3.5 如果查询出来的错误指纹存在 count++
        monitorErrorFingerprintPo.setId(monitorErrorFingerprintPoFromDb.getId());
        monitorErrorFingerprintPo.setCount(monitorErrorFingerprintPoFromDb.getCount()+1);
        monitorErrorFingerprintPo.setLastOccurTime(LocalDateTime.now());
        reportMapper.updateMonitorErrorFingerprint(monitorErrorFingerprintPo);
    }

    @Override
    @Transactional
    public MonitorProjectPo queryMonitorProjectPo(MonitorProjectPo monitorProjectPo) {
        if(monitorProjectPo == null){
            //todo: 相同字符串过多
            throw new BusinessException(ResultCodeEnum.NOT_EXIST,"projectId");
        }
        String key = "monitor:project:" + monitorProjectPo.getProjectId();
        return RedisUtil.getWithCache(
                redisTemplate,
                key,
                () -> reportMapper.searchMonitorProject(monitorProjectPo),
                30, TimeUnit.MINUTES
        );
    }

    @Override
    public boolean exitMonitorProjectByProjectId(MonitorProjectPo monitorProjectPo) {
        if(monitorProjectPo == null){
            throw new BusinessException(ResultCodeEnum.SERVER_ERROR);
        }
        MonitorProjectPo monitorProjectPoFromDb = this.queryMonitorProjectPo(monitorProjectPo);
        if(monitorProjectPoFromDb == null){
            throw new BusinessException(ResultCodeEnum.NOT_EXIST,"projectId");
        }
        return monitorProjectPo.getProjectId().equals(monitorProjectPoFromDb.getProjectId());
    }


    /**
     * 根据 errorType + errorMsg 生成错误唯一标识 sign，用于错误聚合、去重
     */
    public String genErrorSign(String errorType, String errorMsg) {
        if (errorType == null) errorType = "";
        if (errorMsg == null) errorMsg = "";
        // 分隔符 | 防止拼接歧义：a|bc 和 ab|c 区分开
        String raw = errorType + "|" + errorMsg;
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }


}
