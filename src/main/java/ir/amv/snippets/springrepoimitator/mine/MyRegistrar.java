package ir.amv.snippets.springrepoimitator.mine;

import ir.amv.snippets.springrepoimitator.IMyClass;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class MyRegistrar
        extends ClassPathScanningCandidateComponentProvider
        implements ImportBeanDefinitionRegistrar {

    public MyRegistrar() {
        super(false);
        addIncludeFilter(new InterfaceTypeFilter(IMyClass.class));
        addIncludeFilter(new AnnotationTypeFilter(MyAnnot.class));
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Set<BeanDefinition> candidateComponents = findCandidateComponents("ir.amv");
        for (BeanDefinition candidateComponent : candidateComponents) {
            String beanClassName = candidateComponent.getBeanClassName();
            try {
                Class aClass = Class.forName(beanClassName);
                MyAnnot annotation = AnnotationUtils.findAnnotation(aClass, MyAnnot.class);
                assert annotation != null;
                if (!beanDefinitionRegistry.containsBeanDefinition(annotation.beanName())) {
                    ProxyFactory proxyFactory = new ProxyFactory();
                    proxyFactory.setTarget(new Object());
                    proxyFactory.setInterfaces(aClass);
                    proxyFactory.addAdvice((MethodInterceptor) methodInvocation -> {
                        System.out.println("Called:");
                        System.out.println(methodInvocation.getMethod());
                        System.out.println("args:");
                        System.out.println(Arrays.toString(methodInvocation.getArguments()));
                        return null;
                    });
                    Object bean = proxyFactory.getProxy();
                    BeanDefinitionBuilder b =
                            BeanDefinitionBuilder.genericBeanDefinition(aClass, () -> bean);
                    String beanName = annotation.beanName();
                    beanDefinitionRegistry.registerBeanDefinition(beanName, b.getBeanDefinition());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }

    private static class InterfaceTypeFilter extends AssignableTypeFilter {

        public InterfaceTypeFilter(Class<?> targetType) {
            super(targetType);
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                throws IOException {

            return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
        }
    }
}
