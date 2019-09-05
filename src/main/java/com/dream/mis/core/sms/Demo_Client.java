package com.dream.mis.core.sms;

import java.io.IOException;

public class Demo_Client {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException
	{

//		String sn = "SDK-WSS-010-10649";
//		String pwd = "2c67738@d51";
//		SmsSendClient client=new SmsSendClient(sn,pwd);
//
//        String  content = java.net.URLEncoder.encode("【昆明市政府信息公开平台】您本次的验证码是：123321。该验证码五分钟内有效，请不要把验证码泄露给其他人。",  "utf-8");  
//		try {
//			String result_mt = client.mdsmssend("13908866200", content, "", "", "", "");
//			System.out.print(result_mt);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println(">>>>>>>>>>>>>短信发送接口异常："+e.getMessage());
//		}		
		String contents= java.net.URLEncoder.encode("【昆明市政府办公厅】 政务网站监测服务云平台系统提示：您机构在昆明市交通运输局网站党务公开栏目下添加的文章[盘龙区交运局组织干部职工参观“昆明市经济社会发展回顾与展望主题展”专题展览]有2个错链/坏链，请及时处理。");
						//+"【昆明市政府办公厅】 政务网站监测服务云平台系统提示：您机构在昆明市交通运输局网站党务公开栏目下添加的文章[盘龙区交运局组织干部职工参观“昆明市经济社会发展回顾与展望主题展”专题展览]有2个错链/坏链，请及时处理。";
						//+"【昆明市政府办公厅】 政务网站监测服务云平台系统提示：您机构在昆明市交通运输局网站党务公开栏目下添加的文章[盘龙区交运局组织干部职工参观“昆明市经济社会发展回顾与展望主题展”专题展览]有2个错链/坏链，请及时处理。,"
						//+"【昆明市政府办公厅】 政务网站监测服务云平台系统提示：您机构在昆明市交通运输局网站党务公开栏目下添加的文章[盘龙区交运局组织干部职工参观“昆明市经济社会发展回顾与展望主题展”专题展览]有2个错链/坏链，请及时处理。,"
						//+"【昆明市政府办公厅】 政务网站监测服务云平台系统提示：您机构在昆明市交通运输局网站党务公开栏目下添加的文章[盘龙区交运局组织干部职工参观“昆明市经济社会发展回顾与展望主题展”专题展览]有2个错链/坏链，请及时处理。";
		SmsSendClient sms = new SmsSendClient();
		String result_msg = sms.mddxsend("15887253628", contents, "", "", "", "");
		//18183702109
		//System.out.println(result_msg);
	}
}
