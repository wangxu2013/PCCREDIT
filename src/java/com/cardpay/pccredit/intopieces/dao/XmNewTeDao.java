package com.cardpay.pccredit.intopieces.dao;

import java.util.List;
import java.util.Map;

import com.cardpay.pccredit.intopieces.filter.ModelParamFilter;
import com.cardpay.pccredit.intopieces.filter.XmNewTeFilter;
import com.cardpay.pccredit.intopieces.model.ModelParamConfigure;
import com.cardpay.pccredit.intopieces.model.XmNewTe;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface XmNewTeDao {
	public List<XmNewTe> findAllTeByFilter(XmNewTeFilter filter);
	public int findAllTeCountByFilter(XmNewTeFilter filter);
	public List<ModelParamConfigure> findModelParamFilter(ModelParamFilter filter);
	public int  findModelParamCountFilter(ModelParamFilter filter);
	
	
	//台帐bf
	public int insertTzbfCur(Map<String, Object> map);
	//交易bf
	public int insertJybfCur(Map<String, Object> map);
	
	//台帐ql
    public int insertTzqlCur(Map<String, Object> map);
}
