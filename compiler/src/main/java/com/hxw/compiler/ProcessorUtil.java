package com.hxw.compiler;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * 注解处理的工具类
 *
 * @author hxw on 2018/6/10
 */
final class ProcessorUtil {
    private final ProcessingEnvironment processingEnv;
    private final TypeElement configModuleType;

    ProcessorUtil(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.configModuleType = processingEnv
                .getElementUtils().getTypeElement("com.hxw.core.integration.ConfigModule");
    }

    /**
     * 获得所有被指定的注解声明的元素
     *
     * @param clazz 指定的注解
     * @param env   表示当前或是之前的运行环境，可以通过该对象查找找到的注解
     * @return 所需要的注解集合
     */
    List<TypeElement> getElementsFor(Class<? extends Annotation> clazz, RoundEnvironment env) {
        Collection<? extends Element> annotatedElements = env.getElementsAnnotatedWith(clazz);
        return ElementFilter.typesIn(annotatedElements);
    }


    /**
     * 判断指定的元素是否是{com.hxw.core.integration.ConfigModule}的子类
     *
     * @param element 指定的元素
     * @return true 是 false 否
     */
    boolean isConfigModule(TypeElement element) {
        return processingEnv.getTypeUtils().isAssignable(element.asType(),
                configModuleType.asType());
    }

}
