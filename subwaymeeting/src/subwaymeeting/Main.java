package subwaymeeting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Connection conn;
    static PreparedStatement pstmt;
    static ResultSet rs;
    static String sql;
    static Scanner scanner= new Scanner(System.in);
    final static int num=3;
    private static String[] startStation=new String [num];
    
    public static void main(String[] args) {
      // TODO Auto-generated method stub

       input();
      
    }
   
   public static void input() {   
      connectDB();
      String stationCode="";
      String name="";
      String line="";
      String externerCode="";
      double latitude=0;
      double longitude=0;
      int i=0;
      sql="delete from departured where name is not null";
      try {
         pstmt=conn.prepareStatement(sql);
         rs=pstmt.executeQuery();
      } catch (SQLException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
      
      while(i<num) {
         System.out.println(i+1+"번째 역 입력 ");
         name=scanner.next();
         sql="select * from subway_info where name='"+name+"'";
         try {
            pstmt=conn.prepareStatement(sql);
            rs=pstmt.executeQuery();
            if(rs.next()){
               startStation[i]=name;
               stationCode=rs.getString(1);
               name=rs.getString(2);
               line=rs.getString(3);
               externerCode=rs.getString(4);
               latitude=rs.getDouble(5);
               longitude=rs.getDouble(6);   
               sql="insert into departured values(?,?,?,?,?,?)";
               pstmt=conn.prepareStatement(sql);
               pstmt.setString(1, stationCode);
               pstmt.setString(2,name);
               pstmt.setString(3,line);
               pstmt.setString(4,externerCode);
               pstmt.setDouble(5,latitude);
               pstmt.setDouble(6,longitude);
               pstmt.executeUpdate();      
               i++;
            }
            else    {System.out.println("해당 역이 없습니다 ");}
            
         } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      closeDB();
   }
   
   public static void connectDB() {
         try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/subwaymeeting","root","1234");
         } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
         }
      }
   
   public static void closeDB() {
         try {
            pstmt.close();
            rs.close();
            conn.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
}