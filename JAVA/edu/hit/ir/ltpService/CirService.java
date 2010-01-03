package edu.hit.ir.ltpService;

import java.io.IOException;
import java.util.HashMap;

import edu.hit.ir.webcleitapi.WebCleintAPI;

public class CirService {
//	private String serverAddress = "http://192.168.3.134";
//	private String serverAddress = "http://202.118.250.16";
	private String serverAddress = "http://ir.hit.edu.cn";
//	private String uri = "/ltp";
	private String uri = "/demo/ltp_ws/ltp";
//	private int ports = 12345;
	private int ports = 80;
//	private String param;
	private String encoding = "gb2312";
	private String encoding2 = "gb2312";
	private String analysisOptions;
	private boolean isEncoding = false;
	private boolean isAllAnalysis = true;
	private boolean dependencyParser = false;
	private boolean isXml = true;
	private String authorization;
	public CirService(String authorization) {
		super();
		this.authorization = authorization;
	}
	
	public String Connect(String parameters){
		HashMap<String, String> parMap = new HashMap<String, String>();
		parMap.put("a", authorization);
		if(isEncoding){
			parMap.put("c", encoding);
		}
		if(!isAllAnalysis){
			parMap.put("t", analysisOptions);
		}
		if(dependencyParser){
			parMap.put("x", "y");
		}
		if(isXml){
			parMap.put("x", "y");
		}
		if(parameters!=null){
			parMap.put("s", parameters);
		}
		String str = null;
		try {
			str = WebCleintAPI.doPost(serverAddress + ":" + ports + uri, parMap, encoding2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	public boolean setEncoding(String en){
	    if(en == "utf-8"){
	        encoding = "utf8";
	        encoding2 = "utf-8";
	        isEncoding = true;
	    }else if(en == "gb2312"){
	        encoding = "gb2312";
	        encoding2 = "gb2312";
	        isEncoding = false;
	    }else if(en == "gbk"){
	    	encoding = "gbk";
	        encoding2 = "gbk";
	    }else
	        return false;
	    return true;
	}
	public void setAuthorization(String authorization){
		this.authorization = authorization;
	}
	public void setAnalysisOptions(String option){
	    isAllAnalysis = false;
//	    System.out.println(option);
//	    System.out.println(LTPOption.WS);
	    if (LTPOption.WS.equals(option))
		{
			analysisOptions = "ws";
//			System.out.println(analysisOptions);
		}
		else if (LTPOption.NE.equals(option))
		{
			analysisOptions =  "pos";
		}
		else if (LTPOption.PARSER.equals(option))
		{
			analysisOptions = "dp";
		}
		else if (LTPOption.WSD.equals( option))
		{
			analysisOptions = "wsd";
		}
		else if (LTPOption.SRL.equals( option))
		{
			analysisOptions = "srl";
		}
		else
		{
			isAllAnalysis = true;
		}
	    return;
	}
	public void setAnalysisAll(){
		isAllAnalysis = true;;
	}
	public void setDependencyParser(){
		dependencyParser = true;
	}

	public String getEncoding() {
		return encoding2;
	}
	
	public void setXmlOption(boolean isXml){
		this.isXml= isXml;
	}
}
