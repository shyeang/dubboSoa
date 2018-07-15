package shyeang.spring.configBean;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import shyeang.advice.InvokeInvocationHandler;
import shyeang.invoke.HttpInvoke;
import shyeang.invoke.Invoke;
import shyeang.invoke.NettyInvoke;
import shyeang.invoke.RmiInvoke;
import shyeang.registry.BaseRegistryDelegate;


public class Reference implements FactoryBean,ApplicationContextAware,InitializingBean {
    private String id;
    
    private String intf;
    
    private String check;
    
    private String protocol;
    
    private Invoke invoke;
    
    private ApplicationContext application;
    
    private static Map<String,Invoke> invokeMaps = new HashMap<String,Invoke>();

    /** 
     * @Fields registryInfo 本地缓存注册中心中的服务列表信息
     */  
        
    private List<String> registryInfo = new ArrayList<String>();
    
    
    static {
        invokeMaps.put("http", new HttpInvoke());
        invokeMaps.put("rmi", new RmiInvoke());
        invokeMaps.put("netty", new NettyInvoke());
        invokeMaps.put("jack", new NettyInvoke());
    }
    
    /* 
     * 返回一个对象，然后被spring容器管理
     * 
     * 这个方法要返回 intf这个接口的代理实例
     */
    public Object getObject() throws Exception {
        
        if(protocol != null && !"".equals(protocol)) {
            invoke = invokeMaps.get(protocol);
        } else {
            Protocol protocol = application.getBean(Protocol.class);
            if(protocol != null) {
                invoke = invokeMaps.get(protocol.getName());
            } else {
                invoke = invokeMaps.get("http");
            }
        } 
        
        Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[] {Class.forName(intf)},
                new InvokeInvocationHandler(invoke,this));
        
        return proxy;
    }
    
    /* 
     * 返回实例的类型
     */
    public Class getObjectType() {
        try {
            if (intf != null && !"".equals(intf)) {
                return Class.forName(intf);
            }
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /* 
     * 是否单例
     */
    public boolean isSingleton() {
        return true;
    }
    
    public String getId() {
        return id;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.application = applicationContext;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getIntf() {
        return intf;
    }
    
    public void setIntf(String intf) {
        this.intf = intf;
    }
    
    public String getCheck() {
        return check;
    }
    
    public void setCheck(String check) {
        this.check = check;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void afterPropertiesSet() throws Exception {
        registryInfo = BaseRegistryDelegate.getRegistry(id, application);
    }
}
