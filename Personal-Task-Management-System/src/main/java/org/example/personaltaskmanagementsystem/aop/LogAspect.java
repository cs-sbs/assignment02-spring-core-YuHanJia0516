package org.example.personaltaskmanagementsystem.aop;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.personaltaskmanagementsystem.mapper.OperateLogMapper;
import org.example.personaltaskmanagementsystem.pojo.OperateLog;
import org.example.personaltaskmanagementsystem.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;
    @Around("@annotation(org.example.personaltaskmanagementsystem.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        //获取操作时间
        LocalDateTime now = LocalDateTime.now();
        //获取类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        //开始时间
        long beginTime = System.currentTimeMillis();
        //获取返回值
        Object result = joinPoint.proceed();
        //结束时间
        long endTime = System.currentTimeMillis();
        //方法返回值
        String returnValue = JSONObject.toJSONString(result);
        //获取耗时
        long costTime = endTime - beginTime;

        //记录日志
        OperateLog operateLog = new OperateLog(null, userId, now, className, methodName, methodParams, returnValue, costTime);
        operateLogMapper.insert(operateLog);
        return result;
    }
}
