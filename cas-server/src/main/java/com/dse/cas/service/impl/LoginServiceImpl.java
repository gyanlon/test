package com.dse.cas.service.impl;

import java.util.List;
import java.util.Map;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.handler.PasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.jasig.cas.authentication.UsernamePasswordCredential;

import com.dse.cas.CONSTANT;
import com.dse.cas.service.LoginService;


/**
 * Created by zhoujc on 2016/10/20.
 */
public class LoginServiceImpl implements LoginService {

    private JdbcTemplate jdbcTemplate;

    private PasswordEncoder passwordEncoder;

    /**
     *-1 : 用户名或密码为空
     * 0 : 用户名不存在
     * 1 : 密码不相等
     * 2 : 密码相等
     */
    @Override
    public Integer checkLogin(Credential credential) {
        try {
            String sql = "select password from t_sys_user where del_flag = '0' and status = '1' and user_name= ?";
            String username =((UsernamePasswordCredential) credential).getUsername();
            String password = ((UsernamePasswordCredential) credential).getPassword();
            //用户名为空
            if(username == null || "".equals(username)){
                return CONSTANT.LOGIN_NULL_USER_NAME;
            }
            //密码为空
            if(password == null || "".equals(password)){
                return CONSTANT.LOGIN_NULL_PWD;
            }
            List list = jdbcTemplate.queryForList(sql, username);
            //用户名不存在
            if(list.size() == 0){
                return CONSTANT.LOGIN_NO_USER_NAME;
            }
            Map<String, Object> user = (Map<String, Object>) list.get(0);
            String pwd = user.get("PASSWORD").toString();
            //密码错误
            if(!pwd.equals(passwordEncoder.encode(password))){
                return CONSTANT.LOGIN_WRONG_PWD;
            }
            //密码相等
            else {
                return CONSTANT.LOGIN_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    @Override
	public Integer checkUsername(Credential credentials) {
    	try {
            String sql = "select password from t_sys_user where del_flag = '0' and status = '1' and user_name= ?";
            String username =credentials.getId();
            List list = jdbcTemplate.queryForList(sql, username);
            //用户名不存在
            if(list.size() == 0){
                return CONSTANT.LOGIN_NO_USER_NAME;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CONSTANT.LOGIN_SUCCESS;
	}
    

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


	
}
