package com.hxw.compiler;

import com.google.auto.service.AutoService;
import com.hxw.annotation.GlobalConfig;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * @author hxw on 2018/5/8
 */
@AutoService(Processor.class)
public class ConfigAnnotationProcessor extends AbstractProcessor {

    /**
     * 初始化,如果需要在初始化时做一些操作的话
     * 可以得到ProcessingEnviroment,
     * ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    /**
     * 可以在这里写扫描、评估和处理注解的代码，生成Java文件
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }

    /**
     * 指定该注解处理器用于处理哪些注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new HashSet<>();
        result.add(GlobalConfig.class.getName());
        return result;
    }

    /**
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
