package com.yi.consumer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yi.dubbo.service.IDubboService;

@Service
public class ConsumerDubboService {

	@Resource
	IDubboService dubboService;
	
	public void hello(String name){
		String msg = dubboService.say(name);
		System.out.println(msg);
	}
}
