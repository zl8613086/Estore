package com.zl.dao;

import java.sql.Connection;

import com.zl.domain.User;

public interface UserDao extends Dao{

	/**
	 * 
	 * @param username
	 * @param conn 
	 * @return
	 */
	User findUserByName(String username);
/**
 * 
 * @param user
 * @param conn 
 */
	void addUser(User user);
	/**
	 * 
	 * @param activecode
	 * @return
	 */
User findUserByActivecode(String activecode);
/**
 * 
 * @param id
 */
	void delUser(int id);
	/**
	 * 
	 * @param id
	 * @param i
	 */
void updateState(int id, int i);
/**
 * 
 * @param username
 * @param password
 * @return
 */
	User getUserByNameAndPsw(String username, String password);
	/**
	 * 
	 * @param user_id
	 * @return
	 */
User findUserById(int user_id);

}
