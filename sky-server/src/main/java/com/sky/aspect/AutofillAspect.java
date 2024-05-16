package com.sky.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//自定义切面，实现公共字段的自动填充
@Aspect
@Component
@Slf4j
public class AutofillAspect {

//    切入点：对哪些类的那些方法
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
//    mapper 下的所有的类，所有的方法，（..）所有的参数类型  并且 被Autofill注解 表示过的
    public void autoFillPointCut(){

    }


//    前置通知，在通知中进行公共字段赋值
    @Before("autoFillPointCut()")
public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的自动填充");
//        获取大豆当前被拦截的操作类型


//        获取到当前被拦截方法的参数--实体对象


//        准备赋值所需要的数据
}
}
