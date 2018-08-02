package com.newgame.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.newgame.commons.CommonUtils;
import com.newgame.models.GameTransactionsModel;
import com.newgame.network.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

    /* Database name */
    private final static String DATABASE_NAME = "pocgame.sqlite";

    private final static int DATA_VERSION = 1;                               
 
    private String DB_PATH;

    private Context mContext;
    private SQLiteDatabase mSqLiteDatabase = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        DB_PATH="/data/data/" + context.getPackageName() + "/databases/";
        Log.v("The Database Path" , DB_PATH);
    }

    /* create an empty database if data base is not existed */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            //this.getReadableDatabase();

            try {
                copyDataBase();

            }
            catch(SQLiteException ex){
                ex.printStackTrace();
                throw new Error("Error copying database");

            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }

            try{
                openDataBase();
            }
            catch(SQLiteException ex){
                ex.printStackTrace();
                throw new Error("Error opening database");

            }catch(Exception e){
                e.printStackTrace();
                throw new Error("Error opening database");
            }

        }

    }


    /* checking the data base is existed or not */
	/* return true if existed else return false */
    private boolean checkDataBase()
    {
        try
        {
            String check_Path = DB_PATH  + DATABASE_NAME;
            mSqLiteDatabase = SQLiteDatabase.openDatabase(check_Path, null, SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception ex) {
            // TODO: handle exception
            ex.printStackTrace();
        }
        return mSqLiteDatabase != null ? true : false;
    }

    private void copyDataBase() throws IOException {

        File dbDir = new File(DB_PATH);
        if (!dbDir.exists()) {
            dbDir.mkdir();

        }
        //Open your local db as the input stream
        Log.v("1 Opening the assest folder db ", mContext.getAssets() + "/" + DATABASE_NAME);
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        Log.i("2 Copy the db to the path " , outFileName);
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        Log.i("Open the output db " , outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        Log.i("Copied the database file" , outFileName);
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /* Open the database */
    public void openDataBase() throws SQLException {

        String check_Path = DB_PATH  + DATABASE_NAME;
        if(mSqLiteDatabase!=null)
        {
            mSqLiteDatabase.close();
            mSqLiteDatabase = null;
            mSqLiteDatabase = SQLiteDatabase.openDatabase(check_Path, null, SQLiteDatabase.OPEN_READWRITE);
        }else{
            mSqLiteDatabase = SQLiteDatabase.openDatabase(check_Path, null, SQLiteDatabase.OPEN_READWRITE);
        }


    }

    public void closeDataBase(){

        if (mSqLiteDatabase != null) {
            mSqLiteDatabase.close();
            mSqLiteDatabase = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
	
	
	/**
	 * Inserting data into database
	 * @param tableName  ---> Table name to insert data
	 * @param mapList    ---> map list which contains data
	 * @param context    ---> Current class context
	 */
	  public void insertData(String tableName,List<HashMap> mapList,Context context){
		  mSqLiteDatabase = this.getReadableDatabase();
	 
		  try{
			  
			  for(int i=0;i< mapList.size();i++) {
				    List<Entry> entryList = new ArrayList<Entry>((mapList.get(i)).entrySet()); 	        
			        String query ="insert into " + tableName;
			        String namestring, valuestring ;	        
			        StringBuffer values = new StringBuffer();
			        StringBuffer columns = new StringBuffer();       
			        for (Entry temp : entryList) {	                      
			            columns.append(temp.getKey());
			            columns.append(",");
			            values.append("'");
			            values.append(temp.getValue());
			            values.append("'");
			            values.append(",");	           
			         }
			         namestring="("+columns.deleteCharAt(columns.length()-1).toString()+")";	        
			         valuestring="("+values.deleteCharAt(values.length()-1).toString()+")";	         
			         query = query + namestring + "values" + valuestring;	         
			         Log.v(getClass().getSimpleName(), "query.."+query);
			         mSqLiteDatabase.execSQL(query);
			   }
	    }
	    catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}finally{
//			if (mSqLiteDatabase != null)
//				mSqLiteDatabase.close();
		}
		
	  }
	
	  /**
	   * Updating database records
	   * @param tableName   ---> Table name to update
	   * @param list        ---> List which contains data values
	   * @param isClaues    ---> Checking where condition availability
	   * @param clauesValue ---> Where condition values
	   * @param whereCondition ---> condition
	   * @param context  ---> Current class 
	   */
	  public void updateData(String tableName,List<HashMap> list,Boolean isClaues,String clauesValue,String whereCondition,Context context){

		  mSqLiteDatabase = this.getWritableDatabase();
		  try{
			    for(int i=0;i<list.size();i++){
				    List<Entry> entryList = new ArrayList<Entry>((list.get(i)).entrySet()); 	        
			        String query = "update " + tableName + " set ";
					String namestring = "";
			        System.out.println("\n==> Size of Entry list: " + entryList.size());
			        StringBuffer values = new StringBuffer();
			        StringBuffer columns = new StringBuffer();       
			         for (Entry temp : entryList) {
			        	columns.append(temp.getKey());
			            columns.append("='");
			            columns.append(temp.getValue());
			            columns.append("',");       	 
			           }
			        namestring=columns.deleteCharAt(columns.length()-1).toString();	
			     	query = query + namestring +""+ whereCondition;			
			     	mSqLiteDatabase.execSQL(query);
				  }
			  }catch (Exception e) {
					e.printStackTrace();
			}finally{
//					 mSqLiteDatabase.close();
			}
		  
	  }
	  
	  /**
	   * Deleting records from database table
	   * @param tableName  ---> Table name
	   * @param columnName ---> Column name to deleting
	   * @param value      ---> Value for where condition
	   * @param isWhere  ---> Checking where condition is required or not
	   */
	  public void deleteRow(String tableName, String columnName,String value,boolean isWhere){
		  try
			{
			  mSqLiteDatabase = this.getWritableDatabase();

			  String query="delete from "+tableName;
			  if(isWhere){
				  query=query+"where"+columnName +"="+value;
			  }
			  mSqLiteDatabase.execSQL(query);
			 }catch (Exception e) {
				e.printStackTrace();
				// ACRA.getErrorReporter().handleException(e);
			 }finally{
				 mSqLiteDatabase.close();
			 }
 
	  }
	  
	  public void updateSatus(String query){
		  try
			{
			  Log.v(DatabaseHelper.class.getName(), "query_str...."+query);
			  mSqLiteDatabase = this.getWritableDatabase();
			  mSqLiteDatabase.execSQL(query);
			 }catch (Exception e) {
				e.printStackTrace();
				// ACRA.getErrorReporter().handleException(e);
			 }finally{
				 mSqLiteDatabase.close();
			 }

	  }
	  
	  
	  
//	  public void open() throws SQLException {
//		    database = DatabaseHelper.getWritableDatabase();
//		  }
	  
	  /**
	   * Sample method which returns a data cursor.
	   * @param query_str  ---> Query string 
	   * @return ---> Cursor
	   */
	  public  Cursor getdata(String query_str) {
//		  SQLiteDatabase database = DatabaseHelper.getDBAdapterInstance(_context, _dbName, _tables).getWritableDatabase();
//		  database = this.getReadableDatabase();
		  mSqLiteDatabase = this.getReadableDatabase();
		  try{ 
			  Log.v(DatabaseHelper.class.getName(), "query_str...."+query_str);
			Cursor mCursor = mSqLiteDatabase.rawQuery(query_str, null);
			return mCursor;
			}
		  catch(Exception e){
			  // ACRA.getErrorReporter().handleException(e);
//			  System.out.print("error"+e.getMessage());
			  e.printStackTrace();

			  if (mSqLiteDatabase != null)
				  mSqLiteDatabase.close();
			  
			  return null;
		  }finally{
//				if (mSqLiteDatabase != null)
//					mSqLiteDatabase.close();
			}
		}

	  public String getCountValue(String query)
		{
		    mSqLiteDatabase = this.getWritableDatabase();
			Cursor mOprQuery = this.queryDataBase(query);
			try
			{
				if (mOprQuery != null && mOprQuery.moveToFirst()) {
					return mOprQuery.getString(0);
				}
				
				return null;
				
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				mOprQuery.close();
			}
			return "";
	}

	  public  LinkedHashMap<String, String> getOperations(String query){
			LinkedHashMap<String, String> mOperations =  new LinkedHashMap<String, String>();
			 mSqLiteDatabase = this.getWritableDatabase();
			Cursor mOprQuery = this.queryDataBase(query);
			try {
				if (mOprQuery.moveToFirst()) {
					do {
						Log.v("111", "query.."+query);
						mOperations.put(mOprQuery.getString(0), mOprQuery.getString(1));
					} while (mOprQuery.moveToNext());
					
				}
				
			} catch (Exception e) {
				Log.e("GetData:::::::",e.getMessage());
			}finally{
//				mSqLiteDatabase.closedatabase();
				if(null != mOprQuery)
				mOprQuery.close();
			}
			return mOperations;
		}
	  
	  
	  public  ArrayList<GameTransactionsModel> getFinalTransactionData(String query){
		  ArrayList<GameTransactionsModel> arrData =  new ArrayList<GameTransactionsModel>();
			 mSqLiteDatabase = this.getWritableDatabase();
			Cursor mDataQuery = this.queryDataBase(query);
			GameTransactionsModel mDataModel = null;
			try {
				if (mDataQuery.moveToFirst()) {
					do {
						Log.v("111", "query.."+query);
						 mDataModel = new GameTransactionsModel();
						 mDataModel.setEntryId(mDataQuery.getString(mDataQuery.getColumnIndex("EntryId")));
						 mDataModel.setGameType(mDataQuery.getString(mDataQuery.getColumnIndex("GameType")));
						 mDataModel.setNumber(mDataQuery.getString(mDataQuery.getColumnIndex("Number")));
						 mDataModel.setEachAmount(mDataQuery.getString(mDataQuery.getColumnIndex("Amount")));
						 mDataModel.setTime(mDataQuery.getString(mDataQuery.getColumnIndex("Time")));
						 mDataModel.setGameName(mDataQuery.getString(mDataQuery.getColumnIndex("GameTypeId")));
						 mDataModel.setTransactionId(mDataQuery.getString(mDataQuery.getColumnIndex("TransId")));
						 mDataModel.setUserId(mDataQuery.getString(mDataQuery.getColumnIndex("UserId")));
						 mDataModel.setUserName(mDataQuery.getString(mDataQuery.getColumnIndex("UserName")));
						 mDataModel.setDate(mDataQuery.getString(mDataQuery.getColumnIndex("Date")));
						 mDataModel.setTextColor(CommonUtils.giveColorCode(Integer.parseInt(mDataQuery.getString(mDataQuery.getColumnIndex("GameType"))), mContext));
						 arrData.add(mDataModel);
						 mDataModel = null;
					} while (mDataQuery.moveToNext());
					
				}
				
			} catch (Exception e) {
				Log.e("DataBaseHelper:::::::GameTransactionsModel::::",e.getMessage());
			}finally{
//				mSqLiteDatabase.closedatabase();
				if(null != mDataQuery)
					mDataQuery.close();
			}
			return arrData;
		}
	  
	  public  ArrayList<GameTransactionsModel> getMainTransactionData(String query){
		  ArrayList<GameTransactionsModel> arrData =  new ArrayList<GameTransactionsModel>();
			 mSqLiteDatabase = this.getWritableDatabase();
			Cursor mDataQuery = this.queryDataBase(query);
			GameTransactionsModel mDataModel = null;
			try {
				if (mDataQuery.moveToFirst()) {
					do {
						Log.v("111", "query.."+query);
						 mDataModel = new GameTransactionsModel();
						 mDataModel.setEntryId(mDataQuery.getString(mDataQuery.getColumnIndex("EntryId")));
						 mDataModel.setGameType(mDataQuery.getString(mDataQuery.getColumnIndex("GameType")));
						 mDataModel.setNumber(mDataQuery.getString(mDataQuery.getColumnIndex("Number")));
						 mDataModel.setEachAmount(mDataQuery.getString(mDataQuery.getColumnIndex("Amount")));
						 mDataModel.setTime(mDataQuery.getString(mDataQuery.getColumnIndex("Time")));
						 mDataModel.setGameName(mDataQuery.getString(mDataQuery.getColumnIndex("GameTypeId")));
						 mDataModel.setTransactionId(mDataQuery.getString(mDataQuery.getColumnIndex("TransId")));
						 mDataModel.setUserId(mDataQuery.getString(mDataQuery.getColumnIndex("UserId")));
						 mDataModel.setUserName(mDataQuery.getString(mDataQuery.getColumnIndex("UserName")));
						 mDataModel.setDate(mDataQuery.getString(mDataQuery.getColumnIndex("Date")));
						 mDataModel.setCreated_date(mDataQuery.getString(mDataQuery.getColumnIndex("Created_date")));
						 mDataModel.setUpdated_Date(mDataQuery.getString(mDataQuery.getColumnIndex("Updated_Date")));
						 mDataModel.setTextColor(CommonUtils.giveColorCode(Integer.parseInt(mDataQuery.getString(mDataQuery.getColumnIndex("GameType"))), mContext));
						 mDataModel.setSubAgentID(mDataQuery.getInt(mDataQuery.getColumnIndex("SubAgentID")));
						 arrData.add(mDataModel);
						 mDataModel = null;
					} while (mDataQuery.moveToNext());
					
				}
				
			} catch (Exception e) {
				Log.e("DataBaseHelper:::::::GameTransactionsModel::::",e.getMessage());
			}finally{
//				mSqLiteDatabase.closedatabase();
				if(null != mDataQuery)
					mDataQuery.close();
			}
			return arrData;
		}
	  
	  
	  public static boolean  isRecordExisted(String query, Context ctx) {
	        Cursor cursor = null;
	        DatabaseHelper dbhelper = new DatabaseHelper(ctx);
	        try {
	            dbhelper.openDataBase();
	            cursor = dbhelper.getdata(query);
	            if (cursor != null && cursor.moveToFirst()) {
	                return cursor.getCount() == 0 ? false : true;
	            }
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	        } finally {
	            if (cursor != null)
	                cursor.close();
	            dbhelper.closeDataBase();
	        }

	        return false;
	    }
	  
	  public  String getMaxcp_event_compl_idFromTable(String Table,String type,String columnname)
		{
			SQLiteDatabase objSqliteDB = null;
			Long strMaxId = null;
			String strid = null;
			SQLiteStatement stmtSelectRec = null;
			try {

				objSqliteDB = this.getWritableDatabase();
//				objSqliteDB.beginTransaction();

				stmtSelectRec = objSqliteDB
						.compileStatement("select max(CAST( (case "
								+ "when substr("+columnname+",1,1)  is '_' then  substr("+columnname+",1+1)"
								+ "when substr("+columnname+",2,1)  is '_' then  substr("+columnname+",2+1)"
								+ "when substr("+columnname+",3,1)  is '_' then  substr("+columnname+",3+1)"
								+ "when substr("+columnname+",4,1)  is '_' then  substr("+columnname+",4+1)"
								+ "when substr("+columnname+",5,1)  is '_' then  substr("+columnname+",5+1)"
								+ "when substr("+columnname+",6,1)  is '_' then  substr("+columnname+",6+1)"
								+ "when substr("+columnname+",7,1)  is '_' then  substr("+columnname+",7+1)"
								+ "when substr("+columnname+",8,1)  is '_' then  substr("+columnname+",8+1)"
								+ "when substr("+columnname+",9,1)  is '_' then  substr("+columnname+",9+1)"
								+ "when substr("+columnname+",10,1)  is '_' then  substr("+columnname+",10+1)"
								+ "when substr("+columnname+",11,1)  is '_' then  substr("+columnname+",11+1)"
								+ "when substr("+columnname+",12,1)  is '_' then  substr("+columnname+",12+1)"
								+ "when substr("+columnname+",13,1)  is '_' then  substr("+columnname+",13+1)"
								+ "when substr("+columnname+",14,1)  is '_' then  substr("+columnname+",14+1)"
								+ "when substr("+columnname+",15,1)  is '_' then  substr("+columnname+",15+1)"
								+ "when substr("+columnname+",16,1)  is '_' then  substr("+columnname+",16+1)"
								+ "when substr("+columnname+",17,1)  is '_' then  substr("+columnname+",17+1)"
								+ "when substr("+columnname+",18,1)  is '_' then  substr("+columnname+",18+1)"
								+ "when substr("+columnname+",19,1)  is '_' then  substr("+columnname+",19+1)"
								+ "when substr("+columnname+",20,1)  is '_' then  substr("+columnname+",20+1)"
								+ "else "+columnname +" end) as integer)) from " + Table);
				
				strMaxId = stmtSelectRec.simpleQueryForLong();
				
				strid=type+"_" + (strMaxId + 1);
				Log.v("for id...,", "strMaxId..."+strMaxId+"strid.."+strid);
			
			} catch (Exception e) {
				Log.e("GetData:::::::",e.getMessage());
			} finally {
//				objSqliteDB.endTransaction();
				stmtSelectRec.close();
//				DataBase.closedatabase();
			}
			return strid;

		}
	  
    public Cursor queryDataBase(String query){
        DatabaseHelper mDataBaseHelper = new DatabaseHelper(mContext);
        mSqLiteDatabase = mDataBaseHelper.getReadableDatabase();
        Log.v("query ", query);
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);
        return cursor;
    }
}
