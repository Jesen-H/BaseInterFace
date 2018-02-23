package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.Conndb;

public class LoginServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int num = 0;
		
		try {
			conn = Conndb.openConn();
			String sql = "select * from user where name=? and password=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				num = 1;
			}
			response.getWriter().write(num + "");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			Conndb.closeConn(rs, ps, conn);
		}
		
	}
}
