package servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mysql.jdbc.log.Log;

import db.Conndb;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tool.Product;

public class ProductServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		
		
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Product> list = new ArrayList<Product>();
		try {
			conn = Conndb.openConn();
			String sql = "select * from product";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
				product.setPrice(rs.getDouble(3));
				list.add(product);
			}
			JSONArray jsonArray = new JSONArray();
			 for(int i=0;i<list.size();i++){
				  JSONObject jsonObject = new JSONObject();
				  jsonObject.put("id", list.get(i).getId()); 
				  jsonObject.put("name", list.get(i).getName());
				  jsonObject.put("price", list.get(i).getPrice());
				  
				  jsonArray.add(jsonObject);
			  }
		
			//��json���ظ�Android
			response.getWriter().write(jsonArray.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			Conndb.closeConn(rs, ps, conn);
		}
	}
	
//	public List<Product> getAllShops(HttpServletRequest request){
//		List<Product> list = new ArrayList<Product>();
//		String imagesPath = getServletContext().getRealPath("/images");
//		File file = new File(imagesPath);
//		File[] files = file.listFiles();
//		for(int i=0;i<files.length;i++){
//			int id = i + 1;
//			//ͼƬ����
//			String imageName = files[i].getName();
//			//��Ʒ����
//			String name = imageName.substring(0,imageName.lastIndexOf("."));
//			//ͼƬ��������·��
//			String imagePath = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + "/" + request.getContextPath() + "/images/" + imageName;
//			//ͼƬ�۸�
//			float price = new Random().nextInt(20) + 20;
//			
//			Product info = new Product(id,name,price,imagePath);
//			list.add(info);
//		}
//		return list;
//	}

}
