package admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import admin.vo.Admin_MembersVo;
import test.db.DBConnection;

public class Admin_MembersDao {
	public int getinfo(String mid) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=DBConnection.getCon();
			String sql="select * from members where mid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, mid);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				mid=rs.getString("mid");
				String mpw=rs.getString("mpw");
				String mname=rs.getString("mname");
				String maddress=rs.getString("maddress");
				String mpost=rs.getString("mpost");
				String mphone=rs.getString("mphone");
				String mname=rs.getString("mname");
				
			}
		}catch(SQLException s) {
			s.printStackTrace();
			return -1;
		}finally {
			DBConnection.close(con, pstmt, null);
		}
	}
	public ArrayList<Admin_MembersVo> list(int startRow,int endRow,String field,String keyword){
		String sql=null;
		if(field==null || field.equals("")) { //�˻������� ���� ���
		    sql= "select * from " + 
				"( " + 
				"  select board.*,rownum rnum from " + 
				"  (" + 
				"	  select * from members order by mid desc" + 
				"  ) board" + 
				") where rnum>=? and rnum<=?";
		}else{ //�˻������� �ִ� ���
			sql="select * from " + 
				"( " + 
				"  select board.*,rownum rnum from " + 
				"  (" + 
				"	  select * from members where "+ field +" like '%"+ keyword + "%' order by mid desc" + 
				"  ) board" + 
				") where rnum>=? and rnum<=?";
		 }
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=DBConnection.getCon();
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,startRow);
			pstmt.setInt(2,endRow);
			rs=pstmt.executeQuery();
			ArrayList<Admin_MembersVo> list=new ArrayList<Admin_MembersVo>();
			while(rs.next()) {
				Admin_MembersVo vo=new Admin_MembersVo(
						rs.getString("mid"),
						rs.getString("mpw"),
						rs.getString("mname"), 
						rs.getString("maddress"), 
						rs.getString("mpost"),
						rs.getString("mphone"),
						rs.getDate("mrdate"),
						rs.getDate("mbirth"),
						rs.getInt("mdrop"),
						rs.getInt("mmileage"),
						rs.getString("memail"));
				list.add(vo);
			}
			return list;
		}catch(SQLException se) {
			se.printStackTrace();
			return null;
		}finally {
			DBConnection.close(con, pstmt, rs);
		}
	}
	public int getCount(String field,String keyword) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=DBConnection.getCon();
			String sql="select NVL(count(*),0) from members";
			if(field!=null && !field.equals("")) {
				sql += " where " + field + " like '%" + keyword +"%'";
			}		
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				int n=rs.getInt(1);
				return n;
			}
			return -1;
		}catch(SQLException se) {
			se.printStackTrace();
			return -1;
		}finally {
			DBConnection.close(con, pstmt, rs);
		}
	}
	public int insert(Admin_MembersVo vo) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql=null;
		int n=0;
		try {
			con=DBConnection.getCon();
			if(vo.getMbirth()==null) {
				sql="insert into members values(?,?,?,?,?,?,sysdate,sysdate,0,1000,?)";
				pstmt=con.prepareStatement(sql);
				
				pstmt.setString(1, vo.getMid());
				pstmt.setString(2, vo.getMpw());
				pstmt.setString(3, vo.getMname());
				pstmt.setString(4, vo.getMaddress());
				pstmt.setString(5, vo.getMpost());
				pstmt.setString(6, vo.getMphone());
				pstmt.setString(7, vo.getMemail());
				n=pstmt.executeUpdate();
			}else {
				sql="insert into members values(?,?,?,?,?,?,sysdate,?,0,1000,?)";
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, vo.getMid());
				pstmt.setString(2, vo.getMpw());
				pstmt.setString(3, vo.getMname());
				pstmt.setString(4, vo.getMaddress());
				pstmt.setString(5, vo.getMpost());
				pstmt.setString(6, vo.getMphone());
				pstmt.setDate(7, vo.getMbirth());
				pstmt.setString(8, vo.getMemail());
				n=pstmt.executeUpdate();
			}
			return n;
		}catch(SQLException s) {
			s.printStackTrace();
			return -1;
		}finally {
			DBConnection.close(con, pstmt, null);
		}
	}
	public int update(Admin_MembersVo vo) {
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=DBConnection.getCon();
			String sql="update members set mpw=?, mname=?, maddress=?, mpost=?, mphone=?"+
					"mbirth=?,mmileage=?,memail=? where mid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, vo.getMpw());
			pstmt.setString(2, vo.getMname());
			pstmt.setString(3, vo.getMaddress());
			pstmt.setString(4, vo.getMpost());
			pstmt.setString(5, vo.getMphone());
			pstmt.setDate(6, vo.getMbirth());
			pstmt.setInt(7, vo.getMmileage());
			pstmt.setString(8, vo.getMemail());
			pstmt.setString(9, vo.getMid());
			return pstmt.executeUpdate();
		}catch(SQLException s) {
			s.printStackTrace();
			return -1;
		}finally {
			DBConnection.close(con, pstmt, null);
		}
	}
}
