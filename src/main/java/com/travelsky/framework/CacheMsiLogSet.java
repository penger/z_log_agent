package com.travelsky.framework;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.Config;
import com.travelsky.domain.MsiLog;
import com.travelsky.task.msilogtask.MsiLogTask;

public class CacheMsiLogSet {
	
	private static int  maxlogsize;
	private static int  inserttimeout;
	
	private static CacheMsiLogSet set = null;
	
	//ȫ�ֲ�����־Vector
	public static Vector opLogDataVector=new Vector();  
	
	private static final Log log =LogFactory.getLog(CacheMsiLogSet.class);
	
	public static synchronized CacheMsiLogSet getInstance(){
		if(set==null){
			set = new CacheMsiLogSet();
			maxlogsize=ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.INSERT_MAX_SIZE_FOR_MSILOG);
			inserttimeout=ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.INSERT_MSILOG_TIME_OUT);
		}
		return set;
	}
	//add �¼���Ϊ�������������Բ����趨��ѯ��ʱ��!
	public  void addMsiLog2Vector(MsiLog	 msiLog) {
		//�Ƚ����ݷ���
		opLogDataVector.add(msiLog);
		//����������ﵽ�˹̶�������ôִ������
		if(opLogDataVector.size()>=maxlogsize-1){
			MsiLogTask.doMsiLogTask();
			opLogDataVector.removeAllElements();
			return ;
		}
		//�����ϴ�ִ�е�ʱ�������������,��ôִ������
		if(MsiLogTask.lastfinishtime+inserttimeout>System.currentTimeMillis()){
			MsiLogTask.doMsiLogTask();
			opLogDataVector.removeAllElements();
		}
	}
	
}
