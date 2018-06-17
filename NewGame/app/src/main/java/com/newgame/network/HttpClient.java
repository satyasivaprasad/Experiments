package com.newgame.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
/**
 * Class for communicating with the sever.
 * jonnalagadda.siva
 */

@SuppressLint("NewApi")
public class HttpClient {
    public static boolean offline = false;
    public static final String OFFLINE = "3";
    private static final String LOG_TAG = HttpClient.class.getName();
    private static final String HTTP_ACCEPT_LANGUAGE = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
    private static final int CONNECTION_TIMEOUT = 30000; // 3 seconds
    public static String HTTP_USER_AGENT = "app";

    public static String response = null;



    /**
     * Posting data to server
     * @param url  ----> webservice url
     * @param values  ----> Hashmap which contains posting data keys and posring data values.
     * @param onComplete
     */
    @SuppressWarnings("deprecation")
	public static void postDataToServer(final String url, final java.util.Map<String, String> values, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "app not connected");
            return;
        }

        // Not running on main thread so can use AndroidHttpClient.newInstance
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("app");

        // enable redirects
        HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(url);

        try {
            final java.util.List<org.apache.http.NameValuePair> params = new java.util.ArrayList<org.apache.http.NameValuePair>();
            for (java.util.Map.Entry<String, String> entry : values.entrySet()) {
                params.add(new org.apache.http.message.BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            final org.apache.http.client.entity.UrlEncodedFormEntity entity = new org.apache.http.client.entity.UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            final org.apache.http.HttpResponse response = client.execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT) {
            	Log.e(HttpClient.class.getName(), "Error " + statusCode + " while retrieving data from " + url + "\n" + response.getStatusLine().getReasonPhrase());
                if (null != onComplete) onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+url, "Error " + statusCode + " while posting data to " + url + "\n" + response.getStatusLine().getReasonPhrase());
                return;
            }
            if (statusCode != HttpStatus.SC_NO_CONTENT) {
                final String postResponse = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
                com.newgame.network.Log.d(HttpClient.class.getName(), "\n\npost response: \n" + postResponse);
                if (null != onComplete) onComplete.execute(true, postResponse, null);
            } else {
                if (null != onComplete) onComplete.execute(true, "HttpStatus: 204 No Content", null);
            }
        } catch(Exception e) {
            com.newgame.network.Log.e(HttpClient.class.getName(), e);
            post.abort();

            if (null != onComplete) onComplete.execute(false, null, e.getMessage());
        } finally {
            client.close();
        }
    }




    @SuppressWarnings("deprecation")
	public static void putDataToServerjson(final String url, final java.util.Map<String, String> values, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "not connected");
            return;
        }
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("sample");
        final HttpPut httpPut = new HttpPut(url);
        try {
            // Not running on main thread so can use AndroidHttpClient.newInstance

            if( values != null) {
                JSONObject json = new JSONObject(values);
                StringEntity se = new StringEntity(json.toString());
                //sets the post request as the resulting string


                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPut.setEntity(se);
                httpPut.setHeader("Accept", "application/json");
                httpPut.setHeader("Content-type", "application/json");


                Log.i("Json Data to server", json.toString());
            }
            // enable redirects
            HttpClientParams.setRedirecting(client.getParams(), true);

            final org.apache.http.HttpResponse response = client.execute(httpPut);
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_NO_CONTENT) {
                final String postResponse = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.d(HttpClient.class.getName(), "\n\npost response: \n" + postResponse);
                if (null != onComplete) onComplete.execute(true, postResponse, null);
            } else {
                if (null != onComplete) onComplete.execute(true, "HttpStatus: 204 No Content", null);
            }
        } catch(Exception e) {

            e.printStackTrace();
            Log.e(HttpClient.class.getName(), e);
            httpPut.abort();

            if (null != onComplete) onComplete.execute(false, null, e.getMessage());
        } finally {
            client.close();
        }
    }
    
   	public static void putDataToServerjson11(final String url, final JSONArray values, final ApplicationThread.OnComplete onComplete) {
           // check the connectivity mode
           if (offline) {
               if (null != onComplete) onComplete.execute(false, null, "not connected");
               return;
           }
           Log.i("Jurl...", url);

           final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("app");
           final HttpPost httpPut = new HttpPost(url);
           try {
               // Not running on main thread so can use AndroidHttpClient.newInstance

               if( values != null) {
                   StringEntity se = new StringEntity(values.toString());
                   //sets the post request as the resulting string
                   se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                   httpPut.setEntity(se);
                   httpPut.setHeader("Accept", "application/json");
                   httpPut.setHeader("Content-type", "application/json");
                   Log.i("Json Data to server", values.toString());
               } else {
               	httpPut.setHeader("Accept", "application/json");
                   httpPut.setHeader("Content-type", "application/json");
               }
               // enable redirects
               HttpClientParams.setRedirecting(client.getParams(), true);

               final org.apache.http.HttpResponse response = client.execute(httpPut);
               final int statusCode = response.getStatusLine().getStatusCode();

               if (statusCode != HttpStatus.SC_NO_CONTENT) {
                   final String postResponse = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
                   Log.d(HttpClient.class.getName(), "\n\npost response: \n" + postResponse);
                   if (null != onComplete) onComplete.execute(true, postResponse, null);
               } else {
                   if (null != onComplete) onComplete.execute(true, "HttpStatus: 204 No Content", null);
               }
           } catch(Exception e) {

               e.printStackTrace();
               Log.e(HttpClient.class.getName(), e);
               httpPut.abort();

               if (null != onComplete) onComplete.execute(false, null, e.getMessage());
           } finally {
               client.close();
           }
       }


    @SuppressWarnings("deprecation")
	public static void getDataFromServer(final String urlString,  final ApplicationThread.OnComplete onComplete)
    {
    	System.out.println("url.."+urlString);
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "app not connected");
            return;
        }
        org.apache.http.client.HttpClient client=new DefaultHttpClient();
        HttpGet getRequest=new HttpGet();

        try {
            // construct a URI object

            getRequest.setURI(new URI(urlString));

        } catch (URISyntaxException e) {
            Log.e("URISyntaxException", e.toString());
        }

        // buffer reader to read the response
        BufferedReader in=null;
        // the service response

        try {
            // execute the request
            response = getStringFromInputStream(client.execute(getRequest).getEntity().getContent());
            if (null != onComplete) onComplete.execute(true, response, null);
            Log.i("Resposne  ", response);
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocolException", e.toString());
            if (null != onComplete) onComplete.execute(true, null, null);
        } catch (IOException e) {
            Log.e("IO exception", e.toString());
            if (null != onComplete) onComplete.execute(true, null, null);
        }
    }


	public static void deleteFromServer(final String urlString,  final ApplicationThread.OnComplete onComplete)
    {
    	System.out.println("url.."+urlString);
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "app not connected");
            return;
        }
        org.apache.http.client.HttpClient client=new DefaultHttpClient();
        HttpDelete getRequest=new HttpDelete();

        try {
            // construct a URI object

            getRequest.setURI(new URI(urlString));

        } catch (URISyntaxException e) {
            Log.e("URISyntaxException", e.toString());
        }

        // buffer reader to read the response
        BufferedReader in=null;
        // the service response

        try {
            // execute the request
            response = getStringFromInputStream(client.execute(getRequest).getEntity().getContent());
            if (null != onComplete) onComplete.execute(true, response, null);
            Log.i("Resposne  ", response);
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocolException", e.toString());
            if (null != onComplete) onComplete.execute(true, null, null);
        } catch (IOException e) {
            Log.e("IO exception", e.toString());
            if (null != onComplete) onComplete.execute(true, null, null);
        }
    }
	
    /**
     * Posting data to server
     * @param url  ----> webservice url
     * @param values  ----> Hashmap which contains posting data keys and posring data values.
     * @param onComplete
     */
    public static void postDataToServerjson(final String url, final java.util.Map<String, String> values, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "not connected");
            return;
        }
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("sample");
        final org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(url);
        try {
        // Not running on main thread so can use AndroidHttpClient.newInstance

        if( values != null) {
            JSONObject json = new JSONObject(values);
            StringEntity se = new StringEntity(json.toString());
            //sets the post request as the resulting string


            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");


            Log.i("Json Data to server", json.toString());
        }
        // enable redirects
        HttpClientParams.setRedirecting(client.getParams(), true);

            final org.apache.http.HttpResponse response = client.execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_NO_CONTENT) {
                final String postResponse = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.d(HttpClient.class.getName(), "\n\npost response: \n" + postResponse);
                if (null != onComplete) onComplete.execute(true, postResponse, null);
            } else {
                if (null != onComplete) onComplete.execute(true, "HttpStatus: 204 No Content", null);
            }
        } catch(Exception e) {

            e.printStackTrace();
            Log.e(HttpClient.class.getName(), e);
            post.abort();

            if (null != onComplete) onComplete.execute(false, null, e.getMessage());
        } finally {
            client.close();
        }
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {


        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}