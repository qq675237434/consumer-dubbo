package com.yi.consumer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;

@Service
public class DynamicDubboService implements ApplicationContextAware{

	ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}

	
	public void  invoke(){
		String url = "com.yi.dubbo.service.IDynamicService";
		
		//application 
		/*ApplicationConfig app = new ApplicationConfig("yih");
		app.setOwner("yih");*/
		
		ApplicationConfig app = applicationContext.getBean(ApplicationConfig.class);
		
		//register
		/*RegistryConfig resistry = new RegistryConfig();
		resistry.setAddress("zookeeper://192.168.1.108:2181");*/
		
		RegistryConfig resistry = applicationContext.getBean(RegistryConfig.class);
		
		//ProtocolConfig
		/*ProtocolConfig protocol = new ProtocolConfig("dubbo" , 2088);*/
		ProtocolConfig protocol = applicationContext.getBean(ProtocolConfig.class);
		
		
		//provider
		/*ProviderConfig provider = new ProviderConfig();
		provider.setRetries(0);
		provider.setTimeout(1000);*/
		ProviderConfig provider = applicationContext.getBean(ProviderConfig.class);
		
		
		//consumer
		/*ConsumerConfig consumer = new ConsumerConfig();
		consumer.setCheck(false);*/
		ConsumerConfig consumer = applicationContext.getBean(ConsumerConfig.class);
		
		// 引用远程服务 
		ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>(); // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
		
		//设置application
		reference.setApplication(app);
		
		//register
		reference.setRegistry(resistry);
		
		//Protocol
		//reference.setProtocol("dubbo");
		
		//provider
		
		//consumer
		reference.setConsumer(consumer);
		
		
		
		reference.setInterface(url); // 弱类型接口名 
		//reference.setVersion("1.0.0"); 
		reference.setGeneric(true); // 声明为泛化接口 
		
		//re
		
		GenericService genericService = reference.get(); // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用 
		
		// 基本类型以及Date,List,Map等不需要转换，直接调用 
		Object result = genericService.$invoke("invoke", new String[] {"java.lang.String"}, new Object[] {"yif"}); 
		
		System.out.println("===================================");
		System.out.println(result);
		System.out.println("====================================");
		
	}
	
	
	public Object genericInvoke(String interfaceClass, String methodName, List<Map<String, Object>> parameters){

        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        reference.setApplication(applicationContext.getBean(ApplicationConfig.class)); 
        reference.setRegistry(applicationContext.getBean(RegistryConfig.class)); 
        reference.setInterface(interfaceClass); // 接口名 
        reference.setGeneric(true); // 声明为泛化接口 
        /*ReferenceConfig实例很重，封装了与注册中心的连接以及与提供者的连接，
        需要缓存，否则重复生成ReferenceConfig可能造成性能问题并且会有内存和连接泄漏。
        API方式编程时，容易忽略此问题。
        这里使用dubbo内置的简单缓存工具类进行缓存*/
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference); 
        // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用 

        int len = parameters.size();
        String[] invokeParamTyeps = new String[len];
        Object[] invokeParams = new Object[len];
        for(int i = 0; i < len; i++){
            invokeParamTyeps[i] = parameters.get(i).get("ParamType") + "";
            invokeParams[i] = parameters.get(i).get("Object");
        }
        return genericService.$invoke(methodName, invokeParamTyeps, invokeParams);
    }

	
}
