package com.newgame.database;

/**
 * Created by admin on 21-03-2015.
 */
public class Queries {

    private static Queries instance;

    public static Queries getInstance(){
        if(instance==null) {
            instance = new Queries();
        }
        return instance;
    }

    public String checkExistance(String tableName){
        return "select * from "+tableName;
    }
    
    public String getRecordDetails(String tableName,String transId,String status, String userId, String mainGameId) {
        return "select rowid _id,* from "+tableName + " where Status = '"+status+"'"+" and TransId = '"+transId+"'"+" and UserId = '"+userId+"'"+" and MainGameId = '"+mainGameId+"'"+";";
//        return "select rowid _id,* from "+tableName+" where GameType = "+gameId;
    }
    
    
    public String getRecordDetailswithSeperator(){
        return "SELECT EntryId,GameType,Number,Amount,Time,GameTypeId,TransId,UserId,UserName,Date FROM EachEntryDetails";
    }
    
    public String getRecordDetailsBasedonTransID(String transID){
        return "SELECT EntryId,GameType,Number,Amount,Time,GameTypeId,TransId,UserId,UserName,Created_date,Updated_Date,Date, SubAgentID FROM EachEntryDetails where TransId = '"+transID+"'"+";";
    }
    
    public String getSumDetails(String gameId, String userId,  String mainGameId) {
    	return "SELECT Number, SUM(Amount) FROM EachEntryDetails   where GameType = "+gameId+" and UserId = '"+userId+"'"+" and MainGameId = '"+mainGameId+"'"+" GROUP BY Number;";
    }
    
    
    public String getTotalSumByGameType(String gameId, String userId, String mainGameId) {
    	return "SELECT SUM(Amount) FROM EachEntryDetails where GameType = "+gameId+" and UserId = '"+userId+"'"+" and MainGameId = '"+mainGameId+"'"+";";
    }
    
    public String getTotalSumByGameType(String gameId){
    	return "SELECT SUM(Amount) FROM EachEntryDetails where GameType = "+gameId+"'"+";";
    }
    
    
    public String getTotalSumByTransactionType(String transId){
    	return "SELECT SUM(Amount) FROM EachEntryDetails where  TransId = '"+transId+"'"+";";
    }
    
    public String updateNumStatus(String transId, String num,String amount){
    	return "update EachEntryDetails set Amount = "+amount+" where EntryId = '"+transId+"'"+";";
    }
    
    public String updateStatus(String transId, String status){
    	return "update EachEntryDetails set Status = '"+status+"'"+" where TransId = '"+transId+"'"+";";
    }

    
    public String deletePreviousData(String date) {
    	return "delete from EachEntryDetails where Date != '"+date+"'"+";";
    }
    
    
    public String checkUser(String name, String password) {
    	return "select * from user_details where UserName= '"+name+"'"+" and "+"Password = '"+password+"'"+";";
    }
    
}
