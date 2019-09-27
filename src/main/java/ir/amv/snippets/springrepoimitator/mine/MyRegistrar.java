package ir.amv.snippets.springrepoimitator.mine;

import ir.amv.snippets.springrepoimitator.IMyClass;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.repository.util.ClassUtils;

import java.io.IOException;
import java.util.Set;

public class MyRegistrar
        extends ClassPathScanningCandidateComponentProvider
        implements ImportBeanDefinitionRegistrar {

    public MyRegistrar() {
        super(false);
        addIncludeFilter(new InterfaceTypeFilter(IMyClass.class));
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Set<BeanDefinition> candidateComponents = findCandidateComponents("ir.amv");
        for (BeanDefinition candidateComponent : candidateComponents) {
            String beanClassName = candidateComponent.getBeanClassName();
            try {
                Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
                Class aClass = Class.forName(beanClassName);
                MyAnnot annotation = AnnotationUtils.findAnnotation(aClass, MyAnnot.class);
                if (annotation == null || !beanDefinitionRegistry.containsBeanDefinition(annotation.value())) {
                    ProxyFactory proxyFactory = new ProxyFactory();
                    proxyFactory.setTarget(new Object());
                    proxyFactory.setInterfaces(aClass);
                    Object bean = proxyFactory.getProxy();
                    BeanDefinitionBuilder b =
                            BeanDefinitionBuilder.genericBeanDefinition(aClass, () -> bean);
                    beanDefinitionRegistry.registerBeanDefinition(aClass.getSimpleName(), b.getBeanDefinition());
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

        /**
         * @param targetType
         */
        public InterfaceTypeFilter(Class<?> targetType) {
            super(targetType);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter#match(org.springframework.core.type.classreading.MetadataReader, org.springframework.core.type.classreading.MetadataReaderFactory)
         */
        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                throws IOException {

            return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
        }
    }
}
