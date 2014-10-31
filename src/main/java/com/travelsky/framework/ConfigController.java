package com.travelsky.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.dao.impl.ConfigDaoImpl;
import com.travelsky.domain.Config;

public class ConfigController {
	
	private static final Log log = LogFactory.getLog(ConfigController.class);

	private Map configMap = null;
	
	
	public String getCurrentExactFieldFromDB(String key){
		ConfigDaoImpl configDao = ManagerFactory.getConfigDao();
		String value = configDao.getExactFieldValue(key);
		return value;
	}
	  
    public synchronized void initConfigInDB(){
    	ConfigDaoImpl configDao = ManagerFactory.getConfigDao();
    	if(configMap==null){
    		configMap = new HashMap();
    		List list = configDao.getAll();
				Iterator it = list.iterator();
		    	while (it.hasNext()) {
					Config config = (Config) it.next();
					if(config.getAirlineId()==null||config.getAirlineId()==""){
						configMap.put(config.getKey(), config.getValue());// 
					}else{
						configMap.put(config.getAirlineId()+config.getKey(), config.getValue());// 
					}
				}
		    log.info("configmap  initialization finished config items size is :"+configMap.size());
    	}
		
    }

	public boolean getPropertyAsBoolean(String AirlineId,String propertyName) {
    	initConfigInDB();
        return Boolean.valueOf((String) configMap.get(AirlineId + propertyName)).booleanValue();
    }
    
    public int getPropertyAsInteger(String AirlineId,String propertyName) {
    	initConfigInDB();
        return Integer.valueOf((String) configMap.get(AirlineId + propertyName)).intValue();
    }
    
    public long getPropertyAsLong(String AirlineId,String propertyName) {
    	initConfigInDB();
        return Long.valueOf((String) configMap.get(AirlineId + propertyName)).longValue();
    }
    
    public String getPropertyAsString(String AirlineId,String propertyName) {
    	initConfigInDB();
        return (String) configMap.get(AirlineId + propertyName);
    }
    public String getPropertyAsString(String propertyName) {
    	initConfigInDB();
        return (String) configMap.get(propertyName);
    }

	public synchronized void refresh() {
		configMap = null;
		initConfigInDB();
	}

	public Map getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map configMap) {
		this.configMap = configMap;
	}
	
	
}
