package com.snh48.datavisualization.shiro;

import com.snh48.datavisualization.pojo.Item;
import com.snh48.datavisualization.service.ManageService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: chenpeng
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private ManageService manageService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0){
//        System.out.println("授权");
        Subject subject=SecurityUtils.getSubject();
        Item item=(Item) subject.getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermission(item.getAuthority());
        return simpleAuthorizationInfo;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        // TODO Auto-generated method stub
//        System.out.println("认证");

        //shiro判断逻辑
        UsernamePasswordToken user = (UsernamePasswordToken) arg0;
        Item realUser = new Item();
        realUser.setName(user.getUsername());
        realUser.setPassword(String.copyValueOf(user.getPassword()));
        Item newUser = manageService.findItem(realUser.getName());
        //System.out.println(user.getUsername());

        if(newUser == null){
            //用户名错误
            //shiro会抛出UnknownAccountException异常
            return null;
        }

        return new SimpleAuthenticationInfo(newUser,newUser.getPassword(),"");
    }
}
