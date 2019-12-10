package com.safran.ses.casablanca.mytex.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.safran.ses.casablanca.mytex.service.model.User;

public interface UserService {

	public void createUser(User user);
	
	public List<User> getAllUsers();
	
	public void deleteUser(User user);
	
	public void updateUser(User user);

	public void updateUserStatut(User user);
	
	public User findUser(String matricule,String password);
	
}