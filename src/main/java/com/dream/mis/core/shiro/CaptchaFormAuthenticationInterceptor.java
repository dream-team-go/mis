package com.dream.mis.core.shiro;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**  
 * @Description: 验证拦截器(创建token)  
 * @author by
 */
public class CaptchaFormAuthenticationInterceptor extends FormAuthenticationFilter implements Interceptor {
    protected AuthenticationToken createToken(HttpServletRequest request) {
        String username = getUsername(request);
        String password = getPassword(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new UsernamePasswordToken(username, password, rememberMe, host);
    }

    @Override
    public void intercept(Invocation ai) {
        HttpServletRequest request = ai.getController().getRequest();
        AuthenticationToken authenticationToken = createToken(request);
        request.setAttribute("shiroToken", authenticationToken);
        ai.invoke();
    }

}
