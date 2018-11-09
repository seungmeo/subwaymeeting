package subway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Meeting {
      private int menu;
      private int index;
      private String standard;
      String sql;
      Connection conn;
      PreparedStatement pstmt;
      ResultSet rs;
      
      Meeting(int menu, int index){
         this.menu=menu;
         this.index=index;
      }
      public void findCenter() {
         String stationCode="";
         String name="";
         String line="";
         String externerCode="";
         double latitude=0;
         double longitude=0;
         double avgLatitude=0;
         double avgLongitude=0;
          
         connectDB();
         sql="select * from departure";
         
         try {
            pstmt=conn.prepareStatement(sql);
            rs=pstmt.executeQuery();
            //무게중심(x,y) 구하기
            for(int i=0; i<index; i++) {
               if(rs.next()){
                  latitude+=rs.getDouble(5);
                  longitude+=rs.getDouble(6);
               }
            }
            avgLatitude = latitude/index;
            avgLongitude = longitude/index;   
            
            //+- 3km 범위 정하기(범위 안에 없으면 1km씩 넓히기) + Scope Table에 넣기.
            while(true) {
               double x1 = avgLatitude - 0.027;//위도 1도가 대략 111km. 따라서 1km는 대략 1/111=0.009. 따라서 3km는 0.009*3 = 0.027
               double x2 = avgLatitude + 0.027;
               double y1 = avgLongitude - 0.033;//경도 1도가 대략 88km. 따라서 1km는 대략 1/88=0.011. 따라서 3km는 0.011*3 = 0.033
               double y2 = avgLongitude + 0.033;
               
               sql = "select * from subway_info where latitude > x1 and latitude < x2 and longitude > y1 and longitude < y2";
               if(!rs.next()) {
                  x1 -= 0.009;
                  x2 += 0.009;
                  y1 -= 0.011;
                  y2 += 0.011;
               }else {
                  stationCode=rs.getString(1);
                   name=rs.getString(2);
                   line=rs.getString(3);
                   externerCode=rs.getString(4);
                   latitude=rs.getDouble(5);
                   longitude=rs.getDouble(6);   
                   sql="insert into scope values(?,?,?,?,?,?)";
                   pstmt=conn.prepareStatement(sql);
                   pstmt.setString(1, stationCode);
                   pstmt.setString(2,name);
                   pstmt.setString(3,line);
                   pstmt.setString(4,externerCode);
                   pstmt.setDouble(5,latitude);
                   pstmt.setDouble(6,longitude);
                   pstmt.executeUpdate();     
                  break;
               }
            }  
         }catch(SQLException e){
            e.printStackTrace();
         }
         closeDB();
      }
      
      public Station findDestination(Station s) {
         Station st = null;
         return st;
      }      
      public void loop() {}   
      public void show() {
         connectDB();
         sql="select * from departure";
         
         try {
            pstmt=conn.prepareStatement(sql);
            rs=pstmt.executeQuery();
            for(int i=0; i<index; i++) {
               if(rs.next()){
                  System.out.println(rs.getString(2));
               }
            }            
         }catch(SQLException e){
            e.printStackTrace();
         }
         closeDB();   
      }
      
      public void connectDB() {
            try {
               Class.forName("org.mariadb.jdbc.Driver");
               conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/subwaymeeting","root","1234");
            } catch (ClassNotFoundException e) {
                  e.printStackTrace();
           } catch (SQLException e) {
                 e.printStackTrace();
           }
         }
      
      public void closeDB() {
            try {
               pstmt.close();
               rs.close();
               conn.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
   }