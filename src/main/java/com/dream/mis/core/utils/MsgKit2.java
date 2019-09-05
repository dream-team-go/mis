package com.dream.mis.core.utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.jfinal.kit.HttpKit;

/**
 * Author : ly <br>
 * qq:993046532 <br>
 * 2015-11-1 <br>
 * 
 * @see 天下畅通短信kit
 */
public class MsgKit2 {

	/**
	 * 发送短信
	 * @param content  短信内容
	 * @param mobile 要发送的手机号码,用逗号(,)隔开,如18213887580,15388716054
	 * @param sendTime 发送时间，为空表示马上发送
	 * @return map 类似：{"returnstatus":"Success","successCounts":"1","message":"ok","remainpoint":"10986","taskID":"33622745"}
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> send(String content,String mobile,String sendTime) throws DocumentException {
		Map<String,String> map = new HashMap<String,String>();
		map.put("userid", "6980");				//企业id
		map.put("account", "兰亭科技");			//发送用户帐号
		map.put("password", "84304600");		//发送帐号密码
		map.put("mobile", mobile);				//全部被叫号码
		map.put("content", content);			//发送内容
		map.put("sendTime", sendTime);			//定时发送时间
		map.put("action", "send");				//发送任务命令
		map.put("extno", "");					//扩展子号
		return xmltoMap(HttpKit.post("http://sms.chengweigg.com/sms.aspx", map, ""));
	}

	@SuppressWarnings("rawtypes")
	public static Map xmltoMap(String xml) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			Document document = DocumentHelper.parseText(xml);
			Element nodeElement = document.getRootElement();
			List node = nodeElement.elements();
			for (Iterator it = node.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				map.put(elm.getName(), elm.getText());
				elm = null;
			}
			node = null;
			nodeElement = null;
			document = null;
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			 Map<String, String> ret = MsgKit2.send("您的验证码为：1234【新意境】", "15388716054", "");
			 System.out.println(ret.get("returnstatus"));
			 System.out.println(ret.get("message"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("action", "send");
//		map.put("userid", "3176");
//		map.put("account", "k000134");
//		map.put("password", "jdcs65311071");
//		map.put("mobile", "18213887580");//要发送的手机号码,用逗号(,)隔开,如18213887580,15388716054
//		map.put("content", "您的验证码为：9999" + "【新意境】");//发送短信的内容
//		map.put("sendTime", "");//发送时间，为空表示马上发送
//		map.put("checkcontent", "0");//是否检查特殊字符
//		map.put("mobilenumber", "1");//手机号码数量
//		map.put("countnumber", "1");//发送的手机号码数量
//		map.put("telephonenumber", "0");
//		String s = HttpKit.post("http://xtx.telhk.cn:8888/sms.aspx", map, "");
//		System.out.println(s);
	}

}
