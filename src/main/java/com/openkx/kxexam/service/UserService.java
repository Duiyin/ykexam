package com.openkx.kxexam.service;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.openkx.kxexam.dao.UserDao;
import com.openkx.kxexam.domain.CFormDto;
import com.openkx.kxexam.domain.PFormDto;
import com.openkx.kxexam.domain.RegisterDto;
import com.openkx.kxexam.domain.User;
import com.openkx.kxexam.domain.UserDto;
import com.openkx.kxexam.util.PasswordUtil;
import com.openkx.kxexam.util.ServiceException;
import com.openkx.kxexam.util.ValidTool;

@Component
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;

	public User register(HttpSession session, RegisterDto registerDto) {
		try {
			User checkRepeat = userDao.findByAccount(registerDto.getAccount());
			User user = new User();
			user.setAccount(registerDto.getAccount());
			user.setPassword(registerDto.getNpassword());
			if (null != checkRepeat) { // 查账号是否有重复
				throw new ServiceException("register", "account_exist");
			} else {
				if(ValidTool.isEmail(registerDto.getEmail())){
					user.setEmail(registerDto.getAccount());
				}
				BeanUtils.copyProperties(registerDto, user, User.class);
				userDao.save(user);
			}
			return user;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public User login(HttpSession session, String account, String password, String vcode){
		String verifycode = (String) session.getAttribute("VerifyCode");
		Integer login_count = (Integer) session.getAttribute("login_count");
		if(null != login_count && login_count >6){
			if(StringUtils.isNotBlank(verifycode) && StringUtils.equals(verifycode, vcode)){
			}else{
			}
		}
		User user = userDao.findByAccount(account);
		if (null != user) {
			if (!PasswordUtil.authenticatePassword(user.getPassword(), password)) {
				throw new ServiceException("register", "account_or_password_error");
			}
			String sessionid = session.getId().toLowerCase();
			session.setAttribute("userid", user);
			System.out.println("suser：update_login_sessionuser:" + user.getNickname() + sessionid);
			return user;
		} else {
			throw new ServiceException("register", "account_or_password_error");
		}
	}
	
	public User personalDataUpdate(String userid, PFormDto pFormDto){
		User user = userDao.findUserById(userid);
		
		//判断昵称是否为空， 为空则设置为初始名
		if(pFormDto.getNickname().isEmpty() || pFormDto.getNickname() == ""){
			pFormDto.setNickname(user.getIdentifier());
		}
		BeanUtils.copyProperties(pFormDto, user, User.class);
		userDao.update(user);
		return user;
	} 
	
	public User ContactDataUpdate(String userid, CFormDto cFormDto){
		User user = userDao.findUserById(userid);
		BeanUtils.copyProperties(cFormDto, user, User.class);
		userDao.update(user);
		return user;
	} 
}
