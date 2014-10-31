package com.travelsky.task.downloadtask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.dao.impl.AgentServerDaoImpl;
import com.travelsky.domain.AgentServer;
import com.travelsky.domain.Config;
import com.travelsky.domain.MsiLog;
import com.travelsky.framework.CacheMsiLogSet;
import com.travelsky.framework.ManagerFactory;
import com.travelsky.framework.util.DateUtil;
import com.travelsky.framework.util.FtpUtil;
import com.travelsky.framework.util.PropertiesUtil;
import com.travelsky.task.BaseTask;

/**
 * �ṩ���ط���
 * @author gongp
 */
public class SchedulerDownloadTask extends BaseTask{
	
	private static final Log log= LogFactory.getLog(SchedulerDownloadTask.class);
	
	private static List<AgentServer> agentServerList =null;
	
	@Override
	public void doTask() {
		//ÿ��ֱ�Ӵ����ݿ���õ��Ƿ����е�ֵ
		int isDownloadData = Integer.valueOf(ManagerFactory.getConfigController().getCurrentExactFieldFromDB(Config.DOWNLOAD_TASK_RUN));
		if(isDownloadData==1){
			if(agentServerList==null){
				getAgentServerList();
				if(agentServerList==null||agentServerList.size()==0){
					log.info("there hasn't any agent server to download");
					return;
				}
			}
			for(AgentServer agentServer : agentServerList){
				try {
					downloadFileFromServer(agentServer);
				} catch (Exception e) {
					log.error("download error occored at:	"+agentServer.getIp()+"detail is "+e.getMessage());
//					e.printStackTrace();
				}
			}
			//�ÿշ�ֹ�ظ���ȡ����
			agentServerList=null;
		}else{
			log.info("DOWNLOAD_IS_DOWNLOAD_DATA's value is "+isDownloadData+" task not run ");
		}
		
	}
	
	/**
	 * �ӷ������������ļ������ط�����<�ȵõ��������ļ��б�,ɾ����־�ļ�,����Ŀ���ļ�,ɾ��Ŀ���ļ�>
	 * @param server
	 * @return
	 * @throws Exception 
	 */
	private void downloadFileFromServer(AgentServer server) throws Exception{
		StringBuffer filenamerecorder=new StringBuffer();
		long start = System.currentTimeMillis();
		FtpUtil ftpUtil=null;
		ftpUtil=new FtpUtil(server.getIp(), 21, server.getUsername(), server.getPassword(), server.getLogpath(), false);
		String[] filenames = ftpUtil.ListAllFiles();
		if(filenames==null||filenames.length==0){
//			log.info("no file in "+server);
			return ;
		}else{
			log.info(" there are	"+filenames.length+" files  where IP "+server.getIp()+" Path: "+server.getLogpath());
		}
		//�������ݿ������ȡ(һ�δ�һ�������������ȡ��������)
		int maxReadFileSize = PropertiesUtil.getPropertieAsInteger(PropertiesUtil.DOWNLOAD_MAX_LOG_SIZE_PER_TIME);
		String localPath=PropertiesUtil.getPropertieAsString(PropertiesUtil.DOWNLOAD_LOG_PATH);
		String filename;
		for(int i=0;i<filenames.length;i++){
			filename=filenames[i];
			if(maxReadFileSize==0){
				log.info(" max file size :"+maxReadFileSize+"reached");
				break;
			}
			//������flag��β���ļ�
			if(filename.endsWith("flag")){
				//ɾ����־�ļ�
				boolean deleteFile =ftpUtil.deleteFile(filename);
				//�����ļ���������־λ��һ
				maxReadFileSize--;
				String filenamelog = filename.replace("flag", "log");
				String filenameflag=filename;
				filenamerecorder.append(filenamelog);
				filenamerecorder.append("-");
				String localfilename=filenamelog;
				String localfilenameflag=filenameflag;
				//����*.log�ļ�������
				ftpUtil.downloadFile(filenamelog, localPath+File.separatorChar+localfilename);
				//�½�*.flag�ļ�
				File file = new File(localPath+File.separatorChar+localfilenameflag);
				file.createNewFile();
//				log.info("�õ����������ļ�:"+filenamelog+"����������"+MaxReadFileSize+"���ļ�");
				//ɾ����־��flag�ļ�
				boolean deleteRealFile=ftpUtil.deleteFile(filenamelog);
				if(deleteFile&&deleteRealFile){
//						log.info("ɾ��Զ�̵�"+filenameflag+"��"+filenamelog+"�ɹ�");
					}else{
						log.error("delete "+filenameflag+"and "+filenamelog+"failed");
					}
			}
		}
//		filenamerecorder.append("�����ļ�����ʱ��Ϊ:"+(System.currentTimeMillis()-start));
		//��¼�����̵߳���������
		filenamerecorder.append(server.getLogpath());
		MsiLog msiLog = new MsiLog();
		msiLog.setContent("download:"+(PropertiesUtil.getPropertieAsInteger(PropertiesUtil.DOWNLOAD_MAX_LOG_SIZE_PER_TIME)-maxReadFileSize)+"files"+filenamerecorder.toString());
		msiLog.setIp(server.getIp());
		msiLog.setKeyWord("downloadThread");
		msiLog.setOpTime(DateUtil.date2longNormal(new Date()));
		msiLog.setLev("info");
		CacheMsiLogSet.getInstance().addMsiLog2Vector(msiLog);
	}

	/**
	 * ��ȡ�����ص�agentserver������
	 */
	private  void getAgentServerList(){
		AgentServerDaoImpl agentserverdao = ManagerFactory.getAgentServerDao();
		agentServerList = agentserverdao.getAll();
		List<AgentServer> toOperServerList = new ArrayList<AgentServer>();
		int totleServer = agentServerList.size();
		for(int i=0;i<totleServer;i++){
			if(agentServerList.get(i).getStatus().equals("1")){
				toOperServerList.add(agentServerList.get(i));
			}
		}
		agentServerList=toOperServerList;
	}

}
