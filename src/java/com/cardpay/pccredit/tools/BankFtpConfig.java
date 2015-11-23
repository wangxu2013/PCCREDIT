package com.cardpay.pccredit.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 银行数据文件ftp配置信息
 * @author chenzhifang
 *
 * 2014-12-8下午3:43:34
 */
@Component
public class BankFtpConfig {
	// IP地址
	@Value("${bank.data.ftp.host}")
	private String host;
	// 用户名
	@Value("${bank.data.ftp.username}")
	private String username;
	// 密码
	@Value("${bank.data.ftp.password}")
	private String password;
	// 端口号
	@Value("${bank.data.ftp.port}")
	private int port;
	// 密钥文件路径
	@Value("${bank.data.ftp.privateKey}")
	private String privateKey;
	// 密钥口令
	@Value("${bank.data.ftp.passphrase}")
	private String passphrase;
	
	// ftp数据文件路径
	@Value("${bank.data.ftp.remothPath}")
	private String remothPath;
	
	// 远程ftp数据文件备份路径
	@Value("${bank.data.ftp.remothBackupPath}")
	private String remothBackupPath;
	
	// 本地下载目录
	@Value("${bank.data.ftp.local.downloadPath}")
	private String downloadPath;
	
	// 本地备份目录
	@Value("${bank.data.ftp.local.backupPath}")
	private String backupPath;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	public String getRemothPath() {
		return remothPath;
	}

	public void setRemothPath(String remothPath) {
		this.remothPath = remothPath;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}

	public String getRemothBackupPath() {
		return remothBackupPath;
	}

	public void setRemothBackupPath(String remothBackupPath) {
		this.remothBackupPath = remothBackupPath;
	}
}
