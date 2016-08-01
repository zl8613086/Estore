package com.zl.service;

import com.zl.annotation.Tran;
import com.zl.domain.User;

public interface UserService extends Service{

	/**
	 * 
	 * @param user
	 */
	@Tran
	void regist(User user);
/**
 * 
 */
	User active(String activecode);
	/**
	 * 
	 * @param username
	 * @param password
	 */
User getUserByNameAndPsw(String username, String password);

/**
 * 
 * @param username
 * @return
 */
	boolean hasName(String username);

}
