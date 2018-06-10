package com.hxw.compiler;

import com.google.auto.service.AutoService;
import com.hxw.annotation.GlobalConfig;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * {@link GlobalConfig}注解处理
 *
 * @author hxw on 2018/5/8
 */
@SupportedAnnotationTypes("com.hxw.annotation.GlobalConfig")
@AutoService(Processor.class)
public final class GlobalConfigAnnotationProcessor extends AbstractProcessor {

    /**
     * 元素操作的辅助类
     */
    private Elements elementUtils;
    /**
     * 处理帮助类工具
     */
    private ProcessorUtil processorUtil;

    private final List<TypeElement> configModules = new ArrayList<>();

    /**
     * 初始化,如果需要在初始化时做一些操作的话
     * 可以得到ProcessingEnviroment,
     * ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        processorUtil = new ProcessorUtil(processingEnvironment);
    }

    /**
     * 可以在这里写扫描、评估和处理注解的代码，生成Java文件
     *
     * @param set              将返回所有的由该Processor处理，并待处理的 Annotations。（属于该Processor处理的注解，但并未被使用，不存在与这个集合里）
     * @param roundEnvironment 表示当前或是之前的运行环境，可以通过该对象查找找到的注解
     * @return ture:表示该组注解已经被处理, 后续不会再有其他处理器处理;
     * false:表示仍可被其他处理器处理.
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 准备在gradle的控制台打印信息
        Messager messager = processingEnv.getMessager();
        System.out.print("处理");
        for (TypeElement element : processorUtil.getElementsFor(GlobalConfig.class, roundEnvironment)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "元素->" + element + " 类型->" + element.asType());
            if (processorUtil.isConfigModule(element)) {
                configModules.add(element);
            }
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "configModules.size->" + configModules.size());




        return true;
    }

    /**
     * 指定该注解处理器用于处理哪些注解
     * 在只有一到两个注解需要处理时，可以这样编写：
     *
     * @ SupportedAnnotationTypes("com.example.annotation.SetContentView")
     * 使用SupportedAnnotationTypes注解
     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> result = new HashSet<>();
//        result.addAll(Collections.singleton(GlobalConfig.class.getName()));
//        return result;
//    }

    /**
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
