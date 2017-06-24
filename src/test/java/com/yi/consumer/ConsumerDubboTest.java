package com.yi.consumer;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:application.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumerDubboTest {

	@Resource
	ConsumerDubboService consumerDubboService;
	
	@Test
	public void testDubbo(){
		consumerDubboService.hello("yif");
	}
}
