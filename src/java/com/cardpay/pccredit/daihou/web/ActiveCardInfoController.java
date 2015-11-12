package com.cardpay.pccredit.daihou.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.daihou.model.ActiveCard;
import com.cardpay.pccredit.daihou.service.ActiveCardService;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.riskControl.filter.AgrCrdXykCunegFilter;
import com.cardpay.pccredit.riskControl.model.AgrCrdXykCuneg;
import com.cardpay.pccredit.riskControl.model.RiskCustomerCollectionPlan;
import com.cardpay.pccredit.riskControl.service.AgrCrdXykCunegService;
import com.cardpay.pccredit.riskControl.service.RiskCustomerCollectionService;
import com.cardpay.pccredit.riskControl.web.RiskCustomerCollectionPlanForm;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.i18n.I18nHelper;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * 激活卡资料
 * @author 谭文华
 *
 * 2014-12-22下午4:22:04
 */
@Controller
@RequestMapping("/daihou/activecardinfo/*")
@JRadModule("daihou.activecardinfo")
public class ActiveCardInfoController extends BaseController {
	
	@Autowired
	private ActiveCardService activeCardService;
	
	@Autowired
	private RiskCustomerCollectionService riskCustomerCollectionService;
	
	/**
	 * 浏览页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute CustomerInforFilter filter,HttpServletRequest request) {
        filter.setRequest(request);
        IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		filter.setUserId(user.getId());
		filter.setUserType(user.getUserType());
		
		QueryResult<ActiveCard> result = activeCardService.findActiveCard(filter);
		JRadPagedQueryResult<ActiveCard> pagedResult = new JRadPagedQueryResult<ActiveCard>(filter, result);
		JRadModelAndView mv = new JRadModelAndView("/riskcontrol/activecard/activecard_browse",request);
		mv.addObject(PAGED_RESULT, pagedResult);
		mv.addObject("filter", filter);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "create_collection.json", method = { RequestMethod.GET })
	//@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap create_collection(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customer_id = request.getParameter("customer_id");
				IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
				String collectionPlanId = "";
				//查找催收计划 无则添加一条
				List<RiskCustomerCollectionPlanForm> collectionPlan_ls = riskCustomerCollectionService.findRiskCollectionPlansByCustomerId(customer_id);
				if(collectionPlan_ls == null || collectionPlan_ls.size() == 0){
					RiskCustomerCollectionPlan riskCustomerCollectionPlan = new RiskCustomerCollectionPlan();
					riskCustomerCollectionPlan.setCustomerId(customer_id);
					riskCustomerCollectionPlan.setCreatedBy(user.getId());
					collectionPlanId = riskCustomerCollectionService.insertRiskCustomerCollectionPlan(riskCustomerCollectionPlan);
				}
				else{
					collectionPlanId = collectionPlan_ls.get(0).getId();
				}
				returnMap.put("collectionPlanId",collectionPlanId);
				returnMap.addGlobalMessage(CHANGE_SUCCESS);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}

		return returnMap;
	}
}
