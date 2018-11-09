package subway;

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
    static int num = 3;
    static int menu;
    //private static String[] startStation=new String [num]; 설계단계에서 필요할 것 같았는데 딱히 필요가없을듯
    
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      //menu();
      //count();
      input();
      Meeting meeting = new Meeting(menu,num);
      meeting.findCenter();
      //meeting.findDestination(null);
      meeting.show();
      return;
   }
   public static void input() {   
      String stationCode="";
       String name="";
       String line="";
       String externerCode="";
       double latitude=0;
       double longitude=0;
      
       connectDB();
       sql="delete from departure where name is not null";
       try {
          pstmt=conn.prepareStatement(sql);
          rs=pstmt.executeQuery();
       } catch (SQLException e1) {
          // TODO Auto-generated catch block
        e1.printStackTrace();
      }
          
       System.out.println("출발역은 뒤에 ‘역’자를 빼고 단어 사이사이에 공백이 없어야한다.");
       for(int i=0; i<num; i++) {
          System.out.println(i+1+"번째 역 입력 ");
          name=scanner.next();
          
          //띄어쓰기 예외처리
          String[] split = name.split(" ");
         if(split.length >= 2) {
            System.out.println("띄어쓰기 ㄴㄴ 다시입력 ㄱㄱ");
            i--;
            continue;
         }
          
          sql="select * from subway_info where name='"+name+"'";
          try {
             pstmt=conn.prepareStatement(sql);
             rs=pstmt.executeQuery();
             if(rs.next()){
              //startStation[i]=name;
              stationCode=rs.getString(1);
              name=rs.getString(2);
              line=rs.getString(3);
              externerCode=rs.getString(4);
              latitude=rs.getDouble(5);
              longitude=rs.getDouble(6);   
              sql="insert into departure values(?,?,?,?,?,?)";
              pstmt=conn.prepareStatement(sql);
              pstmt.setString(1, stationCode);
              pstmt.setString(2,name);
              pstmt.setString(3,line);
              pstmt.setString(4,externerCode);
              pstmt.setDouble(5,latitude);
              pstmt.setDouble(6,longitude);
              pstmt.executeUpdate();      
            }else{
               System.out.println("해당 역이 없습니다 ");
               i--;
               }
            }catch (SQLException e) {
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
            } catch (ClassNotFoundException e) {
               e.printStackTrace();
            } catch (SQLException e) {
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
      
      public static void menu() {
         //메뉴 선택 + 예외처리.
         while(true) {
             System.out.println("메뉴를 선택하세요.");
             System.out.println("1)최소거리 2)최소환승");
             menu = scanner.nextInt();
             if(menu != 1 && menu != 2) {
                System.out.println("제대로 입력 ㄱㄱ");
                
             }else {
                break;
             }
         }
      }
      public static void count() {
       //출발역 개수 입력받기 + 예외처리(수정 필요)
          while(true) {
             System.out.println("출발역이 몇 개 입니까 : ");
             num = scanner.nextInt();
          
             if(num<3 || num >9) {
                System.out.println("3~9까지의 숫자를 입력하세요.");
             }else if(num != (int)num) {
                System.out.println("3~9까지의 숫자를 입력하세요.");
             }else {
                //startStation=new String [num];
                break;
             }             
          }
      }
}