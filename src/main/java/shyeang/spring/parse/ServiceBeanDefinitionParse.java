package shyeang.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServiceBeanDefinitionParse implements BeanDefinitionParser {
    
    private Class<?> beanClass;
    
    public ServiceBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //怎么才能够把element里面的属性值传到Reference，并且交给spring实例化
        //如果要涉及到一个类的实例化交给spring去实例，那么我们就可以new 一个 BeanDefinition对象
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(beanClass);
        rootBeanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");
        String intf = element.getAttribute("interface");
        String ref = element.getAttribute("ref");
        
        if (id != null && !"".equals(id)) {
            if (parserContext.getRegistry().containsBeanDefinition(id)) {
                throw new IllegalStateException("spring has this id");
            }
            rootBeanDefinition.getPropertyValues().add("id", id);
        }
        else {
            //如果id没有配置，那么就是类名的首字母小写作为一个类的唯一标识
            id = beanClass.getName().substring(0, 1).toLowerCase()
                    + beanClass.getName().substring(1);
            rootBeanDefinition.getPropertyValues().add("id", id);
        }
        
        if(intf == null || "".equals(intf)) {
            throw new IllegalStateException("intf 不能为空！！！");
        }
        
        rootBeanDefinition.getPropertyValues().add("intf", intf);
        rootBeanDefinition.getPropertyValues().add("ref", ref);
        
        //这个创建出来的rootBeanDefinition对象必须要交个spring管理
        parserContext.getRegistry().registerBeanDefinition(id, rootBeanDefinition);
        
        return rootBeanDefinition;
    }
}
