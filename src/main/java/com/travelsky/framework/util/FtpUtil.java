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

	// ftp��������ַ
	private String hostName;

	// ftp������Ĭ�϶˿�
	public static int defaultport = 21;



	// ��¼��
	private String userName;

	// ��¼����
	private String password;

	// ��Ҫ���ʵ�Զ��Ŀ¼
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
	 *            ������ַ
	 * @param port
	 *            �˿ں�
	 * @param userName
	 *            �û���
	 * @param password
	 *            ����
	 * @param remoteDir
	 *            Ĭ�Ϲ���Ŀ¼
	 * @param is_zhTimeZone
	 *            �Ƿ�������FTP Server��
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
		//��¼
		this.login();
		//�л�Ŀ¼
		this.changeDir(this.remoteDir);
		this.setFileType(FTPClient.STREAM_TRANSFER_MODE);
		
		ftpClient.setDefaultPort(port);
	}

	/**
	 * ��¼FTP������
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
		conf.setRecentDateFormatStr("MM��dd�� HH:mm");
		// conf.setRecentDateFormatStr("(YYYY��)?MM��dd��( HH:mm)?");
		return conf;
	}

	/**
	 * �������Ŀ¼
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
	 * ������һ��Ŀ¼(��Ŀ¼)
	 */
	public void toParentDir() {
		try {
			ftpClient.changeToParentDirectory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �г���ǰ����Ŀ¼�������ļ�
	 */
	public String[] ListAllFiles() {
		String[] names = this.ListFiles("*");
		return this.sort(names);
	}

	/**
	 * �г�ָ������Ŀ¼�µ�ƥ���ļ�
	 * 
	 * @param dir
	 *            exp: /cim/
	 * @param file_regEx
	 *            ͨ���Ϊ*
	 */
	public String[] ListAllFiles(String dir, String file_regEx) {
		String[] names = this.ListFiles( dir + file_regEx );
		return this.sort(names);
	}

	/**
	 * �г�ƥ���ļ�
	 * 
	 * @param file_regEx
	 *            ƥ���ַ�,ͨ���Ϊ*
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
	 * ���ô����ļ�������[�ı��ļ����߶������ļ�]
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
	 * �ϴ��ļ�,���ļ���ͬ�����ļ���
	 * 
	 * @param localFilePath
	 *            --�����ļ�·��+�ļ���
	 */
	public boolean uploadFile(String localFilePath) {
		return uploadFile(new File(localFilePath), null);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param localFilePath
	 *            --�����ļ�·��+�ļ���
	 * @param newFileName
	 *            --�µ��ļ���
	 */
	public boolean uploadFile(String localFilePath, String newFileName) {
		return uploadFile(new File(localFilePath), newFileName);
	}
	/**
	 * �ϴ��ļ�
	 * 
	 * @param localFile
	 *            --�����ļ�
	 * @param newFileName
	 *            --�µ��ļ���
	 */
	boolean uploadFile(File localFile, String newFileName) {
		if(!localFile.exists()) return true;
		if(null == newFileName || "".equals(newFileName)) newFileName = localFile.getName();
		
		boolean uploadSuccess = false;
		// �ϴ��ļ�
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
	 * �����ļ�(����)
	 * 
	 * @param remoteFileName
	 *            --�������ϵ��ļ���
	 * @param localFileName
	 *            --�����ļ���
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
     * �������ļ� 
     * 
     * @param oldFileName 
     *            --ԭ�ļ��� 
     * @param newFileName 
     *            --���ļ��� 
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
     * ɾ���ļ�
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
     * ɾ��Ŀ¼
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
	 * �ر�FTP����
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
	 * ð�������ַ���(�Ӵ�С)
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
??????????* �ݹ鴴��Զ�̷�����Ŀ¼
??????????* 
??????????* @param remote
??????????*?????????????????????? Զ�̷������ļ�����·��
??????????* @param ftpClient
??????????*?????????????????????? FTPClient����
??????????* @return Ŀ¼�����Ƿ�ɹ�
??????????* @throws IOException
??????????*/
	public boolean createDirecroty(String remote) {
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		try {
			if (!directory.equalsIgnoreCase("/")
					&& !ftpClient.changeWorkingDirectory(new String(directory
							.getBytes("GBK"), "iso-8859-1"))) {
				// ���Զ��Ŀ¼�����ڣ���ݹ鴴��Զ�̷�����Ŀ¼
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
							log.info("����Ŀ¼ʧ��");
							return false;
						}
					}

					start = end + 1;
					end = directory.indexOf("/", start);

					// �������Ŀ¼�Ƿ񴴽����
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
