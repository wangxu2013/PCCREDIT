package com.cardpay.pccredit.tools;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cardpay.pccredit.intopieces.dao.XmNewTeDao;

/**
 * @author 宋辰
 */
@Service
public class SXykStmtCurService {
	public Logger log = Logger.getLogger(SXykStmtCurService.class);
	
	@Autowired
	private XmNewTeDao xmNewTeDao;
	
	//****************************客户调额信息统计表(月)****************************//
	/**
	 * 台帐（部分）
	 * @param fileName
	 * @throws Exception
	 */
	public void saveTzbfDataFile(String fileName) throws Exception {
		ImportBankDataFileTools tools = new ImportBankDataFileTools();
		// 解析数据文件配置
		//List<DataFileConf> confList = tools.parseDataFileConf(tools.getFileFullName("/datamapping/sXykStmtCur.xml"));
		List<DataFileConf> confList = tools.parseDataFileConf("E:\\workspace\\pccredit_xm\\src\\conf\\datamapping\\tzbf.xml");
		// 解析”帐单记录表“数据文件
		//fileName = "C:\\Users\\Administrator\\Desktop\\aa\\STA_902_cmis_ACC_CREDIT_ADD_20151011.dat";
		List<Map<String, Object>> datas = tools.parseDataFile(fileName, confList);
		for(Map<String, Object> map : datas){
		    xmNewTeDao.insertTzbfCur(map);
		}
	}
	
	
	/**
	 * 交易（部分）
	 * @param fileName
	 * @throws Exception
	 */
	public void saveJybfDataFile(String fileName) throws Exception {
		ImportBankDataFileTools tools = new ImportBankDataFileTools();
		// 解析数据文件配置
		//List<DataFileConf> confList = tools.parseDataFileConf(tools.getFileFullName("/datamapping/sXykStmtCur.xml"));
		List<DataFileConf> confList = tools.parseDataFileConf("E:\\workspace\\pccredit_xm\\src\\conf\\datamapping\\jybf.xml");
		// 解析”帐单记录表“数据文件
		fileName = "C:\\Users\\Administrator\\Desktop\\aa\\STA_902_cmis_ACC_CREDIT_ADD_20151012.dat";
		List<Map<String, Object>> datas = tools.parseDataFile(fileName, confList);
		for(Map<String, Object> map : datas){
		    xmNewTeDao.insertJybfCur(map);
		}
	}
	
	//****************************客户台账表（日）****************************//
	/**
	 * 台帐全量
	 * @param fileName
	 * @throws Exception
	 */
	public void saveTzqlDataFile(String fileName) throws Exception {
		ImportBankDataFileTools tools = new ImportBankDataFileTools();
		// 解析数据文件配置
		//List<DataFileConf> confList = tools.parseDataFileConf(tools.getFileFullName("/datamapping/sXykStmtCur.xml"));
		List<DataFileConf> confList = tools.parseDataFileConf("E:\\workspace\\pccredit_xm\\src\\conf\\datamapping\\tzql.xml");
		// 解析”帐单记录表“数据文件
		fileName = "C:\\Users\\Administrator\\Desktop\\aa\\STA_902_cmis_ACC_CREDIT_ADD_20151011.dat";
		List<Map<String, Object>> datas = tools.parseDataFile(fileName, confList);
		for(Map<String, Object> map : datas){
		    xmNewTeDao.insertTzbfCur(map);
		}
	}
	
	//****************************客户交易数据表****************************//
	/**
	 * 交易全量
	 * @param fileName
	 * @throws Exception
	 */
	public void saveJyqlDataFile(String fileName) throws Exception {
		ImportBankDataFileTools tools = new ImportBankDataFileTools();
		// 解析数据文件配置
		//List<DataFileConf> confList = tools.parseDataFileConf(tools.getFileFullName("/datamapping/sXykStmtCur.xml"));
		List<DataFileConf> confList = tools.parseDataFileConf("E:\\workspace\\pccredit_xm\\src\\conf\\datamapping\\jybf.xml");
		// 解析”帐单记录表“数据文件
		fileName = "C:\\Users\\Administrator\\Desktop\\aa\\STA_902_cmis_ACC_CREDIT_ADD_20151012.dat";
		List<Map<String, Object>> datas = tools.parseDataFile(fileName, confList);
		for(Map<String, Object> map : datas){
		    xmNewTeDao.insertJybfCur(map);
		}
	}
	
	
	
	/*public static void main(String[] args) {
		try {
			ImportBankDataFileTools tools = new ImportBankDataFileTools();
			// 解析数据文件配置
			//List<DataFileConf> confList = tools.parseDataFileConf("E:\\workspace\\pccredit_xm\\src\\conf\\datamapping\\sXykStmtCur.xml");
			List<DataFileConf> confList = tools.parseDataFileConf(tools.getFileFullName("src/conf/datamapping/sXykStmtCur.xml"));
			// 解析”XX表“数据文件
			long start = System.currentTimeMillis();
			//台帐
			List<Map<String, Object>> datas = tools.parseDataFile("C:\\Users\\Administrator\\Desktop\\aa\\STA_902_cmis_ACC_CREDIT_ADD_20151010.dat", confList);
			for(Map<String, Object> map : datas){
				Iterator<String> keys = map.keySet().iterator(); 
				   while(keys.hasNext()) { 
				   String key = (String) keys.next(); 
				   String value=map.get(key).toString(); 
				   System.out.println(key+"*******"+value); 
				} 
			}
			long end = System.currentTimeMillis();
			System.out.println("读取文件内容花费：" + (end - start) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
}
