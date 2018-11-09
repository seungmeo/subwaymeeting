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
    //private static String[] startStation=new String [num]; ����ܰ迡�� �ʿ��� �� ���Ҵµ� ���� �ʿ䰡������
    
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
          
       System.out.println("��߿��� �ڿ� �������ڸ� ���� �ܾ� ���̻��̿� ������ ������Ѵ�.");
       for(int i=0; i<num; i++) {
          System.out.println(i+1+"��° �� �Է� ");
          name=scanner.next();
          
          //���� ����ó��
          String[] split = name.split(" ");
         if(split.length >= 2) {
            System.out.println("���� ���� �ٽ��Է� ����");
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
               System.out.println("�ش� ���� �����ϴ� ");
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
         //�޴� ���� + ����ó��.
         while(true) {
             System.out.println("�޴��� �����ϼ���.");
             System.out.println("1)�ּҰŸ� 2)�ּ�ȯ��");
             menu = scanner.nextInt();
             if(menu != 1 && menu != 2) {
                System.out.println("����� �Է� ����");
                
             }else {
                break;
             }
         }
      }
      public static void count() {
       //��߿� ���� �Է¹ޱ� + ����ó��(���� �ʿ�)
          while(true) {
             System.out.println("��߿��� �� �� �Դϱ� : ");
             num = scanner.nextInt();
          
             if(num<3 || num >9) {
                System.out.println("3~9������ ���ڸ� �Է��ϼ���.");
             }else if(num != (int)num) {
                System.out.println("3~9������ ���ڸ� �Է��ϼ���.");
             }else {
                //startStation=new String [num];
                break;
             }             
          }
      }
}