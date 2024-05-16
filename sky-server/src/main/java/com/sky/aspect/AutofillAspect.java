package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
public void autoFill(JoinPoint joinPoint) throws Exception {
        log.info("开始进行公共字段的自动填充");
//        获取当前被拦截的操作类型
        MethodSignature  signature = (MethodSignature) joinPoint.getSignature();//获取方法签名的对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value();//获取类型
//        获取到当前被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
if (args == null|| args.length==0){
    return;
}
Object entity = args[0];
//        准备赋值所需要的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
//        根据当前的操作累心，为对应的属性赋值
        if (operationType == OperationType.INSERT){
//            为四个公共字段赋值
            Method setCreatTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
            Method setCreatUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
//通过反射来赋值
            setCreatTime.invoke(entity,now);
            setCreatUser.invoke(entity,currentId);
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }else if (operationType ==  OperationType.UPDATE){

            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
//通过反射来赋值

            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }
}
}
