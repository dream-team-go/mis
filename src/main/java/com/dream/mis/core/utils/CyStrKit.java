package com.dream.mis.core.utils;


import java.util.regex.Pattern;

import com.jfinal.kit.StrKit;

/**
 * @author liyun
 */
public class CyStrKit {

	
	public static boolean isNumeric(String str){ 
	     Pattern pattern = Pattern.compile("[0-9]*"); 
	     return pattern.matcher(str).matches();    
	} 
	
	/**
	 * 条形码生存校验位
	 * @return
	 */
	public static int generateBarcodeCheckBit(String bar_code){
		// 6 9 2 0 1 5 0 5 0 0 0 1 1
		//N1 N2 N3 N4 N5 N6 N7 N8 N9 N10 N11 N12 C
		//C1 = N1+ N3+N5+N7+N9+N11 6 2 1 0 0 0 =9
		//C2 = (N2+N4+N6+N8+N10+N12)× 3 = 9 0 5 5 0 1 = 60
		//CC = (C1+C2) 69
		//C (检查码) = 10 - CC　(若值为10，则取0) 1
		
		//8820150510059
		bar_code = bar_code + "x";
		
		//String baseNum[] = bar_code.split("");
		int c1 = Integer.parseInt(bar_code.charAt(0)+"") + Integer.parseInt(bar_code.charAt(2)+"") + Integer.parseInt(bar_code.charAt(4)+"") + Integer.parseInt(bar_code.charAt(6)+"")
				+ Integer.parseInt(bar_code.charAt(8)+"") + Integer.parseInt(bar_code.charAt(10)+"");
		System.out.println(c1);
		int c2 =  (Integer.parseInt(bar_code.charAt(1)+"") + Integer.parseInt(bar_code.charAt(3)+"") + Integer.parseInt(bar_code.charAt(5)+"") + Integer.parseInt(bar_code.charAt(7)+"")
				+ Integer.parseInt(bar_code.charAt(9)+"") + Integer.parseInt(bar_code.charAt(11)+"") )*3;
		int cc = c1+c2;
		System.out.println(cc);
		if(cc%10==0){
			return 0;
		} else {
			return 10-cc%10;
		}
	}
	
	
	/**
	 * @return
	 */
	public static int getSexByIdCard(String id_card){
		if(StrKit.notBlank(id_card) && (id_card.length()==15 || id_card.length()==18)){
			if (id_card.length() == 15) {
				if (Integer.parseInt(id_card.substring(14, 15)) % 2 == 0) {
					return 0;
				} else {
					return 1;
				}
			} else if (id_card.length() == 18) {
				if (Integer.parseInt(id_card.substring(16, 17)) % 2 == 0) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
	
	
	/**
	 * 从身份证获取出生年月日
	 * @param id_card
	 * @return
	 */
	public static String getBirthdayByIdCard(String id_card){
		if(StrKit.notBlank(id_card) && (id_card.length()==15 || id_card.length()==18)){
			if (id_card.length() == 15) {
				String year = id_card.substring(6, 8);
				String month = id_card.substring(8, 10);
				String day = id_card.substring(10, 12);
				return year + "-" + month + "-" + day;
			} else if (id_card.length() == 18) {
				String year = id_card.substring(6, 10);
				String month = id_card.substring(10, 12);
				String day = id_card.substring(12, 14);
				return year + "-" + month + "-" + day;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
	
}
