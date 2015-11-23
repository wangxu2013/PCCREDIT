/**
 * 
 */
package com.cardpay.pccredit.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * @author fengxiao
 *
 */
public class Sftp {
	
	protected String host;//sftp服务器IP
	protected String username;//用户名
	protected String password;//密码
	protected String privateKey;//密钥文件路径
	protected String passphrase;//密钥口令
	protected int port = 22;//默认的sftp端口号是22
	private ChannelSftp sftp = null;
	
	/**
	 * @param args
	 */
	public Sftp(String host, String username, String password, String prvKey, String passphrase, int port){
		this.host = host;
		this.username = username;
		this.password = password;
		this.privateKey = prvKey;
		this.passphrase = passphrase;
		this.port = port;
	}
	
	public void connect() throws JSchException, SftpException{
		JSch jsch = new JSch();
		if(privateKey != null && !"".equalsIgnoreCase(privateKey)){
			if(passphrase != null && !"".equalsIgnoreCase(passphrase)){
				jsch.addIdentity(privateKey, passphrase);
			}else{
				jsch.addIdentity(privateKey);
			}
		}
		Session session = jsch.getSession(username, host, port);
		if(password != null && !password.equalsIgnoreCase("")){
			session.setPassword(password);
		}
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		
		session.setConfig(sshConfig);
		
		session.setServerAliveInterval(92000);
		session.connect();
		
		Channel channel = session.openChannel("sftp");
		
		channel.connect();
		sftp = (ChannelSftp) channel;
		sftp.setFilenameEncoding("GBK");
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<String> getList(String directory) throws SftpException{
		ArrayList<String> files = new ArrayList<String>();
		Vector vv = sftp.ls(directory);
		if(vv != null){
			for(int i = 0; i < vv.size(); i++){
				Object obj = vv.elementAt(i);
				if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
					String path = ((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getFilename();
					if(path.startsWith("STA_902_cmis_ACC_CREDIT_ADD_")
							|| path.startsWith("STA_902_djk_STA_YW_DJK_ACCT_ADD_")
							|| path.startsWith("STA_902_djk_STA_YW_DJK_CARD_ADD_")
							|| path.startsWith("STA_902_djk_STA_YW_DJK_STMT_ADD_")
							|| path.startsWith("STA_902_djk_STA_YW_DJK_CUSTR_ADD_")
							|| path.startsWith("STA_902_djk_STA_YW_DJK_CUNEG_ADD_"))
					{
						if(path.endsWith(".txt") || path.endsWith(".dbs") || path.endsWith(".dat") 
								|| path.endsWith(".txt.gz") || path.endsWith(".dbs.gz") || path.endsWith(".dat.gz")){
							files.add(path);
						}
					}
					
	            }
			}
		}
		return files;
	}
	
	/**
     * 上传文件
     * 
     * @param directory 上传的目录
     *            
     * @param uploadFile 要上传的文件
     *            
     * @param sftp
     */
    public void upload(String directory, String uploadFile) {
        try {
        	if(sftp == null){
        		connect(); //如果没连接上sftp则连接
        	}
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	/**
     * 下载文件
     * 
     * @param directory 下载目录
     *            
     * @param downloadFile 下载的文件
     *            
     * @param saveFile 存在本地的路径
     *            
     */
    public boolean download(String directory, String downloadFile,
            String saveFile) {
        try {
        	if(sftp == null){
        		connect(); //如果没连接上sftp则连接
        	}
            sftp.cd(directory);
            sftp.get(downloadFile,saveFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除文件
     * 
     * @param directory 要删除文件所在目录
     *            
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) {
        try {
        	if(sftp == null){
        		connect(); //如果没连接上sftp则连接
        	}
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 移动文件
     * @param directory 移动文件所在的目录
     * @param removeFile 移动文件名
     * @param removePath 移动文件目标目录
     */
    public boolean remove(String directory, String removeFile, String removePath){
    	try {
        	if(sftp == null){
        		connect(); //如果没连接上sftp则连接
        	}
        	if(removePath.endsWith("/")){
        		removePath = removePath.substring(0, removePath.length()-1);
        	}
        	
        	if(directory.endsWith("/")){
        		directory = directory.substring(0, directory.length()-1);
        	}
        	
            sftp.rename(directory+"/"+removeFile, removePath+"/"+removeFile);
            return true;
    	} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public SftpATTRS getFileStatu(String filePath){
    	try {
        	if(sftp == null){
        		connect(); //如果没连接上sftp则连接
        	}
            return sftp.stat(filePath);
        } catch (Exception e) {
            
        }
    	return null;
    }
    
    public void mkdir(String path){
    	
    	try {
        	if(sftp == null){
        		connect(); //如果没连接上sftp则连接
        	}
        	sftp.mkdir(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭sftp连接
     */
    public void close(){
        if (sftp != null) {
            try {
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            sftp.disconnect();
        }
    }
}
