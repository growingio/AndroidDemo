package com.example.growingio.lib_codebag;

import android.content.Context;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * author WangYing
 * time   2018/03/2018/3/28:下午10:00
 * email  wangying@growingio.com
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ProcessorTestDetail extends AbstractProcessor {

    Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        processingEnv.getOptions();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BugDetail.class.getCanonicalName());
        types.add(TaskBean.class.getCanonicalName());
        return types;

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        ClassName bugBean = null;
        ClassName task = null;
        for (Element element : roundEnvironment.getElementsAnnotatedWith(TaskBean.class)) {
            ElementKind elementKind = element.getKind();
            if (elementKind == ElementKind.CLASS) {
                bugBean = ClassName.get((TypeElement) element);
            } else if (elementKind == ElementKind.METHOD) {
                task = ClassName.get((TypeElement) element.getEnclosingElement());
            }
        }
        if (bugBean == null)
            return false;

        //创建类，公布方法 getBugBeanList() , return List<ButBean> 让外层可以调用
        Filer filer = processingEnv.getFiler();
        ClassName bugList = ClassName.get(List.class);
        ClassName bugArrarList = ClassName.get(ArrayList.class);
        //List<BugBean>
        TypeName bugListOfBugBean = ParameterizedTypeName.get(bugList, bugBean);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getBugList")
                .addModifiers(PUBLIC, STATIC)
                .addStatement("$T list = new $T<>()", bugListOfBugBean, bugArrarList);
        for (Element element1 : roundEnvironment.getElementsAnnotatedWith(BugDetail.class)) {
            ElementKind elementKind = element1.getKind();
            String bugName = element1.getAnnotation(BugDetail.class).bugName();
            String fixVersion = element1.getAnnotation(BugDetail.class).fixVersion();
            String taskNumber = element1.getAnnotation(BugDetail.class).taskNumber();
            String tag = element1.getAnnotation(BugDetail.class).tag();

            if (elementKind == ElementKind.CLASS) {
                String classPackage = ((PackageElement) element1.getEnclosingElement()).getQualifiedName().toString();
                String className = element1.getSimpleName().toString();
                builder.addStatement("list.add(new $T($S,$S,$S,$S,$L,$L,$L))", bugBean,
                        taskNumber, fixVersion, bugName, tag, false, classPackage + "." + className + ".class", null);
            } else if (elementKind == ElementKind.METHOD) {
                TypeElement classElement = (TypeElement) element1.getEnclosingElement();
                ClassName targetClass = ClassName.get(classElement);
                builder.addStatement("list.add(new $T($S,$S,$S,$S,$L,$S,$L))", bugBean,
                        taskNumber, fixVersion, bugName, tag, true,
                        null, TypeSpec.anonymousClassBuilder(CodeBlock.builder().build())
                                .superclass(task)
                                .addMethod(MethodSpec.methodBuilder("execute")
                                        .addModifiers(PUBLIC)
                                        .addAnnotation(Override.class)
                                        .addStatement("$T targetClass = new $T()",
                                                targetClass, targetClass)
                                        .addStatement("targetClass.$L()", element1.getSimpleName().toString())
                                        .returns(TypeName.VOID).build())
                                .build());
            }
        }


        builder.addStatement("return list")
                .returns(bugListOfBugBean);

        MethodSpec getBugList = builder.build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(PUBLIC, FINAL)
                .addMethod(getBugList)
                .build();
        final JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException ignore) {

        }

        return true;
    }

}
