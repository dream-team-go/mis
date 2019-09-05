package com.dream.mis.core.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import com.dream.mis.core.web.model.Function;
import com.dream.mis.core.web.model.Role;
import com.dream.mis.core.web.model.User;
import com.dream.mis.core.web.model.UserV;
import com.jfinal.kit.StrKit;

public class ShiroDbRealm extends AuthorizingRealm {
    
    public ShiroDbRealm(){
        setAuthenticationTokenClass(UsernamePasswordToken.class);
    }

    /**
     * 认证回调函数,登录时调用.
     * doGetAuthenticationInfo这个方法是在用户登录的时候调用的也就是执行SecurityUtils.getSubject().login（）的时候调用；(即:登录验证)
     */    
    @Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token){
    	UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
        String accountName = authcToken.getUsername();
        UserV user = UserV.dao.findByUsernameExceptDel(accountName);
        if (user.getInt("is_locked")==1) {
            throw new LockedAccountException("该用户已被锁定！");
        }
        SimpleAuthenticationInfo simpleUser= new SimpleAuthenticationInfo(new SimpleUser(user.getStr("user_id"),user.getStr("username"),user.getStr("role_name"),user.getStr("user_cn_name"),user.getStr("head_img"), user), user.getStr("password"), getName());
        simpleUser.setCredentialsSalt(ByteSource.Util.bytes(accountName)); //盐是用户名
        return simpleUser;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.（checkPermissions（）时调用）
     */
    @Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //User user = (User) principals.fromRealm(getName()).iterator().next();
    	SimpleUser simpleUser = (SimpleUser) principals.fromRealm(getName()).iterator().next();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if( null == simpleUser){
        	return info;
        }
        User user = User.dao.findById(simpleUser.getId());
        if( null == user){
        	return info;
        }
        List<Role> roles = user.getRoles(user.getStr("user_role_id"));
        if(roles.size()>0){
            for(Role role : roles){
                //角色的名称及时角色的值
                info.addRole(role.getStr("role_name"));
                addResourceOfRole(role,info);
            }
        }
        return info;
    }
    
    private void addResourceOfRole(Role role, SimpleAuthorizationInfo info){
    	List<Function> resources = role.getResources(role.getStr("role_id"));
        if(resources.size()>0){
            for(Function resource : resources ){
                //资源代码就是权限值，类似user：list;json;edit
            	if(StrKit.notBlank(resource.getStr("resource_name"))){
            		String[] resource_name = resource.getStr("resource_name").split(":");
            		if(resource_name!=null && resource_name.length>1){
            			String[] methodKey = resource_name[1].split(";");
            			if(methodKey!=null){
            				for (String mk : methodKey) {
            					if(StrKit.notBlank(mk)){
            						info.addStringPermission(resource_name[0]+":" + mk);//添加权限信息
            					}
            				}
            			}
            		} else {
            			//System.out.println(resource_name[0]);
            			info.addStringPermission(resource_name[0]);
            		}
            	}
            }
        }
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }
}
