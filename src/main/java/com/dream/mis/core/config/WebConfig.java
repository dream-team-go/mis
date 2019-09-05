package com.dream.mis.core.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.dream.mis.core.handler.WebSocketHandler;
import com.dream.mis.core.interceptor.LogInterceptor;
import com.dream.mis.core.interceptor.MenuInterceptor;
import com.dream.mis.core.plugin.shiro.ShiroInterceptor;
import com.dream.mis.core.plugin.shiro.ShiroPlugin;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log4jLogFactory;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
/**
 * API引导式配置
 */
public class WebConfig extends JFinalConfig {
	Routes routes;
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		PropKit.use("web_config.txt");
		loadPropertyFile("web_config.txt");//加载少量必要配置，随后可用getProperty(...)获取值
		me.setDevMode(getPropertyToBoolean("devMode", true));
		me.setViewType(ViewType.JSP);//设置视图类型为Jsp, 否则默认为FreeMarker
		me.setBaseViewPath("WEB-INF/view");
		me.setMaxPostSize(104857600);//100M
		me.setErrorView(401, "/login");//未登录
		me.setErrorView(403,"/error/403.jsp");//权限不足
		me.setError404View("/error/404.jsp");
		me.setError500View("/error/500.jsp");
		me.setLogFactory(new Log4jLogFactory());
	}
	
	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		//me.setBaseViewPath("/WEB-INF/view");
		new AutoBindRoutes();
		me.add(new AutoBindRoutes());//自动设置路由
		this.routes = me;
	}
	
	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		//数据源插件
		DruidPlugin druid = new DruidPlugin(getProperty("jdbcUrl").trim(), getProperty("user").trim(), getProperty("password").trim());
		druid.addFilter(new StatFilter());// SQL监控
		me.add(druid);
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(druid,SimpleNameStyles.UP_UNDERLINE);//自动映射
		atbp.setShowSql(true);
		atbp.setDialect(new MysqlDialect());
		me.add(atbp);
		
//		//多数据源
//		DruidPlugin zving = new DruidPlugin(getProperty("sync.jdbcUrl").trim(), getProperty("sync.user").trim(), getProperty("sync.password").trim());
//		me.add(zving);
//		ActiveRecordPlugin arp = new ActiveRecordPlugin("sync", zving);
//		me.add(arp);
		
		//缓存配置
		me.add(new EhCachePlugin("/"+PathKit.getWebRootPath() +"/WEB-INF/classes/ehcache.xml"));
		
		//me.add(new QuartzPlugin("job.properties"));//定时调度任务配置
		
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
		me.add(shiroPlugin);
		
		// 初始化插件 可以参考 cn.kunming.iss.zving.controller.BranchController中index方法
//		EventPlugin eventPlugin = new EventPlugin();
		// 开启全局异步//设置扫描jar包，默认不扫描 //设置监听器默认包，默认全扫描//启动
//		eventPlugin.async().scanJar().scanPackage("cn.kunming").start();
//		me.add(eventPlugin);
		
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		//me.add(new SessionInterceptor());
		me.add(new ShiroInterceptor());
		me.add(new LogInterceptor());
		me.add(new MenuInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("root"));
		me.add(new WebSocketHandler("^/websocket"));
		//me.add(new UrlSkipHandler("^/websocket", false));
	}
	@Override
	public void afterJFinalStart() {
		JFinal.me().getServletContext().setAttribute("baseUrl", PropKit.get("baseUrl"));
		JFinal.me().getServletContext().setAttribute("hotline", PropKit.get("hotline"));
		super.afterJFinalStart();
	}
	
	
	/**
	 * 运行此 main 方法可以启动项目,此main方法可以放置在任意的Class类定义中,不一定要放于此
	 */
	public static void main(String[] args) {
		//JFinal.start("WebRoot", 8080, "/hlxd", 5);
		//System.out.println(String.valueOf(null));
	}


}
