package edu.hit.ir.ltpService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

//import edu.hit.ir.webcleitapi.WebCleintAPI.PreemptiveAuth;

public class CirService {
	public DefaultHttpClient httpclient = new DefaultHttpClient();
	private String serverAddress = "api.ltp-cloud.com";
	private String uri = "/ltp_srv/ltp";
	// private String uri = "/demo/ltp_ws/ltp";
	// private int ports = 12345;
	private int ports = 8080;
	// private String param;
	private String encoding = "gbk";
	private String encoding2 = "gbk";
	private String analysisOptions;
	private boolean isEncoding = false;
	private boolean isAllAnalysis = true;
	private boolean dependencyParser = false;
	private boolean isXml = true;
	private String usname;
	private String passwd;

	public CirService(String authorization) {
		super();
		usname = authorization.substring(0, authorization.indexOf(":"));
		passwd = authorization.substring(authorization.indexOf(":") + 1);
	}

	public String Connect(String parameters) {
		HashMap<String, String> parMap = new HashMap<String, String>();
		if (isEncoding) {
			parMap.put("c", encoding);
		}
		if (!isAllAnalysis) {
			parMap.put("t", analysisOptions);
		}
		if (isXml) {
			parMap.put("x", "y");
		}
		if (parameters != null) {
			parMap.put("s", parameters);
		}
		String str = null;
		try {			
			str = Connection.doPost(serverAddress, ports, uri, parMap, encoding2, usname, passwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public boolean setEncoding(String en) {
		if (en == "utf-8") {
			encoding = "utf8";
			encoding2 = "utf-8";
			isEncoding = true;
		} else if (en == "gb2312") {
			encoding = "gb2312";
			encoding2 = "gb2312";
			isEncoding = false;
		} else if (en == "gbk") {
			encoding = "gbk";
			encoding2 = "gbk";
		} else
			return false;
		return true;
	}

	public void setAuthorization(String authorization) {
		usname = authorization.substring(0, authorization.indexOf(":"));
		passwd = authorization.substring(authorization.indexOf(":") + 1);
	}

	public void setAnalysisOptions(String option) {
		isAllAnalysis = false;
		if (LTPOption.WS.equals(option)) {
			analysisOptions = "ws";
			// System.out.println(analysisOptions);
		} else if (LTPOption.POS.equals(option)) {
			analysisOptions = "pos";
		} else if (LTPOption.NE.equals(option)) {
			analysisOptions = "ner";
		} else if (LTPOption.PARSER.equals(option)) {
			analysisOptions = "dp";
		} else if (LTPOption.WSD.equals(option)) {
			analysisOptions = "wsd";
		} else if (LTPOption.SRL.equals(option)) {
			analysisOptions = "srl";
		} else {
			isAllAnalysis = true;
		}
		return;
	}

	public void setAnalysisAll() {
		isAllAnalysis = true;
	}

	public void setDependencyParser() {
		dependencyParser = true;
	}

	public String getEncoding() {
		return encoding2;
	}

	public void setXmlOption(boolean isXml) {
		this.isXml = isXml;
	}

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

	public String doPost(String host, int port, String uri,
			HashMap<String, String> paraMap, String charset) throws IOException {
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
		// */
		// HttpResponse response = httpclient.execute(httppost);

		if (response.getStatusLine().toString().indexOf("401") >= 0) {
			throw new RuntimeException("Authorization is denied!");
		}
		HttpEntity res_entity = response.getEntity();

		if(res_entity != null) {
			InputStream is = res_entity.getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null){
			   buffer.append(line.trim());
			}
			return buffer.toString();
		}
		return null;
	}
	
	static class PreemptiveAuth implements HttpRequestInterceptor {  		  
        public void process(  
                final HttpRequest request,   
                final HttpContext context) throws HttpException, IOException {  
              
            AuthState authState = (AuthState) context.getAttribute(  
                    ClientContext.TARGET_AUTH_STATE);  
              
            // If no auth scheme avaialble yet, try to initialize it preemptively  
            if (authState.getAuthScheme() == null) {  
                AuthScheme authScheme = (AuthScheme) context.getAttribute(  
                        "preemptive-auth");  
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(  
                        ClientContext.CREDS_PROVIDER);  
                HttpHost targetHost = (HttpHost) context.getAttribute(  
                        ExecutionContext.HTTP_TARGET_HOST);  
                if (authScheme != null) {  
                    Credentials creds = credsProvider.getCredentials(  
                            new AuthScope(  
                                    targetHost.getHostName(),   
                                    targetHost.getPort()));  
                    if (creds == null) {  
                        throw new HttpException("No credentials for preemptive authentication");  
                    }  
                    authState.setAuthScheme(authScheme);  
                    authState.setCredentials(creds);  
                }  
            }  
              
        }  
          
    }  
	
	public void close(){
		Connection.close();
	}
}
