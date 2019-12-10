package com.safran.ses.casablanca.mytex.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.safran.ses.casablanca.mytex.service.UserService;
import com.safran.ses.casablanca.mytex.service.model.User;

@Component
public class UserServiceImpl implements UserService {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void createUser(User user) {
		entityManager.persist(user);
	}

	public List<User> getAllUsers() {
		return entityManager.createQuery("From User", User.class).getResultList();
	}
	
	@Transactional
	 public void deleteUser(User user) {
		User found = entityManager.find(User.class, user.getUserId());
		entityManager.remove(found);
	}
	
	@Transactional
	public void updateUser(User user){
		entityManager.merge(user);
	}
	@Transactional
	public void updateUserStatut(User user){
		User found = entityManager.find(User.class, user.getUserId());
		found.setActif(user.isActif());
		updateUser(found);
	}

}