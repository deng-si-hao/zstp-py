package com.cavin.culture.config;

import com.cavin.culture.util.SHAUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
* 登录时的密码验证
*
* */
public class CredentialMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String password = SHAUtil.getSHA256(new String(usernamePasswordToken.getPassword()));
        String dbPassword = (String) info.getCredentials();
        return this.equals(password, dbPassword);
    }
}
