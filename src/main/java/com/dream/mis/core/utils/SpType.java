package com.dream.mis.core.utils;


import com.jfinal.kit.StrKit;

public class SpType {
	/**
	 * 返回运营商类型
	 * @param PhoneNumber 电话号码
	 * @return SPTYPE 运营商类型 ：0移动 1 电信 2 移动 -1非三网号码
	 */
	static public int getSPTYPE(String PhoneNumber) throws Exception{
		if(StrKit.isBlank(PhoneNumber)||PhoneNumber.length()!=11){
			throw new Exception(PhoneNumber + "手机号码异常！检查手机号码的是否正确！");
		}
		//移动手机0 /电信手机1 联通手机2 异常号码-1
		String mobile = PropertyUtils.getPropertyValue("mobile");//移动手机0
		String telecom = PropertyUtils.getPropertyValue("telecom");//电信手机1
		String unicom = PropertyUtils.getPropertyValue("unicom");//联通手机2
		String substrPhone  = PhoneNumber.substring(0, 3);
		int SPTYPE = 0;
		if(mobile.indexOf(substrPhone)>0){
			SPTYPE = 0;
		}else if(telecom.indexOf(substrPhone)>0){
			SPTYPE = 1;
		}else if(unicom.indexOf(substrPhone)>0){
			SPTYPE = 2;
		}else{
			SPTYPE = -1;
		}
		return SPTYPE;
	}
}
