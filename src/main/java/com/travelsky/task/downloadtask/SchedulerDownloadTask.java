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
 * 提供下载服务
 * @author gongp
 */
public class SchedulerDownloadTask extends BaseTask{
	
	private static final Log log= LogFactory.getLog(SchedulerDownloadTask.class);
	
	private static List<AgentServer> agentServerList =null;
	
	@Override
	public void doTask() {
		//每次直接从数据库里得到是否运行的值
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
			//置空防止重复读取数据
			agentServerList=null;
		}else{
			log.info("DOWNLOAD_IS_DOWNLOAD_DATA's value is "+isDownloadData+" task not run ");
		}
		
	}
	
	/**
	 * 从服务器上下载文件到本地服务器<先得到服务器文件列表,删除标志文件,下载目标文件,删除目标文件>
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
		//将向数据库里面读取(一次从一个服务器上面读取的最大的量)
		int maxReadFileSize = PropertiesUtil.getPropertieAsInteger(PropertiesUtil.DOWNLOAD_MAX_LOG_SIZE_PER_TIME);
		String localPath=PropertiesUtil.getPropertieAsString(PropertiesUtil.DOWNLOAD_LOG_PATH);
		String filename;
		for(int i=0;i<filenames.length;i++){
			filename=filenames[i];
			if(maxReadFileSize==0){
				log.info(" max file size :"+maxReadFileSize+"reached");
				break;
			}
			//查找以flag结尾的文件
			if(filename.endsWith("flag")){
				//删除标志文件
				boolean deleteFile =ftpUtil.deleteFile(filename);
				//下载文件的数量标志位减一
				maxReadFileSize--;
				String filenamelog = filename.replace("flag", "log");
				String filenameflag=filename;
				filenamerecorder.append(filenamelog);
				filenamerecorder.append("-");
				String localfilename=filenamelog;
				String localfilenameflag=filenameflag;
				//下载*.log文件并更名
				ftpUtil.downloadFile(filenamelog, localPath+File.separatorChar+localfilename);
				//新建*.flag文件
				File file = new File(localPath+File.separatorChar+localfilenameflag);
				file.createNewFile();
//				log.info("得到服务器上文件:"+filenamelog+"还可以下载"+MaxReadFileSize+"个文件");
				//删除标志的flag文件
				boolean deleteRealFile=ftpUtil.deleteFile(filenamelog);
				if(deleteFile&&deleteRealFile){
//						log.info("删除远程的"+filenameflag+"和"+filenamelog+"成功");
					}else{
						log.error("delete "+filenameflag+"and "+filenamelog+"failed");
					}
			}
		}
//		filenamerecorder.append("下载文件花费时间为:"+(System.currentTimeMillis()-start));
		//记录单个线程的下载内容
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
	 * 获取能下载的agentserver的数量
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
