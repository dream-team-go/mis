package com.dream.mis.core.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.dream.mis.core.utils.CommonUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@TableBind(tableName="td_user",pkName="user_id")
public class User extends Model<User>{
	private static final long serialVersionUID = 8574125570715711373L;
	public static final User dao = new User();
	private static String msg = "";
	private static String status = "y";
	
	
	public User findByUsernameExceptDel(String username) {
		return User.dao.findFirst("select * from td_user where is_del=0 and username = ?", username);
	}
	public User findByUserIncludeDel(String username) {
		return User.dao.findFirst("select * from td_user where is_del=0 and username = ?", username);
	}
	
	public List<Role> getRoles(String user_role_id) {
		return Role.dao.find("select * from td_role where role_id = ?", user_role_id);
	}
	
	public Page<Record> page(int pageNumber,int pageSize,String searchKey){
		String condition = " from v_user a where 1=1";
		List<Object> params = new ArrayList<Object>();
		if(StrKit.notBlank(searchKey)){
			condition += " and a.user_cn_name like ?";
			condition += " or a.tel_no like ?";
			params.add("%"+searchKey+"%");
			params.add("%"+searchKey+"%");
		}
		condition += " and a.is_del=0 and a.username is not null";
		Page<Record> page = Db.paginate(pageNumber, pageSize, "select *", condition+" order by create_time desc",params.toArray());
		return page;
	}
	public Page<Record> page(int pageNumber,int pageSize,Map<String, String[]> dataMap){
		String exceptSelect = " from v_user  where 1=1 ";
		List<Object> params = new ArrayList<>();
		if(StrKit.notBlank(dataMap.get("search[value]"))){
			exceptSelect += " and (username LIKE ? or tel_no LIKE ? or user_cn_name LIKE ? or role_name like ?)";
			params.add("%"+dataMap.get("search[value]")[0] + "%");
			params.add("%"+dataMap.get("search[value]")[0] + "%");
			params.add("%"+dataMap.get("search[value]")[0] + "%");
			params.add(dataMap.get("search[value]")[0] + "%");
		}
		exceptSelect += " and is_del=0 and username is not null ";
		if(StrKit.notBlank(dataMap.get("order[0][column]"))){
			exceptSelect += " order by " + getOrderByColumn(dataMap) + " " + getDesc_Asc(dataMap);
		}
		Page<Record> page = Db.paginate(pageNumber, pageSize, "select * ", exceptSelect,params.toArray());
		return page;
	}
	
	/**
	 * 得到当前排序的字段
	 * @param dataMap
	 * @return
	 */
	public Object getOrderByColumn(Map<String, String[]> dataMap){
		return dataMap.get("columns["+dataMap.get("order[0][column]")[0]+"][data]")[0];
	}
	
	/**
	 * 得到当前字段的排序方式
	 * @param dataMap
	 * @return desc asc
	 */
	public Object getDesc_Asc(Map<String, String[]> dataMap){
		return dataMap.get("order[0][dir]")[0];
	}
	
	public Record saveUser(User user,String defaultPassword){
		if(StrKit.notBlank(user.getStr("user_id"))) { //update 
			user.update();
			status = "y";
			msg = "更新成功！";
		} else { // new save
			user.set("user_id", CommonUtils.getUUID());
			//PasswordService svc = new DefaultPasswordService();  
			//String encrypted = svc.encryptPassword(defaultPassword);
			String hashAlgorithmName = "MD5";//加密方式  
	        Object crdentials =defaultPassword;//密码原值  
	        ByteSource salt = ByteSource.Util.bytes(user.getStr("username"));//以账号作为盐值  
	        int hashIterations = 2;//加密2次  
	        SimpleHash hash = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
			user.set("password", hash.toString());
			user.save();
			status = "y";
			msg = "保存成功！";
		}
		return new Record().set("status", status).set("msg", msg); 
	}
	
	
	/**
	 * 根据openid 查找用户
	 * @param openid
	 * @return
	 */
	public User findbyOpenid(String openid) {
		return User.dao.findFirst("select * from td_user where openid =?",openid);
	}
	/**
	 * 根据tel_no 查找用户
	 * @param openid
	 * @return
	 */
	public User findByTelNo(String tel_no) {
		return User.dao.findFirst("select * from td_user where tel_no =?",tel_no);
	}
	
	/**
	 * 检查用户是否存在，并返回相关json信息
	 * @return json
	 */
	public Record checkUsernameStatus(String username){
		User user = User.dao.findByUsernameExceptDel(username);
		if(user!=null) {
			if(user.getInt("is_locked")==0){
				return new Record().set("ok", "用户存在");
			} else if(user.getInt("is_locked")==1){
				return new Record().set("error", "用户被锁定");
			} else {
				return new Record().set("error", "用户异常");
			}
		} else {
			return new Record().set("error", "用户不存在");
		}
	}
	/**
	 * 检查用户是否存在，并返回相关json信息
	 * @return json
	 */
	public Record checkUsernameStatus2(String username){
		User user = User.dao.findByUsernameExceptDel(username);
		if(user!=null) {
			return new Record().set("error", "该用户名已存在，请输入其他用户名");
		} else {
			return new Record();//验证通过
		}
	}
	/**
	 * 检查用户是否存在，并返回相关json信息
	 * @return json
	 */
	public Record checkUsernameStatus3(String username,String old){
		User user = User.dao.findByUsernameExceptDel(username);
		if(username.equals(old)){
			return new Record();//验证通过
		}else{
			if(user!=null) {
				return new Record().set("error", "该用户名已存在，请输入其他用户名！");
			} else {
				return new Record();//验证通过
			}
		}
	}
	/**
	 * 修改密码
	 */
	public boolean updatePwd(String user_id,String pwd){
		User user = this.findById(user_id);
		String hashAlgorithmName = "MD5";//加密方式  
        Object crdentials =pwd;//密码原值  
        ByteSource salt = ByteSource.Util.bytes(user.getStr("username"));//以账号作为盐值  
        int hashIterations = 2;//加密2次  
        SimpleHash hash = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
		user.set("password", hash.toString());
		return user.update();
	}
	
	/**
	 * 查询用户的v_user视图数据
	 * @param user_id
	 * @return
	 */
	public Record findVUser(String user_id){
		return Db.findFirst("select * from v_user where user_id=?",user_id);
	}
	/**
	 * 查询具有某个权限的用户
	 */
	public List<User> findUsersByResource(String resource_name){
		return User.dao.find("select * from td_user where user_role_id in(select role_id from tr_role_function where function_id in(select function_id from td_function where resource_name = ?))",resource_name);
	}
	
	/**
	 * 更新头像
	 * @param user_id
	 * @param head_img
	 */
	public void updateHeadImg(String user_id,String head_img){
		this.findById(user_id).set("head_img", head_img).update();
	}
	/**
	 * 根据拼音检索进行搜索
	 * @param py
	 * @return
	 */
	public boolean findByPy(String py) {
		User user = User.dao.findFirst("select * from td_user where unit_name_py = ?",py);
		if(user==null){
			return true;
		}else{
			return false;
		}
	}
	
	public List<Record> findUserByRoleId(String role_id){
		return Db.find("SELECT user_id from v_user where user_role_id = ?", role_id);
	}
	
	public List<Record> findUser(String role_id){
		return Db.find("select user_id, org_id, user_cn_name from td_user where  user_role_id = ?", role_id);
	}
	/**
	 * 查看用户个人资料
	 * @return
	 */
	public Record getMyInfo(String id) {
		String sql="select user_id,username,user_cn_name,tel_no,sex,role_name from v_user " + 
				"where user_id=?";
		Record user=Db.findFirst(sql,id);
		return user;
	}
}
