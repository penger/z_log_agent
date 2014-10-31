package com.travelsky.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

public class FtpUtil {
	
	private static Log log = LogFactory.getLog(FtpUtil.class);
	private FTPClient ftpClient = null;

	// ftp服务器地址
	private String hostName;

	// ftp服务器默认端口
	public static int defaultport = 21;



	// 登录名
	private String userName;

	// 登录密码
	private String password;

	// 需要访问的远程目录
	private String remoteDir;
	
	public FtpUtil(String hostName, String userName, String password) {
		this(hostName, 21, userName, password, "/", false);
	}
	
	public FtpUtil(String hostName, int port, String userName, String password) {
		this(hostName, port, userName, password, "/", false);
	}

	/** */
	/**
	 * @param hostName
	 *            主机地址
	 * @param port
	 *            端口号
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param remoteDir
	 *            默认工作目录
	 * @param is_zhTimeZone
	 *            是否是中文FTP Server端
	 * @return
	 */
	public FtpUtil(String hostName, int port, String userName, String password,
			String remoteDir, boolean is_zhTimeZone) {
		this.hostName = hostName;
		this.userName = userName;
		this.password = password;
		this.remoteDir = remoteDir == null ? "" : remoteDir;
		this.ftpClient = new FTPClient();
		if (is_zhTimeZone) {
			this.ftpClient.configure( FtpUtil.Config() );
			this.ftpClient.setControlEncoding("GBK");
		}
		//登录
		this.login();
		//切换目录
		this.changeDir(this.remoteDir);
		this.setFileType(FTPClient.STREAM_TRANSFER_MODE);
		
		ftpClient.setDefaultPort(port);
	}

	/**
	 * 登录FTP服务器
	 */
	public void login() {
		try {
			ftpClient.connect(this.hostName);
			ftpClient.login(this.userName, this.password);
		} catch (FTPConnectionClosedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static FTPClientConfig Config() {
		FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX );
		conf.setRecentDateFormatStr("MM月dd日 HH:mm");
		// conf.setRecentDateFormatStr("(YYYY年)?MM月dd日( HH:mm)?");
		return conf;
	}

	/**
	 * 变更工作目录
	 * 
	 * @param remoteDir
	 *  
	 */
	public void changeDir(String remoteDir) {
		try {
			this.remoteDir = remoteDir;
			ftpClient.changeWorkingDirectory(remoteDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回上一级目录(父目录)
	 */
	public void toParentDir() {
		try {
			ftpClient.changeToParentDirectory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出当前工作目录下所有文件
	 */
	public String[] ListAllFiles() {
		String[] names = this.ListFiles("*");
		return this.sort(names);
	}

	/**
	 * 列出指定工作目录下的匹配文件
	 * 
	 * @param dir
	 *            exp: /cim/
	 * @param file_regEx
	 *            通配符为*
	 */
	public String[] ListAllFiles(String dir, String file_regEx) {
		String[] names = this.ListFiles( dir + file_regEx );
		return this.sort(names);
	}

	/**
	 * 列出匹配文件
	 * 
	 * @param file_regEx
	 *            匹配字符,通配符为*
	 */
	public String[] ListFiles(String file_regEx) {
		try {
			/**//*
				 * FTPFile[] remoteFiles = ftpClient.listFiles(file_regEx);
				 * //System.out.println(remoteFiles.length); String[] name = new
				 * String[remoteFiles.length]; if(remoteFiles != null) { for(int
				 * i=0;i<remoteFiles.length;i++) { if(remoteFiles[i] == null)
				 * name[i] = ""; else
				 * if(remoteFiles[i].getName()==null||remoteFiles
				 * [i].getName().equals
				 * (".")||remoteFiles[i].getName().equals("..")) { name[i] = "";
				 * } else name[i] = remoteFiles[i].getName();
				 * System.out.println(name[i]); } }
				 */
			String[] name = ftpClient.listNames( file_regEx );
			if (name == null)
				return new String[0];
			
			return this.sort(name);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String[0];
	}

	/**
	 * 设置传输文件的类型[文本文件或者二进制文件]
	 * 
	 * @param fileType
	 *            --BINARY_FILE_TYPE,ASCII_FILE_TYPE
	 */
	public void setFileType(int fileType) {
		try {
			ftpClient.setFileType(fileType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传文件,新文件名同本地文件名
	 * 
	 * @param localFilePath
	 *            --本地文件路径+文件名
	 */
	public boolean uploadFile(String localFilePath) {
		return uploadFile(new File(localFilePath), null);
	}

	/**
	 * 上传文件
	 * 
	 * @param localFilePath
	 *            --本地文件路径+文件名
	 * @param newFileName
	 *            --新的文件名
	 */
	public boolean uploadFile(String localFilePath, String newFileName) {
		return uploadFile(new File(localFilePath), newFileName);
	}
	/**
	 * 上传文件
	 * 
	 * @param localFile
	 *            --本地文件
	 * @param newFileName
	 *            --新的文件名
	 */
	boolean uploadFile(File localFile, String newFileName) {
		if(!localFile.exists()) return true;
		if(null == newFileName || "".equals(newFileName)) newFileName = localFile.getName();
		
		boolean uploadSuccess = false;
		// 上传文件
		BufferedInputStream buffIn = null;
		try {
			buffIn = new BufferedInputStream(new FileInputStream(localFile));
			uploadSuccess = ftpClient.storeFile(newFileName, buffIn);
		} catch (Exception e) {
			e.printStackTrace();
			uploadSuccess = false;
		} finally {
			try {
				if (buffIn != null)
					buffIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return uploadSuccess;
	}

	/**
	 * 下载文件(单个)
	 * 
	 * @param remoteFileName
	 *            --服务器上的文件名
	 * @param localFileName
	 *            --本地文件名
	 * @throws Exception 
	 */
	public String downloadFile(String remoteFileName, String localFileName) throws Exception {
		BufferedOutputStream buffOut = null;
		try {
			buffOut = new BufferedOutputStream(new FileOutputStream(
					localFileName));
			ftpClient.retrieveFile(remoteFileName, buffOut);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (buffOut != null)
					buffOut.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return localFileName;
	}

	/** 
     * 重命名文件 
     * 
     * @param oldFileName 
     *            --原文件名 
     * @param newFileName 
     *            --新文件名 
     */  
    public boolean renameFile(String oldFileName, String newFileName) {  
        try {
            return ftpClient.rename(oldFileName, newFileName);  
        } catch (IOException ioe) {  
            ioe.printStackTrace(); 
            return false;
        }
    }
    
    /**
     * 删除文件
     * @param filename
     * @return
     */
    public boolean deleteFile(String filename) {
    	boolean flag = true;
    	try{
    		flag = ftpClient.deleteFile(filename);
    	} catch (IOException ioe) {  
            ioe.printStackTrace(); 
        }
    	return flag;
    }
    
    /**
     * 删除目录
     * @param pathname
     * @return
     */
    public boolean deleteDirectory(String pathname) {
    	boolean flag = true;
    	try{
    		ftpClient.removeDirectory(pathname);
    	} catch (IOException ioe) {  
            ioe.printStackTrace(); 
        }
    	return flag;
    }
	
	/**
	 * 关闭FTP连接
	 */
	public void close() {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 冒泡排序字符串(从大到小)
	 */
	String[] sort(String[] str_Array) {
		if (str_Array == null) {
			throw new NullPointerException("The str_Array can not be null!");
		}
		String tmp = "";
		for (int i = 0; i < str_Array.length; i++) {
			for (int j = 0; j < str_Array.length - i - 1; j++) {
				if (str_Array[j].compareTo(str_Array[j + 1]) < 0) {
					tmp = str_Array[j];
					str_Array[j] = str_Array[j + 1];
					str_Array[j + 1] = tmp;
				}
			}
		}
		return str_Array;
	}
	
	/**
??????????* 递归创建远程服务器目录
??????????* 
??????????* @param remote
??????????*?????????????????????? 远程服务器文件绝对路径
??????????* @param ftpClient
??????????*?????????????????????? FTPClient对象
??????????* @return 目录创建是否成功
??????????* @throws IOException
??????????*/
	public boolean createDirecroty(String remote) {
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		try {
			if (!directory.equalsIgnoreCase("/")
					&& !ftpClient.changeWorkingDirectory(new String(directory
							.getBytes("GBK"), "iso-8859-1"))) {
				// 如果远程目录不存在，则递归创建远程服务器目录
				int start = 0;
				int end = 0;
				if (directory.startsWith("/")) {
					start = 1;
				} else {
					start = 0;
				}
				end = directory.indexOf("/", start);
				while (true) {
					String subDirectory = new String(remote.substring(start, end)
							.getBytes("GBK"), "iso-8859-1");
					if (!ftpClient.changeWorkingDirectory(subDirectory)) {
						if (ftpClient.makeDirectory(subDirectory)) {
							ftpClient.changeWorkingDirectory(subDirectory);
						} else {
							log.info("创建目录失败");
							return false;
						}
					}

					start = end + 1;
					end = directory.indexOf("/", start);

					// 检查所有目录是否创建完毕
					if (end <= start) {
						break;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException{
//		FtpUtil ftpUtil = new FtpUtil("10.6.183.75", 21, "etdftp", "123", "/opt/app/WebSphere6/profiles/AppSrv02/logs/huServer/", false);
//		ftpUtil.downloadFile("MF_10_6_183_75_20131016132650.log", "templog/22.txt");
//		ftpUtil.close();
	}
		

}
