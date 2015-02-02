package com.cardpay.pccredit.xm_appln.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.constant.WfProcessInfoType;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.intopieces.constant.ApplicationStatusEnum;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.constant.IntoPiecesException;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationInfoService;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationIntopieceWaitService;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationProcessService;
import com.cardpay.pccredit.product.filter.ProductFilter;
import com.cardpay.pccredit.product.model.ProductAttribute;
import com.cardpay.pccredit.product.service.ProductService;
import com.cardpay.pccredit.system.constants.NodeAuditTypeEnum;
import com.cardpay.pccredit.system.constants.YesNoEnum;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.pccredit.system.service.NodeAuditService;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_ADDR;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_DBXX;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_DCSC;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_HKSZ;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_JCZL;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_KHED;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_KHFW;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_KHLB;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_KPMX;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_LXRZL;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_POZL;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_QTXYKXX;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_SQED;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_TJINFO;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_YWXX;
import com.cardpay.pccredit.xm_appln.model.XM_APPLN_ZXQSZL;
import com.cardpay.pccredit.xm_appln.service.XM_APPLN_Service;
import com.cardpay.workflow.models.WfProcessInfo;
import com.cardpay.workflow.models.WfStatusInfo;
import com.cardpay.workflow.models.WfStatusResult;
import com.cardpay.workflow.service.ProcessService;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.i18n.I18nHelper;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.dictionary.DictionaryManager;
import com.wicresoft.jrad.modules.dictionary.model.Dictionary;
import com.wicresoft.jrad.modules.dictionary.model.DictionaryItem;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;

@Controller
@RequestMapping("/xm_appln/*")
@JRadModule("xm_appln")
public class XM_APPLN_Controller extends BaseController {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private NodeAuditService nodeAuditService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private CustomerApplicationInfoService customerApplicationInfoService;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private XM_APPLN_Service xM_APPLN_Service;
	
	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	
	@Autowired
	private CustomerApplicationProcessService customerApplicationProcessService;
	
	//跳转到page0
	@ResponseBody
	@RequestMapping(value = "xm_appln_page0.page")
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView xm_appln_page0(HttpServletRequest request) {        
		//JRadModelAndView mv = new JRadModelAndView("/xm_appln/xm_appln_page1", request);
		JRadModelAndView mv = new JRadModelAndView("/xm_appln/xm_appln_page0", request);
		return mv;
	}
	
	//page0保存
	@ResponseBody
	@RequestMapping(value = "xm_appln_page0_update.json")
	@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap xm_appln_page0_update(@ModelAttribute XM_APPLN_NEW_CUSTOMER_FORM xM_APPLN_NEW_CUSTOMER_FORM, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				String customerId = request.getParameter("customer_id");
				customerId = xM_APPLN_Service.insertXM_APPLN_NEW_CUSTOMER(customerId,xM_APPLN_NEW_CUSTOMER_FORM,user);
				returnMap.put(RECORD_ID, customerId);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//page0申请
	@ResponseBody
	@RequestMapping(value = "xm_appln_page0_apply.json")
	@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap xm_appln_page0_apply(@ModelAttribute XM_APPLN_NEW_CUSTOMER_FORM xM_APPLN_NEW_CUSTOMER_FORM, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				String customerId = request.getParameter("customer_id");
				customerId = xM_APPLN_Service.insertXM_APPLN_NEW_CUSTOMER(customerId,xM_APPLN_NEW_CUSTOMER_FORM,user);
				
				//设置流程开始
				startProcess(customerId);
				
				returnMap.put(RECORD_ID, customerId);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//跳转到iframe
	@ResponseBody
	@RequestMapping(value = "changewh_xm_appln.page")
	@JRadOperation(JRadOperation.MAINTENANCE)
	public AbstractModelAndView changewh_xm_appln(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/xm_appln/iframe", request);
		
		String customerInforId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "aid");
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforservice.findCustomerInforById(customerInforId);
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
			mv.addObject("appId", appId);
		}
		return mv;
	}
	
	//iframe跳转到第page1
	@ResponseBody
	@RequestMapping(value = "xm_appln_page1_update.page")
	@JRadOperation(JRadOperation.MAINTENANCE)
	public AbstractModelAndView changewh_xm_appln_page1_update(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/xm_appln/xm_appln_page1_update", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforservice.findCustomerInforById(customerInforId);
			//查找xm_appln相关信息
			XM_APPLN_JCZL xM_APPLN_JCZL = xM_APPLN_Service.findXM_APPLN_JCZLByCustomerId(customerInforId);
			XM_APPLN_KHFW xM_APPLN_KHFW = xM_APPLN_Service.findXM_APPLN_KHFWByCustomerId(customerInforId);
			XM_APPLN_KHED xM_APPLN_KHED = xM_APPLN_Service.findXM_APPLN_KHEDByCustomerId(customerInforId);
			XM_APPLN_KHLB xM_APPLN_KHLB = xM_APPLN_Service.findXM_APPLN_KHLBByCustomerId(customerInforId);
			XM_APPLN_POZL xM_APPLN_POZL = xM_APPLN_Service.findXM_APPLN_POZLByCustomerId(customerInforId);
			List<XM_APPLN_LXRZL> xM_APPLN_LXRZL_ls = xM_APPLN_Service.findXM_APPLN_LXRZLByCustomerId(customerInforId);
			if(xM_APPLN_LXRZL_ls == null || xM_APPLN_LXRZL_ls.size() == 0){
				xM_APPLN_LXRZL_ls = new ArrayList<XM_APPLN_LXRZL>();
				XM_APPLN_LXRZL obj = new XM_APPLN_LXRZL();
				obj.setLsh(0);
				obj.setCustomer_id(customerInforId);
				xM_APPLN_LXRZL_ls.add(obj);
				XM_APPLN_LXRZL obj2 = new XM_APPLN_LXRZL();
				obj2.setLsh(1);
				obj2.setCustomer_id(customerInforId);
				xM_APPLN_LXRZL_ls.add(obj2);
			}
			XM_APPLN_ZXQSZL xM_APPLN_ZXQSZL = xM_APPLN_Service.findXM_APPLN_ZXQSZLByCustomerId(customerInforId);
			mv.addObject("xM_APPLN_JCZL", xM_APPLN_JCZL);
			mv.addObject("xM_APPLN_KHFW", xM_APPLN_KHFW);
			mv.addObject("xM_APPLN_KHED", xM_APPLN_KHED);
			mv.addObject("xM_APPLN_KHLB", xM_APPLN_KHLB);
			mv.addObject("xM_APPLN_POZL", xM_APPLN_POZL);
			mv.addObject("xM_APPLN_LXRZL_ls", xM_APPLN_LXRZL_ls);
			mv.addObject("xM_APPLN_ZXQSZL", xM_APPLN_ZXQSZL);
			
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
		}
		return mv;
	}
		
	//iframe跳转到第page2
	@ResponseBody
	@RequestMapping(value = "xm_appln_page2.page")
	@JRadOperation(JRadOperation.MAINTENANCE)
	public AbstractModelAndView changewh_xm_appln_page2(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/xm_appln/xm_appln_page2", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforservice.findCustomerInforById(customerInforId);
			//查找xm_appln相关信息
			XM_APPLN xM_APPLN = this.xM_APPLN_Service.findXM_APPLNByCustomerId(customerInforId);
			XM_APPLN_SQED xM_APPLN_SQED = this.xM_APPLN_Service.findXM_APPLN_SQEDByCustomerId(customerInforId);
			List<XM_APPLN_KPMX> xM_APPLN_KPMX_ls = this.xM_APPLN_Service.findXM_APPLN_KPMXByCustomerId(customerInforId);
			if(xM_APPLN_KPMX_ls == null || xM_APPLN_KPMX_ls.size() == 0){
				xM_APPLN_KPMX_ls = new ArrayList<XM_APPLN_KPMX>();
				XM_APPLN_KPMX obj = new XM_APPLN_KPMX();
				obj.setLsh(0);
				obj.setCustomer_id(customerInforId);
				xM_APPLN_KPMX_ls.add(obj);
				XM_APPLN_KPMX obj2 = new XM_APPLN_KPMX();
				obj2.setLsh(1);
				obj2.setCustomer_id(customerInforId);
				xM_APPLN_KPMX_ls.add(obj2);
			}
			XM_APPLN_HKSZ xM_APPLN_HKSZ = this.xM_APPLN_Service.findXM_APPLN_HKSZByCustomerId(customerInforId);
			XM_APPLN_DBXX xM_APPLN_DBXX = this.xM_APPLN_Service.findXM_APPLN_DBXXByCustomerId(customerInforId);
			XM_APPLN_QTXYKXX xM_APPLN_QTXYKXX = this.xM_APPLN_Service.findXM_APPLN_QTXYKXXByCustomerId(customerInforId);
			XM_APPLN_YWXX xM_APPLN_YWXX = this.xM_APPLN_Service.findXM_APPLN_YWXXByCustomerId(customerInforId);
			XM_APPLN_DCSC xM_APPLN_DCSC = this.xM_APPLN_Service.findXM_APPLN_DCSCByCustomerId(customerInforId);
			mv.addObject("xM_APPLN", xM_APPLN);
			mv.addObject("xM_APPLN_SQED", xM_APPLN_SQED);
			mv.addObject("xM_APPLN_KPMX_ls", xM_APPLN_KPMX_ls);
			mv.addObject("xM_APPLN_HKSZ", xM_APPLN_HKSZ);
			mv.addObject("xM_APPLN_DBXX", xM_APPLN_DBXX);
			mv.addObject("xM_APPLN_QTXYKXX", xM_APPLN_QTXYKXX);
			mv.addObject("xM_APPLN_YWXX", xM_APPLN_YWXX);
			mv.addObject("xM_APPLN_DCSC", xM_APPLN_DCSC);
			
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
		}
		return mv;
	}
	
	//iframe跳转到第page3
	@ResponseBody
	@RequestMapping(value = "xm_appln_page3.page")
	@JRadOperation(JRadOperation.MAINTENANCE)
	public AbstractModelAndView changewh_xm_appln_page3(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/xm_appln/xm_appln_page3", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforservice.findCustomerInforById(customerInforId);
			//查找xm_appln信息
			XM_APPLN_TJINFO xM_APPLN_TJINFO = this.xM_APPLN_Service.findXM_APPLN_TJINFOByCustomerId(customerInforId);
			XM_APPLN_ADDR xM_APPLN_ADDR = this.xM_APPLN_Service.findXM_APPLN_ADDRByCustomerId(customerInforId);
			XM_APPLN xM_APPLN = this.xM_APPLN_Service.findXM_APPLNByCustomerId(customerInforId);
			XM_APPLN_JCZL xM_APPLN_JCZL = xM_APPLN_Service.findXM_APPLN_JCZLByCustomerId(customerInforId);
			List<XM_APPLN_KPMX> xM_APPLN_KPMX_ls = this.xM_APPLN_Service.findXM_APPLN_KPMXByCustomerId(customerInforId);
			if(xM_APPLN_KPMX_ls == null || xM_APPLN_KPMX_ls.size() == 0){
				xM_APPLN_KPMX_ls = new ArrayList<XM_APPLN_KPMX>();
				XM_APPLN_KPMX obj = new XM_APPLN_KPMX();
				obj.setLsh(0);
				obj.setCustomer_id(customerInforId);
				xM_APPLN_KPMX_ls.add(obj);
				XM_APPLN_KPMX obj2 = new XM_APPLN_KPMX();
				obj2.setLsh(1);
				obj2.setCustomer_id(customerInforId);
				xM_APPLN_KPMX_ls.add(obj2);
			}
			mv.addObject("xM_APPLN_TJINFO", xM_APPLN_TJINFO);
			mv.addObject("xM_APPLN_ADDR", xM_APPLN_ADDR);
			mv.addObject("xM_APPLN", xM_APPLN);
			mv.addObject("xM_APPLN_JCZL", xM_APPLN_JCZL);
			mv.addObject("xM_APPLNKPMX_ls", xM_APPLN_KPMX_ls);
			
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
		}
		return mv;
	}
		
	//iframe跳转到第page4
	@ResponseBody
	@RequestMapping(value = "xm_appln_page4.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.CREATE)
	public AbstractModelAndView xm_appln_page4(HttpServletRequest request) {
		String customerId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "appId");
		CustomerInfor customerInfor = customerInforservice.findCustomerInforById(customerId);
		
		//查找相关xm_xppln信息
		XM_APPLN xM_APPLN = xM_APPLN_Service.findXM_APPLNByCustomerId(customerId);
		XM_APPLN_SQED xM_APPLN_SQED = xM_APPLN_Service.findXM_APPLN_SQEDByCustomerId(customerId);
		List<XM_APPLN_KPMX> xM_APPLN_KPMX_ls = xM_APPLN_Service.findXM_APPLN_KPMXByCustomerId(customerId);
		XM_APPLN_HKSZ xM_APPLN_HKSZ = xM_APPLN_Service.findXM_APPLN_HKSZByCustomerId(customerId);
		XM_APPLN_DBXX xM_APPLN_DBXX = xM_APPLN_Service.findXM_APPLN_DBXXByCustomerId(customerId);
		XM_APPLN_QTXYKXX xM_APPLN_QTXYKXX = xM_APPLN_Service.findXM_APPLN_QTXYKXXByCustomerId(customerId);
		XM_APPLN_DCSC xM_APPLN_DCSC = xM_APPLN_Service.findXM_APPLN_DCSCByCustomerId(customerId);
		XM_APPLN_TJINFO xM_APPLN_TJINFO = xM_APPLN_Service.findXM_APPLN_TJINFOByCustomerId(customerId);
		XM_APPLN_ADDR xM_APPLN_ADDR = xM_APPLN_Service.findXM_APPLN_ADDRByCustomerId(customerId);
		
		//转化数据字典value为显示值
		xM_APPLN.setProduct(conventDic2Title("PRODUCT", xM_APPLN.getProduct()));
		xM_APPLN.setAddl_card((xM_APPLN.getAddl_card()!=null&&xM_APPLN.getAddl_card().equals("1"))?"是":"否");
		xM_APPLN.setRush_card((xM_APPLN.getRush_card()!=null&&xM_APPLN.getRush_card().equals("1"))?"是":"否");
		xM_APPLN.setApp_source(conventDic2Title("APP_SOURCE", xM_APPLN.getApp_source()));
		xM_APPLN.setRush_fee((xM_APPLN.getRush_fee()!=null&&xM_APPLN.getRush_fee().equals("1"))?"是":"否");
		xM_APPLN.setAcc_type(conventDic2Title("ACC_TYPE", xM_APPLN.getAcc_type()));
		xM_APPLN.setGtoc((xM_APPLN.getGtoc()!=null&&xM_APPLN.getGtoc().equals("1"))?"是":"否");
		
		for(XM_APPLN_KPMX obj : xM_APPLN_KPMX_ls){
			obj.setEmboss_cd((obj.getEmboss_cd()!=null&&obj.getEmboss_cd().equals("1"))?"是":"否");
			obj.setPin_reqd((obj.getPin_reqd()!=null&&obj.getPin_reqd().equals("1"))?"是":"否");
			obj.setSms_yn((obj.getSms_yn()!=null&&obj.getSms_yn().equals("1"))?"是":"否");
			obj.setPin_chk((obj.getPin_chk()!=null&&obj.getPin_chk().equals("1"))?"是":"否");
			obj.setCdfrm(conventDic2Title("CDFRM", obj.getCdfrm()));
			if(obj.getAtm().equals("MR"))
				obj.setAtm("取产品新卡参数");
			if(obj.getAtm().equals("BKT"))
				obj.setAtm("不开通");
			if(obj.getAtm().equals("KT"))
				obj.setAtm("开通");
			if(obj.getTele().equals("MR"))
				obj.setTele("取产品新卡参数");
			if(obj.getTele().equals("BKT"))
				obj.setTele("不开通");
			if(obj.getTele().equals("KT"))
				obj.setTele("开通");
			if(obj.getNet().equals("MR"))
				obj.setNet("取产品新卡参数");
			if(obj.getNet().equals("BKT"))
				obj.setNet("不开通");
			if(obj.getNet().equals("KT"))
				obj.setNet("开通");
			if(obj.getPhone().equals("MR"))
				obj.setPhone("取产品新卡参数");
			if(obj.getPhone().equals("BKT"))
				obj.setPhone("不开通");
			if(obj.getPhone().equals("KT"))
				obj.setPhone("开通");
		}
		
		xM_APPLN_HKSZ.setRepay_code(conventDic2Title("REPAY_CODE", xM_APPLN_HKSZ.getRepay_code()));
		xM_APPLN_HKSZ.setRepay_codx(conventDic2Title("REPAY_CODX", xM_APPLN_HKSZ.getRepay_codx()));
		xM_APPLN_HKSZ.setExch_flag(conventDic2Title("EXCH_FLAG", xM_APPLN_HKSZ.getExch_flag()));
		xM_APPLN_HKSZ.setExch_code(conventDic2Title("EXCH_CODE", xM_APPLN_HKSZ.getExch_code()));
		
		xM_APPLN_DBXX.setGuarn_code(conventDic2Title("GUARN_CODE", xM_APPLN_DBXX.getGuarn_code()));
		
		if(xM_APPLN_QTXYKXX.getXrefcode1().equals("WU")){
			xM_APPLN_QTXYKXX.setXrefcode1("无");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode1().equals("BH")){
			xM_APPLN_QTXYKXX.setXrefcode1("本行");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode1().equals("TH")){
			xM_APPLN_QTXYKXX.setXrefcode1("他行");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode2().equals("WU")){
			xM_APPLN_QTXYKXX.setXrefcode2("无");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode2().equals("BH")){
			xM_APPLN_QTXYKXX.setXrefcode2("本行");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode2().equals("TH")){
			xM_APPLN_QTXYKXX.setXrefcode2("他行");
		}
		
		xM_APPLN_TJINFO.setIntr_recom(conventDic2Title("INTR_RECOM", xM_APPLN_TJINFO.getIntr_recom()));
		xM_APPLN_TJINFO.setBrnch_intr(conventDic2Title("BRNCH_INTR", xM_APPLN_TJINFO.getBrnch_intr()));
		xM_APPLN_TJINFO.setIntr_rl(conventDic2Title("INTR_RL", xM_APPLN_TJINFO.getIntr_rl()));
		xM_APPLN_TJINFO.setIntr_qc(conventDic2Title("INTR_QC", xM_APPLN_TJINFO.getIntr_qc()));
		
		xM_APPLN_ADDR.setAddr1_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr1_type()));
		xM_APPLN_ADDR.setAddr2_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr2_type()));
		xM_APPLN_ADDR.setAddr3_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr3_type()));
		xM_APPLN_ADDR.setAddr4_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr4_type()));
		xM_APPLN_ADDR.setOsea_f1((xM_APPLN_ADDR.getOsea_f1()!=null&&xM_APPLN_ADDR.getOsea_f1().equals("1"))?"是":"否");
		xM_APPLN_ADDR.setOsea_f2((xM_APPLN_ADDR.getOsea_f2()!=null&&xM_APPLN_ADDR.getOsea_f2().equals("1"))?"是":"否");
		xM_APPLN_ADDR.setOsea_f3((xM_APPLN_ADDR.getOsea_f3()!=null&&xM_APPLN_ADDR.getOsea_f3().equals("1"))?"是":"否");
		xM_APPLN_ADDR.setOsea_f4((xM_APPLN_ADDR.getOsea_f4()!=null&&xM_APPLN_ADDR.getOsea_f4().equals("1"))?"是":"否");
		
		xM_APPLN.setMail_to(conventDic2Title("MAIL_TO", xM_APPLN.getMail_to()));
		
		JRadModelAndView mv = new JRadModelAndView("/xm_appln/xm_appln_page4", request);
		
		mv.addObject("customerId", customerId);
		mv.addObject("customer", customerInfor);
		mv.addObject("appId", appId);
		mv.addObject("xM_APPLN", xM_APPLN);
		mv.addObject("xM_APPLN_SQED", xM_APPLN_SQED);
		mv.addObject("xM_APPLN_KPMX_ls", xM_APPLN_KPMX_ls);
		mv.addObject("xM_APPLN_HKSZ", xM_APPLN_HKSZ);
		mv.addObject("xM_APPLN_DBXX", xM_APPLN_DBXX);
		mv.addObject("xM_APPLN_QTXYKXX", xM_APPLN_QTXYKXX);
		mv.addObject("xM_APPLN_DCSC", xM_APPLN_DCSC);
		mv.addObject("xM_APPLN_TJINFO", xM_APPLN_TJINFO);
		mv.addObject("xM_APPLN_ADDR", xM_APPLN_ADDR);
		
		return mv;
	}
	
	//iframe跳转到第page5
	@ResponseBody
	@RequestMapping(value = "xm_appln_page5.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.CREATE)
	public AbstractModelAndView xm_appln_page5(HttpServletRequest request) {
		String customerId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "appId");
		CustomerInfor customerInfor = customerInforservice.findCustomerInforById(customerId);
		
		//查找相关xm_xppln信息
		XM_APPLN xM_APPLN = xM_APPLN_Service.findXM_APPLNByCustomerId(customerId);
		XM_APPLN_SQED xM_APPLN_SQED = xM_APPLN_Service.findXM_APPLN_SQEDByCustomerId(customerId);
		List<XM_APPLN_KPMX> xM_APPLN_KPMX_ls = xM_APPLN_Service.findXM_APPLN_KPMXByCustomerId(customerId);
		XM_APPLN_HKSZ xM_APPLN_HKSZ = xM_APPLN_Service.findXM_APPLN_HKSZByCustomerId(customerId);
		XM_APPLN_DBXX xM_APPLN_DBXX = xM_APPLN_Service.findXM_APPLN_DBXXByCustomerId(customerId);
		XM_APPLN_QTXYKXX xM_APPLN_QTXYKXX = xM_APPLN_Service.findXM_APPLN_QTXYKXXByCustomerId(customerId);
		XM_APPLN_DCSC xM_APPLN_DCSC = xM_APPLN_Service.findXM_APPLN_DCSCByCustomerId(customerId);
		XM_APPLN_TJINFO xM_APPLN_TJINFO = xM_APPLN_Service.findXM_APPLN_TJINFOByCustomerId(customerId);
		XM_APPLN_ADDR xM_APPLN_ADDR = xM_APPLN_Service.findXM_APPLN_ADDRByCustomerId(customerId);
		
		//转化数据字典value为显示值
		xM_APPLN.setProduct(conventDic2Title("PRODUCT", xM_APPLN.getProduct()));
		xM_APPLN.setAddl_card((xM_APPLN.getAddl_card()!=null&&xM_APPLN.getAddl_card().equals("1"))?"是":"否");
		xM_APPLN.setRush_card((xM_APPLN.getRush_card()!=null&&xM_APPLN.getRush_card().equals("1"))?"是":"否");
		xM_APPLN.setApp_source(conventDic2Title("APP_SOURCE", xM_APPLN.getApp_source()));
		xM_APPLN.setRush_fee((xM_APPLN.getRush_fee()!=null&&xM_APPLN.getRush_fee().equals("1"))?"是":"否");
		xM_APPLN.setAcc_type(conventDic2Title("ACC_TYPE", xM_APPLN.getAcc_type()));
		xM_APPLN.setGtoc((xM_APPLN.getGtoc()!=null&&xM_APPLN.getGtoc().equals("1"))?"是":"否");
		
		for(XM_APPLN_KPMX obj : xM_APPLN_KPMX_ls){
			obj.setEmboss_cd((obj.getEmboss_cd()!=null&&obj.getEmboss_cd().equals("1"))?"是":"否");
			obj.setPin_reqd((obj.getPin_reqd()!=null&&obj.getPin_reqd().equals("1"))?"是":"否");
			obj.setSms_yn((obj.getSms_yn()!=null&&obj.getSms_yn().equals("1"))?"是":"否");
			obj.setPin_chk((obj.getPin_chk()!=null&&obj.getPin_chk().equals("1"))?"是":"否");
			obj.setCdfrm(conventDic2Title("CDFRM", obj.getCdfrm()));
			if(obj.getAtm().equals("MR"))
				obj.setAtm("取产品新卡参数");
			if(obj.getAtm().equals("BKT"))
				obj.setAtm("不开通");
			if(obj.getAtm().equals("KT"))
				obj.setAtm("开通");
			if(obj.getTele().equals("MR"))
				obj.setTele("取产品新卡参数");
			if(obj.getTele().equals("BKT"))
				obj.setTele("不开通");
			if(obj.getTele().equals("KT"))
				obj.setTele("开通");
			if(obj.getNet().equals("MR"))
				obj.setNet("取产品新卡参数");
			if(obj.getNet().equals("BKT"))
				obj.setNet("不开通");
			if(obj.getNet().equals("KT"))
				obj.setNet("开通");
			if(obj.getPhone().equals("MR"))
				obj.setPhone("取产品新卡参数");
			if(obj.getPhone().equals("BKT"))
				obj.setPhone("不开通");
			if(obj.getPhone().equals("KT"))
				obj.setPhone("开通");
		}
		
		xM_APPLN_HKSZ.setRepay_code(conventDic2Title("REPAY_CODE", xM_APPLN_HKSZ.getRepay_code()));
		xM_APPLN_HKSZ.setRepay_codx(conventDic2Title("REPAY_CODX", xM_APPLN_HKSZ.getRepay_codx()));
		xM_APPLN_HKSZ.setExch_flag(conventDic2Title("EXCH_FLAG", xM_APPLN_HKSZ.getExch_flag()));
		xM_APPLN_HKSZ.setExch_code(conventDic2Title("EXCH_CODE", xM_APPLN_HKSZ.getExch_code()));
		
		xM_APPLN_DBXX.setGuarn_code(conventDic2Title("GUARN_CODE", xM_APPLN_DBXX.getGuarn_code()));
		
		if(xM_APPLN_QTXYKXX.getXrefcode1().equals("WU")){
			xM_APPLN_QTXYKXX.setXrefcode1("无");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode1().equals("BH")){
			xM_APPLN_QTXYKXX.setXrefcode1("本行");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode1().equals("TH")){
			xM_APPLN_QTXYKXX.setXrefcode1("他行");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode2().equals("WU")){
			xM_APPLN_QTXYKXX.setXrefcode2("无");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode2().equals("BH")){
			xM_APPLN_QTXYKXX.setXrefcode2("本行");
		}
		if(xM_APPLN_QTXYKXX.getXrefcode2().equals("TH")){
			xM_APPLN_QTXYKXX.setXrefcode2("他行");
		}
		
		xM_APPLN_TJINFO.setIntr_recom(conventDic2Title("INTR_RECOM", xM_APPLN_TJINFO.getIntr_recom()));
		xM_APPLN_TJINFO.setBrnch_intr(conventDic2Title("BRNCH_INTR", xM_APPLN_TJINFO.getBrnch_intr()));
		xM_APPLN_TJINFO.setIntr_rl(conventDic2Title("INTR_RL", xM_APPLN_TJINFO.getIntr_rl()));
		xM_APPLN_TJINFO.setIntr_qc(conventDic2Title("INTR_QC", xM_APPLN_TJINFO.getIntr_qc()));
		
		xM_APPLN_ADDR.setAddr1_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr1_type()));
		xM_APPLN_ADDR.setAddr2_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr2_type()));
		xM_APPLN_ADDR.setAddr3_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr3_type()));
		xM_APPLN_ADDR.setAddr4_type(conventDic2Title("ADDR_TYPE",xM_APPLN_ADDR.getAddr4_type()));
		xM_APPLN_ADDR.setOsea_f1((xM_APPLN_ADDR.getOsea_f1()!=null&&xM_APPLN_ADDR.getOsea_f1().equals("1"))?"是":"否");
		xM_APPLN_ADDR.setOsea_f2((xM_APPLN_ADDR.getOsea_f2()!=null&&xM_APPLN_ADDR.getOsea_f2().equals("1"))?"是":"否");
		xM_APPLN_ADDR.setOsea_f3((xM_APPLN_ADDR.getOsea_f3()!=null&&xM_APPLN_ADDR.getOsea_f3().equals("1"))?"是":"否");
		xM_APPLN_ADDR.setOsea_f4((xM_APPLN_ADDR.getOsea_f4()!=null&&xM_APPLN_ADDR.getOsea_f4().equals("1"))?"是":"否");
		
		xM_APPLN.setMail_to(conventDic2Title("MAIL_TO", xM_APPLN.getMail_to()));
		
		JRadModelAndView mv = new JRadModelAndView("/xm_appln/xm_appln_page4", request);
		
		mv.addObject("customerId", customerId);
		mv.addObject("customer", customerInfor);
		mv.addObject("appId", appId);
		mv.addObject("xM_APPLN", xM_APPLN);
		mv.addObject("xM_APPLN_SQED", xM_APPLN_SQED);
		mv.addObject("xM_APPLN_KPMX_ls", xM_APPLN_KPMX_ls);
		mv.addObject("xM_APPLN_HKSZ", xM_APPLN_HKSZ);
		mv.addObject("xM_APPLN_DBXX", xM_APPLN_DBXX);
		mv.addObject("xM_APPLN_QTXYKXX", xM_APPLN_QTXYKXX);
		mv.addObject("xM_APPLN_DCSC", xM_APPLN_DCSC);
		mv.addObject("xM_APPLN_TJINFO", xM_APPLN_TJINFO);
		mv.addObject("xM_APPLN_ADDR", xM_APPLN_ADDR);
		
		return mv;
	}
		
	//保存page1
	@ResponseBody
	@RequestMapping(value = "update_xm_appln_page1.json")
	@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap update_xm_appln_page1(@ModelAttribute XM_APPLN_JBZL_FORM xM_APPLN_JBZL_FORM, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				String customerId = request.getParameter("customer_id");
				customerId = xM_APPLN_Service.insertXM_APPLN_JBZL(customerId,xM_APPLN_JBZL_FORM,user);
				returnMap.put(RECORD_ID, customerId);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//保存page2
	@ResponseBody
	@RequestMapping(value = "update_xm_appln_page2.json")
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap update_xm_appln_page2(@ModelAttribute XM_APPLN_FORM xM_APPLN_FORM, HttpServletRequest request) {

		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				xM_APPLN_Service.insertXM_APPLN(xM_APPLN_FORM,user);
				returnMap.put("customerId",xM_APPLN_FORM.getCustomer_id());
				returnMap.addGlobalMessage(CHANGE_SUCCESS);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}

		return returnMap;
	}
	
	//保存page3
	@ResponseBody
	@RequestMapping(value = "update_xm_appln_page3.json")
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap update_xm_appln_page3(@ModelAttribute XM_APPLN_ADDR_FORM xM_APPLN_ADDR_FORM, HttpServletRequest request) {
		
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				xM_APPLN_Service.insertXM_APPLN_ADDR(xM_APPLN_ADDR_FORM, user);
				returnMap.put("customerId",xM_APPLN_ADDR_FORM.getCustomer_id());
				returnMap.addGlobalMessage(CHANGE_SUCCESS);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}

		return returnMap;
	}
	
	//保存page4
	@ResponseBody
	@RequestMapping(value = "update_xm_appln_page4.json", method = {RequestMethod.GET })
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap update_xm_appln_page4(HttpServletRequest request)throws Exception {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String appId = request.getParameter("appId");
				
				//设置审批金额
				CustomerApplicationInfo customerApplicationInfo = customerApplicationInfoService.findCustomerApplicationrById(appId);
				String customer_id = customerApplicationInfo.getCustomerId();
				String sqed = this.xM_APPLN_Service.findXM_APPLN_SQEDByCustomerId(customer_id).getCrdlmt_req();
				customerApplicationInfo.setApplyQuota(Integer.valueOf(sqed)*100+"");
				customerApplicationInfoService.update(customerApplicationInfo);
				
				CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(appId);
				request.setAttribute("serialNumber", process.getSerialNumber());
				request.setAttribute("applicationId", process.getApplicationId());
				request.setAttribute("applicationStatus", ApplicationStatusEnum.APPROVE);
				request.setAttribute("objection", "false");
				request.setAttribute("examineAmount", "");
				customerApplicationIntopieceWaitService.updateCustomerApplicationProcessBySerialNumberApplicationInfo1(request);
				
				
				
				
				returnMap.addGlobalMessage(CHANGE_SUCCESS);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
	//保存page5
	@ResponseBody
	@RequestMapping(value = "update_xm_appln_page5.json", method = {RequestMethod.GET })
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap update_xm_appln_page5(HttpServletRequest request)throws Exception {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String appId = request.getParameter("appId");
				
				//设置审批金额
				CustomerApplicationInfo customerApplicationInfo = customerApplicationInfoService.findCustomerApplicationrById(appId);
				String customer_id = customerApplicationInfo.getCustomerId();
				String sqed = this.xM_APPLN_Service.findXM_APPLN_SQEDByCustomerId(customer_id).getCrdlmt_req();
				customerApplicationInfo.setApplyQuota(Integer.valueOf(sqed)*100+"");
				customerApplicationInfoService.update(customerApplicationInfo);
				
				CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(appId);
				request.setAttribute("serialNumber", process.getSerialNumber());
				request.setAttribute("applicationId", process.getApplicationId());
				request.setAttribute("applicationStatus", ApplicationStatusEnum.APPROVE);
				request.setAttribute("objection", "false");
				request.setAttribute("examineAmount", "");
				customerApplicationIntopieceWaitService.updateCustomerApplicationProcessBySerialNumberApplicationInfo1(request);
				
				returnMap.addGlobalMessage(CHANGE_SUCCESS);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
	//发起审批流程
	private void startProcess(String customer_id){
		//设置申请
		CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
		//customerApplicationInfo.setStatus(status);
		customerApplicationInfo.setId(IDGenerator.generateID());
		XM_APPLN_SQED xM_APPLN_SQED = xM_APPLN_Service.findXM_APPLN_SQEDByCustomerId(customer_id);
		if(xM_APPLN_SQED==null||xM_APPLN_SQED.getCrdlmt_req()==null||xM_APPLN_SQED.getCrdlmt_req().equals("")){
			customerApplicationInfo.setApplyQuota("0");//设置额度
		}
		customerApplicationInfo.setCustomerId(customer_id);
		customerApplicationInfo.setApplyQuota((Integer.valueOf(customerApplicationInfo.getApplyQuota())*100)+"");
		customerApplicationInfo.setStatus(Constant.APPROVE_INTOPICES);
		//查找默认产品
		ProductFilter filter = new ProductFilter();
		filter.setDefault_type(Constant.DEFAULT_TYPE);
		ProductAttribute productAttribute = productService.findProductsByFilter(filter).getItems().get(0);
		customerApplicationInfo.setProductId(productAttribute.getId());
				
		commonDao.insertObject(customerApplicationInfo);
		
		
		//添加申请件流程
		WfProcessInfo wf=new WfProcessInfo();
		wf.setProcessType(WfProcessInfoType.process_type);
		wf.setVersion("1");
		commonDao.insertObject(wf);
		List<NodeAudit> list=nodeAuditService.findByNodeTypeAndProductId(NodeAuditTypeEnum.Product.name(),productAttribute.getId());
		boolean startBool=false;
		boolean endBool=false;
		//节点id和WfStatusInfo id的映射
		Map<String, String> nodeWfStatusMap = new HashMap<String, String>();
		for(NodeAudit nodeAudit:list){
			if(nodeAudit.getIsstart().equals(YesNoEnum.YES.name())){
				startBool=true;
			}
			
			if(startBool&&!endBool){
				WfStatusInfo wfStatusInfo=new WfStatusInfo();
				wfStatusInfo.setIsStart(nodeAudit.getIsstart().equals(YesNoEnum.YES.name())?"1":"0");
				wfStatusInfo.setIsClosed(nodeAudit.getIsend().equals(YesNoEnum.YES.name())?"1":"0");
				wfStatusInfo.setRelationedProcess(wf.getId());
				wfStatusInfo.setStatusName(nodeAudit.getNodeName());
				wfStatusInfo.setStatusCode(nodeAudit.getId());
				commonDao.insertObject(wfStatusInfo);
				
				nodeWfStatusMap.put(nodeAudit.getId(), wfStatusInfo.getId());
				
				if(nodeAudit.getIsstart().equals(YesNoEnum.YES.name())){
					//添加初始审核
					CustomerApplicationProcess customerApplicationProcess=new CustomerApplicationProcess();
					String serialNumber = processService.start(wf.getId());
					customerApplicationProcess.setSerialNumber(serialNumber);
					customerApplicationProcess.setNextNodeId(nodeAudit.getId()); 
					customerApplicationProcess.setApplicationId(customerApplicationInfo.getId());
					commonDao.insertObject(customerApplicationProcess);
					
					CustomerApplicationInfo applicationInfo = commonDao.findObjectById(CustomerApplicationInfo.class, customerApplicationInfo.getId());
					applicationInfo.setSerialNumber(serialNumber);
					commonDao.updateObject(applicationInfo);
				}
			}
			
			if(nodeAudit.getIsend().equals(YesNoEnum.YES.name())){
				endBool=true;
			}
		}
		//节点关系
		List<NodeControl> nodeControls = nodeAuditService.findNodeControlByNodeTypeAndProductId(NodeAuditTypeEnum.Product.name(), productAttribute.getId());
		for(NodeControl control : nodeControls){
			WfStatusResult wfStatusResult = new WfStatusResult();
			wfStatusResult.setCurrentStatus(nodeWfStatusMap.get(control.getCurrentNode()));
			wfStatusResult.setNextStatus(nodeWfStatusMap.get(control.getNextNode()));
			wfStatusResult.setExamineResult(control.getCurrentStatus());
			commonDao.insertObject(wfStatusResult);
		}
	}
	
	//转化数据字典的vlaue值为显示值
	private String conventDic2Title(String dicName,String value){
		DictionaryManager dictMgr = Beans.get(DictionaryManager.class);
		Dictionary dictionary = dictMgr.getDictionaryByName(dicName);
		List<DictionaryItem> ls = dictionary.getItems();
		for(DictionaryItem obj : ls){
			if(obj.getName().equals(value)){
				return obj.getTitle();
			}
		}
		return "";
	}
}
