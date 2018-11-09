package subway;

import java.util.Queue;

public class Station {
   int stationCode;
   int externerCode;
   String stationName;
   double latitude;
   double longitude;
   int stationValue;
   int transferValue;
   int transferAll;
   Queue Close;
   String sql;
   
   Station(String stationCode, String externerCode, String stationName, String latitude, String longitude){

      this.stationCode=Integer.parseInt(stationCode);
      this.externerCode=Integer.parseInt(externerCode);
      this.stationName=stationName;
      this.latitude=Double.parseDouble(latitude);
      this.longitude=Double.parseDouble(longitude);
      transferAll=0;
      transferValue=0;
      stationValue=0;
   }
   int getStCode() {
      return stationCode;
   }
   int getExCode() {
      return externerCode;
   }
   String getName() {
      return stationName;
   }
   double getLatitude() {
      return latitude;
   }
   double getlongitude() {
      return longitude;
   }
   int getTransferValue() {
      return transferValue;
   }
   int getTransferAll() {
      return transferAll;
   }
   void Evalue(int count, int transferCount) {
      
   }
}