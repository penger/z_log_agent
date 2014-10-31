package com.travelsky.framework;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.travelsky.dao.impl.AgentServerDaoImpl;
import com.travelsky.dao.impl.BaseDaoImpl;
import com.travelsky.dao.impl.ConfigDaoImpl;
import com.travelsky.manager.IFareFlightManager;
import com.travelsky.manager.IMsiLogManager;
import com.travelsky.service.dbservice.IDBService;
import com.travelsky.service.logservice.ILogService;

public class ManagerFactory implements ApplicationContextAware {
	
    
	
	private static ApplicationContext context;
	
    private static ApplicationContext getContext() {
        return context;
    }
	public void setApplicationContext(ApplicationContext contex)  {
		LogFactory.getLog(this.getClass()).info("Spring  context initialization");
		context = contex;
	}
	
	static{
		String[] files=new String[]{
				"applicationContext_basic.xml",
				"applicationContext_dao.xml",
				"applicationContext_manager.xml",
				"applicationContext_service.xml"
		};
		context = new ClassPathXmlApplicationContext(files);
		System.out.println("spring context init finished in static block finished!");
	}
    
	public static ILogService getlogService() {
		if (getContext() != null) {
			return (ILogService) context.getBean("logService");
		}
		return null;
	}
	public static ConfigController getConfigController() {
		if (getContext() != null) {
			return (ConfigController) context.getBean("configController");
		}
		return null;
	}
	// service实例化end
	/**
	 * 由于增加了事务的控制,打算将dao层的获取全部改为spring的注入,且操作dao层必须经过Manager
	 * 下面的这些dao层的代码拟删除
	 */
	// dao实例化start
	public static BaseDaoImpl getBaseDao() {
		if (getContext() != null) {
			return (BaseDaoImpl) context.getBean("baseDao");
		}
		return null;
	}
	public static ConfigDaoImpl getConfigDao() {
		if (getContext() != null) {
			return (ConfigDaoImpl) context.getBean("configDao");
		}
		return null;
	}
	public static AgentServerDaoImpl getAgentServerDao() {
		if (getContext() != null) {
			return (AgentServerDaoImpl) context.getBean("agentServerDao");
		}
		return null;
	}
    //  dao实例化end
   
    /**
     * 得到应用实例service或dao
     * @param beanInstanceName 实例bean的名称
     * @return 应用实例
     * @author zhangli z013-9-18
     */
    
    
    //manager实例化start
	public static IMsiLogManager getMsiLogManager() {
		if(getContext()!=null){
			return (IMsiLogManager)context.getBean("msiLogManager");
		}
		return null;
	}
	public static IFareFlightManager getFareFlightManager() {
		if(getContext()!=null){
			return (IFareFlightManager)context.getBean("fareFlightManager");
		}
		return null;
	}
	public static IDBService getDBService() {
		if (getContext() != null) {
			return (IDBService) context.getBean("dbService");
		}
		return null;
	}
    //manager实例化end

}
