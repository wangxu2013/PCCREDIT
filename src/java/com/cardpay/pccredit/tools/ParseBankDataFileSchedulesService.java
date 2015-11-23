package com.cardpay.pccredit.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * 银行卸数
 * @author 宋辰
 *
 */
@Service
public class ParseBankDataFileSchedulesService {
	public Logger log = Logger.getLogger(ParseBankDataFileSchedulesService.class);
	public String curRemotePath = "";//保存本次remotePath
	
	@Autowired
	private SXykStmtCurService sXykStmtCurService;
	
	@Autowired
	private BankFtpConfig bankFtpConfig;
	
	/**
	 * 下载并解析银行数据文件定时任务
	 * @return
	 * @throws Exception 
	 */
	//@Scheduled(cron = "0 30 20 * * ?")
	public void downloadAndParseDataFileSchedules(){
		if(DataSourceContextHolder.getDbType()!=null &&!DataSourceContextHolder.getDbType().equalsIgnoreCase(DataSourceContextHolder.BANK)){
			DataSourceContextHolder.setDbType(DataSourceContextHolder.BANK);
		}
		Sftp sftp = null;
		try {
			sftp = new Sftp(bankFtpConfig.getHost(),bankFtpConfig.getUsername(), bankFtpConfig.getPassword(),
					 			bankFtpConfig.getPrivateKey(), bankFtpConfig.getPassphrase(),bankFtpConfig.getPort());
			sftp.connect();
			//获取前一天
			String fmt = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			Date date = new Date();
			String dateStr = sdf.format(date);
			//dateStr = "2015-03-09";
			curRemotePath = bankFtpConfig.getRemothPath()+DateUtils.getSpecifiedDayBefore(dateStr)+"/902";
			log.info("downloadAndParseDataFileSchedules path = "+ curRemotePath);
			//获取文件列表
			ArrayList<String> files = sftp.getList(curRemotePath);
			//清空中间表中前一天的数据
			//xmAccCreditService.deleteOld();
			//sXykAcctCurService.deleteOld();
			//sXykCardCurService.deleteOld();
			//sXykStmtCurService.deleteOld();
			//sXykCustrCurService.deleteOld();
			//agrCrdXykCunegService.deleteOld();
			// 处理ftp文件
			processFtpFile(dateStr,sftp, files);
			
		} catch (JSchException e) {
			log.error("", e);
		} catch (SftpException e) {
			log.error("", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(sftp != null){
				sftp.close();
				System.out.println("success,,/////");
			}
		}
		//flag = false;
	}
	
	/**
	 * 处理ftp文件
	 * @return
	 * @throws Exception 
	 */
	public void processFtpFile(String obj,Sftp sftp, ArrayList<String> files) throws Exception{
		Iterator<String> pathIterator = files.iterator();
		// 下载路径
		String downloadPath =  bankFtpConfig.getDownloadPath();
		downloadPath = URLDecoder.decode(downloadPath, "utf-8");
		// 备份路径
		String backupPath = bankFtpConfig.getBackupPath();
		backupPath = URLDecoder.decode(backupPath, "utf-8");
		
		//获取前一天
		String fmt = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Date date = new Date();
		String dateStr = sdf.format(date);
		dateStr = obj;
		backupPath  = backupPath + File.separator + DateUtils.getSpecifiedDayBefore(dateStr);
		File backupPathDir = new File(backupPath);
		//判断文件夹是否存在,如果不存在则创建文件夹
		if (!backupPathDir.exists()) {
			backupPathDir.mkdir();
		}
		while(pathIterator.hasNext()){
			String file = pathIterator.next();
			try{
				//if(!bankDataFileProcessService.isExist(file)){
					//下载文件
					if(sftp.download(curRemotePath, file, downloadPath)){
						//判断是否压缩文件
						String gzFile = downloadPath + File.separator + file;
						if(gzFile.endsWith(".gz"))
						{
							ExpandGZ.doExpand(gzFile);
							//删除压缩文件
							/*try{
								new File(srcFilePath).delete();
							}catch(Exception e){
								e.getStackTrace();
							}*/
							//修改文件名从.gz为.dat
							int to = gzFile.lastIndexOf('.');
							gzFile = gzFile.substring(0, to);
							
							to = file.lastIndexOf('.');
							file = file.substring(0, to);
						}
						// 备份文件
						boolean flag = backupFile(gzFile, backupPath);
						if(flag){
							// 解析数据文件TODO
							//saveParseDataFile(backupPath, file);
						}
						
						//if(!sftp.remove(bankFtpConfig.getRemothPath(), file, bankFtpConfig.getRemothBackupPath())){
						//	log.error("移动文件" + file + "失败");
						//}
					}else{
						log.error("下载文件" + file + "失败");
					}
				//}else{
					//删除文件
					//sftp.delete(bankFtpConfig.getRemothPath(), file);
				//}
			}catch(Exception e){
				log.error("处理文件" + file + "出错", e);
			}
		}
	}
	
	
	@Scheduled(cron = "0 39 16 * * ?")
	private void dosynchMethod() throws IOException{
  //public void saveParseDataFile(String path, String fileName) throws Exception
		List<String> spFile = new ArrayList<String>();
		//TODO 测试
		String path = "C:\\Users\\Administrator\\Desktop\\bb\\";
		String fileName = "STA_902_cmis_ACC_CREDIT_ADD_20151010.dat";
		//判断文件大小，超过50M的先分割
		File f= new File(path + File.separator + fileName);
		if (f.exists() && f.isFile()){
			if(f.length()>50000000){
				int spCount = (int) (f.length()/50000000);
				SPTxt.splitTxt(path + File.separator + fileName,spCount);
				int to = fileName.lastIndexOf('.');
		    	fileName = fileName.substring(0, to);
				for(int i=0;i<spCount;i++){
					spFile.add(fileName+"_"+i+".dat");
				}
			}else{
				spFile.add(fileName);
			}
		}
		//写入数据
		for(String fn : spFile){
			try{
				if(fn.startsWith("STA_902_cmis_ACC_CREDIT_ADD_")) {
					//台帐部分数据
					sXykStmtCurService.saveTzbfDataFile(path + File.separator + fn);
				}else if(fn.startsWith("STA_902_cmis_ACC_CREDIT_jy_")){//TODO
					//交易部分数据
					sXykStmtCurService.saveJybfDataFile("");
				}else if(fn.startsWith("STA_902_cmis_ACC_CREDIT_jy_")){//TODO
					//交易全量数据
					//sXykStmtCurService.saveJybfDataFile("");
				}
				else if(fn.startsWith("STA_902_cmis_ACC_CREDIT_jy_")){//TODO
					//台帐全量数据
					//sXykStmtCurService.saveJybfDataFile("");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 备份并删除原始数据文件
	 * @return
	 * @throws Exception 
	 */
	public boolean backupFile(String srcFilePath, String toPathString) {
		File srcF = new File(srcFilePath);
		File toFilePath = new File(toPathString);
		if (!toFilePath.exists()){
			toFilePath.mkdirs();
		}
		String dstFilePath = toPathString + File.separator + srcF.getName();
		if(copyFile(srcFilePath, dstFilePath)){
			try{
				srcF.delete();
				return true;
			}catch(Exception e){
				e.getStackTrace();
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 拷贝数据文件
	 * @param srcFilePath
	 * @param dstFilePath
	 * @return
	 * @throws Exception 
	 */
	public boolean copyFile(String srcFilePath, String dstFilePath) {
		try {
			int byteread = 0;
			File srcFile = new File(srcFilePath);
			if (srcFile.exists()) {
				InputStream inputStream = new FileInputStream(srcFilePath);
				FileOutputStream outputStream = new FileOutputStream(dstFilePath);
				byte[] buffer = new byte[1024];
				while ((byteread = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, byteread);
				}
				inputStream.close();
				outputStream.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
