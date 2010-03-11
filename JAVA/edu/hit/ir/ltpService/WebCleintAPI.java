/**
 * ====================================================================
 * 这个包是一个提供Web访问客户端的接口，通过该接口可以方便的进行Web访问，获
 * 取服务器端返回内容，目前只提供GET和POST方法。
 * 
 * 整个包完全开源，欢迎大家一起讨论学习，如果发现发现什么问题，很乐意和大家
 * 一起讨论学习
 * ====================================================================
 *
 * author：chuter
 * mail：     liulong@ir.hit.edu.cn
 * data:   2009/10/27
 */
package edu.hit.ir.ltpService;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header; 
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;

/**
 * Web客户端访问API，只提供GET和POST请求，整个包利用httpcleint4.0
 * 开源包
 * 
 * @author chuter
 * @version 0.1.a01
 * 
 */
public class WebCleintAPI {
	
	public static final DefaultHttpClient httpclient = new DefaultHttpClient();
	private static String usname;
	private static String passwd;
	private static boolean author = false;
	
	public static void setAuthor(String username, String password) {
		usname = username;
		passwd = password;
		author = true;
	}
	public static void setAuthor(String authorization) {
		usname = authorization.substring(0, authorization.indexOf(":"));
		passwd = authorization.substring(authorization.indexOf(":") + 1);
//		System.out.println("username: " +usname + "\npassword: " + passwd);
	}
	/**
	 * 在站点URL上配置参数，形成所要访问的URL
	 * @param site 站点路径
	 * @param parMap 参数，key为参数名，value为参数值
	 * @param charset URL的编码格式
	 * @return 配置好的URL
	 */
	@SuppressWarnings("unchecked")
	private static String genFullUrl(String site, HashMap<String, String> parMap,
			String charset) {
		String newUrl = site + "?";
		
		Iterator it = parMap.entrySet().iterator();
        while (it.hasNext()) {  
            Map.Entry element = (Map.Entry) it.next();
            String parVal = (String)element.getValue();
            String newParVal = "";
			try {
				newParVal = URLEncoder.encode(parVal, charset);
			} catch (UnsupportedEncodingException e) {
				// log
				e.printStackTrace();
			}
            newUrl += (String)element.getKey()+"="+newParVal;
            newUrl += "&";
        }
		
        newUrl = newUrl.substring(0, newUrl.length()-1);
		return newUrl;
	}
	
	//	设置其他的参数
	//	HttpParams params = new BasicHttpParams();
	//	HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
	//	paramsBean.setVersion(HttpVersion.HTTP_1_1);
	//	paramsBean.setContentCharset("UTF-8");
	//	paramsBean.setUseExpectContinue(true);
	
	/**
	 * 处理需要配置参数的请求，给定站点地址和参数对应表（HashMap），
	 * 生成完整的路径后，调用doGet(String url)处理
	 * 
	 * @param url
	 * @param parMap
	 * @param urlCharset URL编码类型
	 * @param contCharset 正文编码类型
	 * @return 接受到得正文内容，如果出错放回空
	 * @throws IOException 
	 */
	public static String doGet( String url, HashMap<String, String> parMap,
			String urlCharset, String contCharset ) throws IOException {
		String newUrl = genFullUrl(url, parMap, urlCharset);
		return doGet(newUrl, contCharset);
	}
	
	/**
	 * 处理GET请求，接受的参数是站点地址和参数表，生成完整的访问路
	 * 径后调用doGetObj(String url)
	 * 
	 * @param url
	 * @parMap 参数对应表
	 * @return 接受到的Object
	 * @throws IOException 
	 */
	public static Object doGetObj(String url, HashMap<String, String> parMap,
			String charset) throws IOException {
		String newUrl = genFullUrl(url, parMap, charset);
		return doGetObj(newUrl);
	}
	
	/**
	 * 处理GET请求，给定的URL就是完整的访问路径，返回服务器端返回的
	 * 内存数据结构
	 * 
	 * @param url
	 * @return 接受到的Object，如果出错返回null
	 * @throws IOException 
	 */
	public static Object doGetObj(String url) throws IOException {
		HttpGet httpget = new HttpGet(url);
		
		Object retObj = null;
		HttpEntity res_entity = null;
		try {
			HttpResponse response = httpclient.execute(httpget);
			res_entity = response.getEntity();
			if ( res_entity != null ) {
				InputStream fin = res_entity.getContent();
				ObjectInputStream foin = new ObjectInputStream(fin);
				retObj = foin.readObject();
				res_entity.consumeContent();
			}
		} catch (ClientProtocolException e1) {
			// log
			e1.printStackTrace();
		} catch (IOException e1) {
			// log
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// log
			e.printStackTrace();
		}
		
		return retObj;
	}
	
	/**
	 * 处理GET请求，给定的URL就是完整的访问路径，以字符串的形式
	 * 返回服务器端返回的正文内容（去掉报文头）
	 * 
	 * @param url
	 * @return 接受到得正文内容 如果出错放回空
	 * @throws IOException 
	 */
	public static String doGet(String url, String charset) throws IOException {
		HttpGet httpget = new HttpGet(url);
//		httpclient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,  
//                new DefaultHttpRequestRetryHandler());  
		String retStr = "";
		HttpEntity entity = null;
//		System.out.println("hello!!!!!!\n");
		try {
			System.out.println(httpget.getProtocolVersion());
			
			HttpResponse response = httpclient.execute(httpget);
			
			entity = response.getEntity();
			
			if (entity != null) {
			    InputStream instream = entity.getContent();
			    int size;
			    byte[] tmp = new byte[2048];
			    while ((size = instream.read(tmp)) != -1)
			    	retStr += new String(tmp, 0, size, charset);
			    entity.consumeContent();
			}
			entity.consumeContent();
		} catch (Exception ex) {
			//log记录
			ex.printStackTrace();
		}
		
		return retStr;
	}
	
	/**
	 * 给定参数表，生成以制定编码格式编码的POST的entity对象
	 * 
	 * @param paraMap 参数表
	 * @param charset 编码类型
	 * @return entity
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	private static UrlEncodedFormEntity genEntity(HashMap<String, String> paraMap,
		String charset) throws UnsupportedEncodingException {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		Iterator it = paraMap.entrySet().iterator();
        while (it.hasNext()) {  
            Map.Entry element = (Map.Entry) it.next();  
            NameValuePair nameValuePair = new BasicNameValuePair(
            		(String)element.getKey(), (String)element.getValue());
            params.add(nameValuePair);
        }
        
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charset);
		return entity;
	}
	
	/**
	 * 处理POST请求，返回服务器端返回的内存数据结构
	 * 
	 * @param url 访问路径
	 * @param paraMap  参数表
	 * @return 接受到的Object，如果出错返回null
	 * @throws IOException
	 */
	public static Object doPostObj(String url, 
						      HashMap<String, String> paraMap,
						      String charset) throws IOException {
		UrlEncodedFormEntity entity = genEntity(paraMap, charset);
		
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(entity);
		
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity res_entity = response.getEntity();
		Object retObj = null;
		
		if ( res_entity != null ) {
			InputStream fin = res_entity.getContent();
			ObjectInputStream foin = new ObjectInputStream(fin);
			try {
				retObj = foin.readObject();			
			} catch (ClassNotFoundException e) {
				// log
				e.printStackTrace();
			}
			entity.consumeContent();
		}

		return retObj;
	}
	
	/**
	 * 处理POST请求，以字符串的形式返回服务器端返回的正文内容（除了报头）
	 * 
	 * @param url 访问路径
	 * @param paraMap  参数表
	 * @return 接受到的正文内容，如果出错返回null
	 * @throws IOException
	 */
	public static String doPost(String url,  
						      HashMap<String, String> paraMap,
						      String charset) throws IOException {
		UrlEncodedFormEntity entity = genEntity(paraMap, charset);
		
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(entity);
		/*
		if(author) {
			httpclient.getCredentialsProvider().setCredentials(new AuthScope("202.118.250.16", 54321)
				, new UsernamePasswordCredentials(usname, passwd));
		}
		//*
		BasicScheme basicAuth = new BasicScheme();
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute("preemptive-auth", basicAuth);

		httpclient.addRequestInterceptor((HttpRequestInterceptor) new PreemptiveAuth(), 0);
		
		HttpResponse response = httpclient.execute(httppost, localcontext);
		// */
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity res_entity = response.getEntity();
		
		String retStr = "";
		if (res_entity != null) {
		    InputStream instream = res_entity.getContent();
		    int size;
		    byte[] tmp = new byte[2048];
		    while ((size = instream.read(tmp)) != -1)
		    	retStr += new String(tmp, 0, size, charset);
		    res_entity.consumeContent();
		}
		
		return retStr;
	}

	public static String doPost(String host, int port, String uri,  
						      HashMap<String, String> paraMap,
						      String charset) throws IOException {
		UrlEncodedFormEntity entity = genEntity(paraMap, charset);
		
		HttpPost httppost = new HttpPost("http://" + host + ":" + port + uri);
		httppost.setEntity(entity);
		
		httpclient.getCredentialsProvider().setCredentials(new AuthScope(host, port)
			, new UsernamePasswordCredentials(usname, passwd));
		
		//*
		BasicScheme basicAuth = new BasicScheme();
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute("preemptive-auth", basicAuth);

		httpclient.addRequestInterceptor((HttpRequestInterceptor) new PreemptiveAuth(), 0);
		
		HttpResponse response = httpclient.execute(httppost, localcontext);
		// */
//		HttpResponse response = httpclient.execute(httppost);
		HttpEntity res_entity = response.getEntity();
		
		String retStr = "";
		if (res_entity != null) {
		    InputStream instream = res_entity.getContent();
		    int size;
		    byte[] tmp = new byte[2048];
		    while ((size = instream.read(tmp)) != -1)
		    	retStr += new String(tmp, 0, size, charset);
		    res_entity.consumeContent();
		}
		
		return retStr;
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
	public static void main(String[] argv) throws ClientProtocolException, IOException {
//		System.out.println(CleitTest.doGet("http://www.baidu.com/s?wd=%C1%F5%C1%FA"));;
		HashMap<String, String> parMap = new HashMap<String, String>();

		//测试POST方法返回Object
//		parMap.put("text", "2009周杰伦在哈尔滨举办个人演唱会，他在演唱会唱了一首《青花瓷》，非常好听。2009周杰伦在哈尔滨举办个人演唱会，他在演唱会唱了一首《青花瓷》，非常好听。");
//		Document doc = (Document) CleitTest.doPostObj("http://localhost:8087/tmstest", parMap, "UTF-8");;
//		for (Token token : doc.getTokens())
//			System.out.println(token.getContent()+": "+token.getPos());
		
		//测试POST方法返回字符串
		parMap.put("s", "2009周杰伦在哈尔滨举办个人演唱会，他在演唱会唱了一首《青花瓷》，非常好听。2009周杰伦在哈尔滨举办个人演唱会，他在演唱会唱了一首《青花瓷》，非常好听。");
	//	System.out.println(WebCleintAPI.doPost("http://192.168.3.134:12345/ltp", parMap, "gb2312"));
//		System.out.println(WebCleintAPI.doPostObj("http://192.168.3.134:12345/ltp", parMap, "gb2312").toString());
		
		//测试GET方法返回字符串且无参数
//		System.out.println(WebCleintAPI.doGet("http://202.118.250.16:12345/ltp?s=%E6%B5%8B%E8%AF%95", "gb2312"));
//		System.out.println(WebCleintAPI.doGet("http://192.168.3.134:12345/ltp?s=我们都是中国人。", "gb2312"));
//		System.out.println(WebCleintAPI.doGet("http://www.hit.edu.cn", "gb2312"));
		
		//测试GET方法返回字符串且有参数配置
//		parMap.put("s", "周杰伦");
//		System.out.println(WebCleintAPI.doGet("http://202.118.250.16:12345/ltp", parMap, "GBk", "GBK"));
		
		//测试GET方法返回Object
//		parMap.put("text", "周杰伦在哈尔滨举办个人演唱会");
//		Document doc = (Document) CleitTest.doGetObj("http://localhost:8087/tmstest", parMap);
//		for (Token token : doc.getTokens())
//			System.out.println(token.getContent()+": "+token.getPos());
	}
}
