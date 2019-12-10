package com.safran.ses.casablanca.mytex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.safran.ses.casablanca.mytex.service.model.Job;
import com.safran.ses.casablanca.mytex.service.model.User;


@Component
public class Main {

	/**
	 * @param args
	 */
	
	
//	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("/databaseConfig.xml");
		Main m= context.getBean(Main.class);
		
		User user = new User();
		
		Job jb = new Job();
		
		jb.setJobLabel("Job1");
		
		user.setFirstName("BASMA");
		user.setLastName("EL KADIRI");
		m.userService.createUser(user);
		
		m.userService.getAllUsers();
		
		
		

	}
	
	

}
