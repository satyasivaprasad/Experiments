package com.newgame.network;

public class Config {
    public static final boolean DEVELOPER_MODE = false;
    public static final String url = "http://172.168.2.174:8080/MyLoginForm/rest/WebService/";
    public static final String registration_url = "POST-api-Users";
    public static final String live_url = "http://Gamesapi.knlmtk.co/api";
    public static final String GAMEDETAILS_URL = "/GameDetails";
    public static final String GAMETIMINGS_URL = "/GamesTimings";
    public static final String AgentInfo_URL = "/Agents?imeno=%s";
    public static final String Tabs_URL = "/Tabs?IMEINo=%s&PhoneNo=%s"; //?IMEINo=123456&PhoneNo=9885479672
    public static final String Verification_URL = "/Verification?PhoneNo=%s&VerificationCode=%s"; //Verification?PhoneNo=9885479672&VerificationCode=abc123
    public static final String games_URL = "/games";
    public static final String GamesConfig_URL = "/GamesConfig";
    public static final String SubGamesConfig_URL = "/GameStatusWithCurrentTime/%s";
    public static final String SendBulkData_URL = "/bulkTransactions";
    public static final String GetUserNameAndPassword = "/GetUserNameAndPassword?IMENO=%s";
    public static final String CheckTabIDAndEmi = "/CheckTabIDAndEmi?TabID=%s&EmiNO=%s";
    public static final String CheckUserNameAndPassword = "/CheckUserNameAndPassword?UserName=%s&password=%s&Imeno=%s";
    public static final String SubAgentinfo = "/SubAgentinfo?AgentID=%s";



    public static String OPEN_ID = "1";
    public static String BRACKET_ID = "2";
    public static String CLOSE_ID = "3";
    public static String CPANA_ID = "4";
    public static String OPANA_ID = "5";


}
