package com.dream.mis.core.sms;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信发送接口
 * @author suj
 * @version created at 2017年5月24日 下午2:50:52
 */
public class SmsSendClient {
	private static final String serviceURL = "http://sdk.entinfo.cn:8061/webservice.asmx";
	private static final String sn = "SDK-WSS-010-10649";
	private static final String password = "2c67738@d51";
	private String pwd = "";
	
	public SmsSendClient() throws UnsupportedEncodingException{
		this.pwd = getMD5(sn + password);
	}

	/**
	 * 密码 = md5(sn+password)
	 * @author suj
	 * @version created at 2017年5月24日 下午2:48:00
	 * @return
	 * @throws IOException
	 */
	public String getMD5(String sourceStr) throws UnsupportedEncodingException {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}


	public String mdgetSninfo() throws IOException {
		String result = "";
		String soapAction = "http://entinfo.cn/mdgetSninfo";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mdgetSninfo xmlns=\"http://entinfo.cn/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwd + "</pwd>";
		xml += "<mobile>" + "" + "</mobile>";
		xml += "<content>" + "" + "</content>";
		xml += "<ext>" + "" + "</ext>";
		xml += "<stime>" + "" + "</stime>";
		xml += "<rrid>" + "" + "</rrid>";
		xml += "<msgfmt>" + "" + "</msgfmt>";
		xml += "</mdgetSninfo>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		URL url;
		ByteArrayOutputStream bout = null;
		OutputStream out = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String
					.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			out = httpconn.getOutputStream();
			out.write(b);

			isr = new InputStreamReader(httpconn
					.getInputStream());
			in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<mdgetSninfoResult>(.*)</mdgetSninfoResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}finally {
			if (bout!=null) {
				bout.close();
			}
			if(out!=null){
				out.close();
			}
			if(isr!=null){
				isr.close();
			}
			if(in!=null){
				in.close();
			}
		}
	}

	
	/**
	 * http调用webservice方式的SDK发送个性短信
	 * @author suj
	 * @version created at 2017年5月24日 下午2:49:16
	 * @param mobile
	 * @param content
	 * @param ext
	 * @param stime
	 * @param rrid
	 * @param msgfmt
	 * @return
	 * @throws IOException
	 */
	public String mdgxsend(String mobile, String content, String ext, String stime,String rrid, String msgfmt) throws IOException {
		String result = "";
		String soapAction = "http://entinfo.cn/mdgxsend";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mdgxsend xmlns=\"http://entinfo.cn/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwd + "</pwd>";
		xml += "<mobile>" + mobile + "</mobile>";
		xml += "<content>" + content + "</content>";
		xml += "<ext>" + ext + "</ext>";
		xml += "<stime>" + stime + "</stime>";
		xml += "<rrid>" + rrid + "</rrid>";
		xml += "<msgfmt>" + msgfmt + "</msgfmt>";
		xml += "</mdgxsend>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		ByteArrayOutputStream bout = null;
		OutputStream out = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String
					.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			isr = new InputStreamReader(httpconn
					.getInputStream());
			in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<mdgxsendResult>(.*)</mdgxsendResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}finally {
			if (bout!=null) {
				bout.close();
			}
			if(out!=null){
				out.close();
			}
			if(isr!=null){
				isr.close();
			}
			if(in!=null){
				in.close();
			}
		}
	}
	
	/**
	 * http调用webservice方式的SDK 发送短信 
	 * 【昆明市纪委】时ext传1
	 * 【昆明市政府办公厅】时其它参数不用传
	 * @author suj
	 * @version created at 2017年5月24日 下午2:49:54
	 * @param mobile
	 * @param content
	 * @param ext
	 * @param stime
	 * @param rrid
	 * @param msgfmt
	 * @return
	 * @throws IOException
	 */
	public String mdsmssend(String mobile, String captcha_code, String ext, String stime,	String rrid,String msgfmt) throws IOException {
		String  content = java.net.URLEncoder.encode("【昆明市纪委】您本次的验证码是："+captcha_code+"。该验证码五分钟内有效，请勿将验证码告知他人。",  "utf-8");  
		String result = "";
		String soapAction = "http://entinfo.cn/mdsmssend";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mdsmssend  xmlns=\"http://entinfo.cn/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwd + "</pwd>";
		xml += "<mobile>" + mobile + "</mobile>";
		xml += "<content>" + content + "</content>";
		xml += "<ext>" + ext + "</ext>";
		xml += "<stime>" + stime + "</stime>";
		xml += "<rrid>" + rrid + "</rrid>";
		xml += "<msgfmt>" + msgfmt + "</msgfmt>";
		xml += "</mdsmssend>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		ByteArrayOutputStream bout = null;
		OutputStream out = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String
					.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			isr = new InputStreamReader(httpconn
					.getInputStream());
			in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<mdsmssendResult>(.*)</mdsmssendResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}finally {
			if (bout!=null) {
				bout.close();
			}
			if(out!=null){
				out.close();
			}
			if(isr!=null){
				isr.close();
			}
			if(in!=null){
				in.close();
			}
		}
	}

	/**
	 * http调用webservice方式的SDK 发送短信 
	 * 【昆明市纪委】时ext传1
	 * 【昆明市政府办公厅】时其它参数不用传
	 * @author suj
	 * @version created at 2017年5月24日 下午2:49:54
	 * @param mobile
	 * @param content
	 * @param ext
	 * @param stime
	 * @param rrid
	 * @param msgfmt
	 * @return
	 * @throws IOException
	 */
	public String mddxsend(String mobile, String content, String ext, String stime,	String rrid,String msgfmt) throws IOException {
		String result = "";
		String soapAction = "http://entinfo.cn/mdsmssend";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mdsmssend  xmlns=\"http://entinfo.cn/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwd + "</pwd>";
		xml += "<mobile>" + mobile + "</mobile>";
		xml += "<content>" + content + "</content>";
		xml += "<ext>" + ext + "</ext>";
		xml += "<stime>" + stime + "</stime>";
		xml += "<rrid>" + rrid + "</rrid>";
		xml += "<msgfmt>" + msgfmt + "</msgfmt>";
		xml += "</mdsmssend>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		ByteArrayOutputStream bout = null;
		OutputStream out = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String
					.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			isr = new InputStreamReader(httpconn
					.getInputStream());
			in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<mdsmssendResult>(.*)</mdsmssendResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}finally {
			if (bout!=null) {
				bout.close();
			}
			if(out!=null){
				out.close();
			}
			if(isr!=null){
				isr.close();
			}
			if(in!=null){
				in.close();
			}
		}
	}

}
