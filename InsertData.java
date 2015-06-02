package com.gpcsoft.srplatform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gpcsoft.srplatform.core.util.DateUtils;
import com.gpcsoft.srplatform.core.util.StringUtils;
import com.gpcsoft.srplatform.core.util.UidFactory;

public class InsertData
{
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;

	private List companyIds;
	private List employeeIds;

	public InsertData() throws Exception
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.4.1:1521:ge01", "SRPLATFORM110_DEV", "SRPLATFORM110_DEV");
	}

	

	public void insertUser() throws Exception
	{
		try
		{
			String sql = "insert into auth_user(id,username,password,EMAIL,INVALID_DATE,LOCKED,PASSWD_INVALID_DATE,USE_STATUS,CHANGED_PWD,CREATE_USER_ID,CREATE_TIME,VISIBLE,COM_ID,EMP_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			Random random = new Random();
			for (int i = 0; i < 50000; i++)
			{
				ps.setString(1, UidFactory.createUid());
				ps.setString(2, StringUtils.getRandomString(12) + i);
				ps.setString(3, "96e79218965eb72c92a549dd5a330112");
				ps.setString(4, "testddd@gpcsoft.com");
				ps.setTimestamp(5, DateUtils.getTodayTimeStamp());
				ps.setString(6, "0");
				ps.setTimestamp(7, DateUtils.getTodayTimeStamp());
				ps.setString(8, "1");
				ps.setString(9, "01");
				ps.setString(10, "1");
				ps.setTimestamp(11, DateUtils.getTodayTimeStamp());
				ps.setString(12, "1");
				int a = random.nextInt(499);
				ps.setString(13, companyIds.get(a).toString());
				ps.setString(14, employeeIds.get(a).toString());

				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			con.rollback();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null){
				//con.close();
			}
		}
	}

	public void insertEmployee() throws SQLException
	{
		try
		{
			String sql = "insert into biz_employee(id,CREATE_TIME,CREATE_USER_ID,NAME,SPELL_NAME,COM_ID)values(?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			Random random = new Random();
			for (int i = 0; i < 50000; i++)
			{
				ps.setString(1, UidFactory.createUid());
				ps.setTimestamp(2, DateUtils.getTodayTimeStamp());
				ps.setString(3, "1");
				ps.setString(4, "testEmployee" + i);
				ps.setString(5, StringUtils.convertToPinYinFirst("testEmployee" + i));
				ps.setString(6, companyIds.get(random.nextInt(499)).toString());

				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			con.rollback();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}

	public void insertCompany() throws SQLException
	{
		try
		{
			String sql = " insert into biz_company(id,CREATE_TIME,CREATE_USER_ID,CODE,NAME)values(?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			for (int i = 0; i < 50000; i++)
			{
				ps.setString(1, UidFactory.createUid());
				ps.setTimestamp(2, DateUtils.getTodayTimeStamp());
				ps.setString(3, "1");
				ps.setString(4, StringUtils.getRandomNumberString(6) + i);
				ps.setString(5, "testCompany" + i);

				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			con.rollback();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}

	public void insertOperationLog() throws SQLException
	{
		try
		{
			String sql = "insert into SYS_OPERLOG(id,action,actionContent,operatorTime,operatorUserId,operatorUserName)values(?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			for (int i = 0; i < 50000; i++)
			{
				ps.setString(1, UidFactory.createUid());
				ps.setString(2, "test insert");
				ps.setString(3, "test insert" + i);
				ps.setTimestamp(4, DateUtils.getTodayTimeStamp());
				ps.setString(5, "1");
				ps.setString(6, "admin");

				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			con.rollback();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}

	}

	/**
	 * Description: 生成公司、部门、员工及账号
	 * compNum个公司 每个公司下有 deptNum 个部门 每个部门下有 empNum 个员工 每个员工对应一个可以登录的账号
	 * 此外可以按要求生成 单位－角色 表数据。
	 * Create Date: 2012-6-28下午02:15:57
	 * Author     : dell
	 * Modify Date: 
	 * Modify By  : 
	 * @throws SQLException
	 */
	public void createData() throws SQLException{
		// 
		int compNum = 10000; // 生成公司数
		int deptNum = 1; // 生成部门数
		int empNum = 1; // 生成员工数
		
		int compNo = 210000; // 公司编号
		int num = 210000; // 员工编号
		try
		{
			// 公司
			String sql = "insert into BIZ_COMPANY(ID, COM_CODE,COM_NAME, COM_SPELL_NAME, USE_STATUS, AUDIT_STATUS, TREE_IS_LEAF, TREE_LEVEL, TREE_PATH, CREATE_USER_ID, CREATE_TIME, COM_IDENTIFIER_CODE) values(?,?,?,?,?,?,?,?,?,?,?, ?)";
			ps = con.prepareStatement(sql);
			// 部门
			String sql_org = "insert into BIZ_ORGANIZATION ( ID, COM_IDENTIFIER_CODE, ORG_NAME, ORG_TYPE, USE_STATUS, AUDIT_STATUS, TREE_IS_LEAF, TREE_LEVEL, TREE_PATH,CREATE_TIME,CREATE_USER_ID) values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps2 = con.prepareStatement(sql_org);
			// 员工
			String sql_per = "insert into BIZ_PERSONNEL (ID, PER_NAME, PER_SPELL_NAME ,PER_GENDER , COM_IDENTIFIER_CODE) values (?,?,?,?, ?)";
			PreparedStatement ps3 = con.prepareStatement(sql_per);
			String sql_emp = "insert into BIZ_EMPLOYEE(ID, CREATE_TIME, CREATE_USER_ID, DEPT_ID, EMP_SERIAL_NUMBER, EMP_MAILBOX) values (?,?,?,?,?,?)";
			PreparedStatement ps4 = con.prepareStatement(sql_emp);
			// 账号
			String sql_user = "insert into AUTH_USER (ID, USER_NAME, USER_PASSWORD, USER_LOCKED, USER_ENABLED, USER_INVALID_DATE, USER_TYPE, CREATE_TIME, CREATE_USER_ID, PER_ID,USE_STATUS, user_update_password) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps5 = con.prepareStatement(sql_user);
			// 权限
			String sql_role = "insert into AUTH_USER_ROLE ( ID ,USER_ID ,ROLE_ID) values(?,?,?)";
			PreparedStatement ps6 = con.prepareStatement(sql_role);
			
			// 单位－角色
//			String sql_com_role = "insert into AUTH_COM_ROLE(ID,COM_IDENTIFIER_CODE, ROLE_ID) values(?,?,?)";
//			PreparedStatement ps7 = con.prepareStatement(sql_com_role);
			
			con.setAutoCommit(false);
			
			for (int i = 0; i < compNum; i++)
			{   
				String compId = UidFactory.createUid();
				ps.setString(1, compId);
				ps.setString(2, "1000012667");
				ps.setString(3, "测试单位" + compNo);
				ps.setString(4, "csdw" + compNo);
				ps.setString(5, "02");
				ps.setString(6, "03");
				ps.setString(7, "1");
				ps.setInt(8, 1);
				ps.setString(9, "1");
				ps.setString(10, "1");
				ps.setTimestamp(11, DateUtils.getTodayTimeStamp());
				ps.setString(12, compId);
				
				// 单位－角色
//				ps7.setString(1, UidFactory.createUid());
//				ps7.setString(2, compId);
//				ps7.setString(3, "6738807d-496c-457f-859a-6418143c12fe");
//				ps7.addBatch();
				
				for(int j=0;j<deptNum;j++)
				{
					String deptId = UidFactory.createUid();
					ps2.setString(1, deptId);
					ps2.setString(2, compId);
					ps2.setString(3, "测试部门" + num);
					ps2.setString(4, "1");
					ps2.setString(5, "02");
					ps2.setString(6, "03");
					ps2.setString(7, "1");
					ps2.setInt(8, 1);
					ps2.setString(9, "1");
					ps2.setTimestamp(10, DateUtils.getTodayTimeStamp());
					ps2.setString(11, "1");
					
					for(int k=0;k<empNum; k++)
					{
						// Personnel
						String perId = UidFactory.createUid();
						ps3.setString(1, perId);
						ps3.setString(2, "测试员工" + num);
						ps3.setString(3, "csyg" + num);
						ps3.setString(4, "M");
						ps3.setString(5, compId);
						
						// Employee
						ps4.setString(1, perId);
						ps4.setTimestamp(2, DateUtils.getTodayTimeStamp());
						ps4.setString(3, "1");
						ps4.setString(4, deptId);
						ps4.setString(5, "empnum" + num);
						ps4.setString(6, "empmail"+ num + "@mail.com");
						
						// User
						String userId = UidFactory.createUid();
						ps5.setString(1, userId);
						ps5.setString(2, "zctesttest" + num);
						ps5.setString(3, "e10adc3949ba59abbe56e057f20f883e");
						ps5.setString(4, "0");
						ps5.setString(5, "1");
						ps5.setTimestamp(6, new Timestamp(System.currentTimeMillis()+ 100000000000L));
						ps5.setString(7, "4");
						ps5.setTimestamp(8, DateUtils.getTodayTimeStamp());
						ps5.setString(9, "1");
						ps5.setString(10, perId);
						ps5.setString(11, "02");
						ps5.setString(12, "01");
						
						// 权限
						ps6.setString(1, UidFactory.createUid());
						ps6.setString(2, userId);
						ps6.setString(3, "2");
						
						ps3.addBatch();
						ps4.addBatch();
						ps5.addBatch();
						ps6.addBatch();
						
						num ++;
					}
					ps2.addBatch();
					
				}
				ps.addBatch();
				
				compNo++;
			}
			ps.executeBatch();
//			ps7.executeBatch();
			ps2.executeBatch();
			ps3.executeBatch();
			ps4.executeBatch();
			ps5.executeBatch();
			ps6.executeBatch();
			con.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			con.rollback();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
		
		
	}
	
	public void create() throws SQLException{
		try
		{
			// test_list
			String sql = "insert into SYS_OPERLOG(id,action,actionContent,operatorTime,operatorUserId,operatorUserName)values(?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			for (int i = 0; i < 50000; i++)
			{
				ps.setString(1, UidFactory.createUid());
				ps.setString(2, "test insert");
				ps.setString(3, "test insert" + i);
				ps.setTimestamp(4, DateUtils.getTodayTimeStamp());
				ps.setString(5, "1");
				ps.setString(6, "admin");

				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			con.rollback();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		InsertData t = new InsertData();
		t.createData();
		//t.initData();
		//t.insertUser();
		// for (int i = 0; i < 10; i++)
		// {
		// t.insertUser();
		//
		// Thread.currentThread().sleep(6000);
		// }

	}

}
