package com.dream.mis.core.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
//	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
//			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 * 
	 * @param bytes
	 *            the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
//	private static String getFormattedText(byte[] bytes) {
//		int len = bytes.length;
//		StringBuilder buf = new StringBuilder(len * 2);
//		// 把密文转换成十六进制的字符串形式
//		for (int j = 0; j < len; j++) {
//			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
//			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
//		}
//		return buf.toString();
//	}

	public static String encode(String str) {
		 try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
	}
	public static void main(String[] args) {
		String r = SHA1.encode("jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VNXdKYNtXlxe7df56JwIJ0Do9EVclfCv8PJA17ITAFpjORj759NYFtS45dr4ojvdWQ&noncestr=22addcd89a4c419b9ea0ccf89a7deb05&timestamp=1438012121&url=http://chuyun.ddns.net/ppx/weixin/user");
		System.out.println(r);
		
		System.out.println(Runtime.getRuntime().maxMemory());
	}
}
