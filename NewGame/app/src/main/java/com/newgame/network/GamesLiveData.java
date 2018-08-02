package com.newgame.network;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newgame.models.GameJsonDataResult;

public class GamesLiveData {
    
    public static void getGameDetails(final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"getGameDetails....",new Runnable() {
            @Override
            public void run() {
                HttpClient.getDataFromServer(url, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                        	try {
                                 onComplete.execute(success, result, msg);
							} catch (Exception e) {
								// TODO: handle exception
					            Log.e("DataParsing Exception", e.toString());
							}
                           
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    public static void getAllTransactionDetails(final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"getAllTransactionDetails....",new Runnable() {
            @Override
            public void run() {
                HttpClient.getDataFromServer(url, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                        	try {
                                 onComplete.execute(success, result, msg);
							} catch (Exception e) {
								// TODO: handle exception
					            Log.e("DataParsing Exception", e.toString());
							}
                           
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    public static void getGameTimingDetails(final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"getGameDetails....",new Runnable() {
            @Override
            public void run() {
                HttpClient.getDataFromServer(url, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                        	try {
                        		Type collectionType = new TypeToken<Collection<GameJsonDataResult>>(){}.getType();
       	                	 Gson googleJson = new Gson();
       	                	 List<GameJsonDataResult> gamesList = googleJson.fromJson((String) result,collectionType);
       	                	 
                                 onComplete.execute(success, gamesList, msg);
							} catch (Exception e) {
								// TODO: handle exception
					            Log.e("DataParsing Exception", e.toString());
							}
                           
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    public static void deleteDatafromServer(final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"getGameDetails....",new Runnable() {
            @Override
            public void run() {
                HttpClient.deleteFromServer(url, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                        	try {
                        	     onComplete.execute(success, result, msg);
							} catch (Exception e) {
								// TODO: handle exception
					            Log.e("DataParsing Exception", e.toString());
							}
                           
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    public static void UserRegister(final String userid, final HashMap<String,String> values, final ApplicationThread.OnComplete<String> onComplete)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"user registration",new Runnable() {
            @Override
            public void run() {
                HttpClient.putDataToServerjson(String.format(Config.live_url + Config.registration_url, userid), values, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                            try {
                                onComplete.execute(success, result.toString(), msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onComplete.execute(success, null, msg);
                            }
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    
    
    public static void uploadWithJson(final HashMap<String,String> values, final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"user registration",new Runnable() {
            @Override
            public void run() {
                HttpClient.putDataToServerjson(url, values, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                            try {
                                onComplete.execute(success, result.toString(), msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onComplete.execute(success, null, msg);
                            }
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    public static void uploadData(final JSONArray values, final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"user registration",new Runnable() {
            @Override
            public void run() {
                HttpClient.putDataToServerjson11(url, values, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                            try {
                                onComplete.execute(success, result.toString(), msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onComplete.execute(success, null, msg);
                            }
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    public static void SendBulkData(final JSONArray values, final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"SendBulkData....",new Runnable() {
            @Override
            public void run() {
                HttpClient.putDataToServerjson11(url, values, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                            try {
                                onComplete.execute(success, result.toString(), msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onComplete.execute(success, null, msg);
                            }
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }
    
    public static void getGenericData(final ApplicationThread.OnComplete<String> onComplete, final String url)
    {
        ApplicationThread.bgndPost(GamesLiveData.class.getName(),"SendBulkData....",new Runnable() {
            @Override
            public void run() {
                HttpClient.getDataFromServer(url, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                            try {
                                onComplete.execute(success, result.toString(), msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onComplete.execute(success, null, msg);
                            }
                        } else
                            onComplete.execute(success, null, msg);
                    }
                });
            }
        });

    }

}
