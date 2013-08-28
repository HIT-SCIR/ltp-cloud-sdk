package edu.hit.ir.ltpService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class Connection {
    public static final DefaultHttpClient httpclient = new DefaultHttpClient();

    private static UrlEncodedFormEntity genEntity(
            HashMap<String, String> paraMap, String charset)
        throws UnsupportedEncodingException {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        Iterator it = paraMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            NameValuePair nameValuePair = new BasicNameValuePair(
                    (String) element.getKey(), (String) element.getValue());
            params.add(nameValuePair);
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charset);
        return entity;
    }

    public static String doPost(String host, int port, String uri,
            HashMap<String, String> paraMap, String charset, String usname,
            String passwd) throws IOException {
        UrlEncodedFormEntity entity = genEntity(paraMap, charset);

        HttpPost httppost = new HttpPost("http://" + host + ":" + port + uri);
        httppost.setEntity(entity);

        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(usname, passwd));

        // *
        BasicScheme basicAuth = new BasicScheme();
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute("preemptive-auth", basicAuth);

        httpclient.addRequestInterceptor(
                (HttpRequestInterceptor) new PreemptiveAuth(), 0);

        HttpResponse response = httpclient.execute(httppost, localcontext);

        //		System.err.println(response.getStatusLine().toString());
        if (response.getStatusLine().toString().indexOf("401") >= 0) {
            throw new RuntimeException("Authorization is denied!");
        } else if (response.getStatusLine().toString().indexOf("200") < 0) {
            throw new RuntimeException(response.getStatusLine().toString());
        } 
        HttpEntity res_entity = response.getEntity();

        //		httpclient.getConnectionManager().closeIdleConnections(0, TimeUnit.SECONDS);

        //		StringBuffer result = new StringBuffer();
        String result = "";
        if (res_entity != null) {
            InputStream is = res_entity.getContent();			

            int size;
            byte[] tmp = new byte[4098];
            int beg=0,leng=tmp.length;
            while ((size = is.read(tmp,beg,leng)) != -1)
                if(leng == size) {
                    int i;
                    for(i = tmp.length-1; i>0; --i){
                        if(tmp[i] == '\n') {
                            //		    				System.out.println("i: "+i);
                            beg = i+1;
                            result += new String(tmp, 0, beg, charset);
                            beg = mv2beg(tmp, beg);
                            leng = tmp.length - beg;
                            break;
                        }
                    }
                    if(i==0) {
                        System.err.println("Warning: the single sentence is too long!");
                        result += new String(tmp, 0, tmp.length, charset);			
                        beg = 0;
                        leng = tmp.length;
                    }
                } else {
                    result += new String(tmp, 0, beg+size, charset);
                    beg = 0;
                    leng = tmp.length;
                }
            //		    System.err.println(result);
            //*/

            res_entity.consumeContent();
        }
        return result;
    }

    static int mv2beg(byte[] tmp, int pos) {
        for(int i = pos,j = 0; i<tmp.length; ++i, ++j) {
            tmp[j] = tmp[i];
        }
        return tmp.length - pos;
    }

    static class PreemptiveAuth implements HttpRequestInterceptor {
        public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {

            AuthState authState = (AuthState) context
                .getAttribute(ClientContext.TARGET_AUTH_STATE);

            // If no auth scheme avaialble yet, try to initialize it
            // preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context
                    .getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context
                    .getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context
                    .getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider
                        .getCredentials(new AuthScope(targetHost
                                    .getHostName(), targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException(
                                "No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }
        }

    }

    public static void close() {
        httpclient.getConnectionManager().shutdown();
    }
}
