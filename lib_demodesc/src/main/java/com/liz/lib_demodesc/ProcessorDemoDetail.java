package com.liz.lib_demodesc;

import com.squareup.javapoet.ClassName;
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
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * classDesc: annotation processor
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ProcessorDemoDetail extends AbstractProcessor {
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(DemoBeanTag.class.getCanonicalName());
        types.add(DemoDetail.class.getCanonicalName());
        return types;
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        ClassName demoBean = null;
        ClassName task = null;
        for (Element element : roundEnvironment.getElementsAnnotatedWith(DemoBeanTag.class)) {
            ElementKind elementKind = element.getKind();
            if (elementKind == ElementKind.CLASS) {
                demoBean = ClassName.get((TypeElement) element);
            } else if (elementKind == ElementKind.METHOD) {
                task = ClassName.get((TypeElement) element.getEnclosingElement());
            }
        }
        if (demoBean == null)
            return false;
        
        
        Filer filer = processingEnv.getFiler();
        
        ClassName bugList = ClassName.get(List.class);
        ClassName bugArrarList = ClassName.get(ArrayList.class);
        //List<BugBean>
        TypeName bugListOfBugBean = ParameterizedTypeName.get(bugList, demoBean);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getBugList")
                .addModifiers(PUBLIC, STATIC)
                .addStatement("$T list = new $T<>()", bugListOfBugBean, bugArrarList);
        for (Element element1 : roundEnvironment.getElementsAnnotatedWith(DemoDetail.class)) {
            ElementKind elementKind = element1.getKind();
            String demoDesc = element1.getAnnotation(DemoDetail.class).demoDesc();
            String demoGroup = element1.getAnnotation(DemoDetail.class).demoGroup();
            
            if (elementKind == ElementKind.CLASS) {
                String classPackage = ((PackageElement) element1.getEnclosingElement()).getQualifiedName().toString();
                String className = element1.getSimpleName().toString();
                builder.addStatement("list.add(new $T($S,$S,$S,$L,$L))", demoBean,
                        demoDesc, demoGroup, false, classPackage + "." + className + ".class", null);
            }
//            else if (elementKind == ElementKind.METHOD) {
//                TypeElement classElement = (TypeElement) element1.getEnclosingElement();
//                ClassName targetClass = ClassName.get(classElement);
//                builder.addStatement("list.add(new $T($S,$S,$S,$S,$L,$S,$L))", bugBean,
//                        taskNumber, fixVersion, bugName, tag, true,
//                        null, TypeSpec.anonymousClassBuilder(CodeBlock.builder().build())
//                                .superclass(task)
//                                .addMethod(MethodSpec.methodBuilder("execute")
//                                        .addModifiers(PUBLIC)
//                                        .addAnnotation(Override.class)
//                                        .addStatement("$T targetClass = new $T()",
//                                                targetClass, targetClass)
//                                        .addStatement("targetClass.$L()", element1.getSimpleName().toString())
//                                        .returns(TypeName.VOID).build())
//                                .build());
//            }
        }
        
        
        builder.addStatement("return list")
                .returns(bugListOfBugBean);
        
        
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getDemoList")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        
        
        MethodSpec methodSpec = methodBuilder.build();
        
        TypeSpec demoNote = TypeSpec.classBuilder("DemoNote")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodSpec)
                .build();
        
        final JavaFile javaFile = JavaFile.builder("com.growingio.DemoNote", demoNote)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException ignore) {
        
        }
        
        
        return true;
    }
}
