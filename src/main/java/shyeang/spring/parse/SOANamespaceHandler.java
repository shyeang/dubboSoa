package shyeang.spring.parse;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import shyeang.spring.configBean.Protocol;
import shyeang.spring.configBean.Reference;
import shyeang.spring.configBean.Registry;
import shyeang.spring.configBean.Service;


/**
 * @Description TODO 
 * @ClassName   SOANamespaceHandler 
 * @Date        2018年5月10日 下午10:03:20 
 * @Author      zg-jack 
 * 
 * 这个类是用来注册标签解析类的
 */  
    
public class SOANamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        this.registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParse(Registry.class));
        this.registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParse(Reference.class));
        this.registerBeanDefinitionParser("protocol", new ProtocolBeanDefinitionParse(Protocol.class));
        this.registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(Service.class));
    }
}
