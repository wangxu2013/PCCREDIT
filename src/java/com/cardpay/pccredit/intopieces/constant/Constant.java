package com.cardpay.pccredit.intopieces.constant;

public class Constant {
	/* 操作失败 */
	public static String FAIL_MESSAGE = "操作失败";
	
	/* 操作成功 */
	public static String SUCCESS_MESSAGE = "操作成功";
	
	/* 操作成功 */
	public static String UPLOAD_SUCCESS_MESSAGE = "导出并上传成功";
	
	/* 文件为空 */
	public static String FILE_EMPTY = "文件不可为空";
	
	/* 结果集 */
	public static String RESULT_LIST1 = "resultList1";
	
	/* 结果集 */
	public static String RESULT_LIST2 = "resultList2";
	
	/* 影像资料上传路径 */
	public static String FILE_PATH = "/usr/pccreditFile/";
	
	/* 保存进件*/
	public static String SAVE_INTOPICES = "save";
	
	/* 申请进件*/
	public static String APPROVE_INTOPICES = "audit";  
	
	/* 拒绝进件*/
	public static String REFUSE_INTOPICES = "refuse";
	
	/* 成功进件*/
	public static String SUCCESS_INTOPICES = "success";
	
	/* 申请未通过*/
	public static String NOPASS_INTOPICES = "nopass";
	
	
	/*以下是上传状态*/
	public static String INITIAL_INTOPICES="initial";
	
	public static String  EXPORT_INTOPICES="export";
	
	public static String  UPLOAD_INTOPICES="upload";
	
	
	
	/* 申请已通过*/
	public static String APPROVED_INTOPICES = "approved";
	
	/*联系人*/
	public static String CONTACTID = "contactId";
	
	/*担保人*/
	public static String GUARANTORID = "guarantorId";
	
    /*推荐人*/
	public static String RECOMMENDID = "recommendId";
	
	/**
	 * 定时生成 默认用户
	 */
	public static String SCHEDULES_SYSTEM_USER = "system";
	
	
	/*FTP链接配置*/
	public static String FTPIP = "10.0.21.91";
	
	public static String FTPPORT = "21";
	
	public static String FTPUSERNAME = "ftpxm";
	
	public static String FTPPASSWORD = "fil@xm123";
	
	public static String FTPPATH = "/xm_request/";
	
	/*ftp下载到本地的临时文件	 */
	public static String FTPTODOWN="/var/ftp/makecard/";
	
	//系统默认产品
	public static String DEFAULT_TYPE = "1";
	//进件接收标志
	public static String recieve_type = "1";
	
	public static String APP_STATE_1="已申请";
	public static String APP_STATE_2="未申请";
	public static String APP_STATE_3="退回";
	
	public static String APP_STATE_END="end";
	
	//制卡返回结果
	public static String CARD_RETURN_TYPE1="成功";
	public static String CARD_RETURN_TYPE2="资料不全";
	public static String CARD_RETURN_TYPE3="格式错误";
	
	//商圈审批状态
	public static String SQ_APPROVE_TYPE_1="1";//未审批
	public static String SQ_APPROVE_TYPE_2="2";//通过
	public static String SQ_APPROVE_TYPE_3="3";//不通过
}
