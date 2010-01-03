package edu.hit.ir.ltpService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//import java.util.LinkedList;

import org.jdom.Element;

/**
 * <p>Title: Word</p>
 * <p>Description: 单词</p>
 * <p>Organization: HIT-CIR</p>
 * @author    Zhonghua Han
 * @date       Nov 1, 2009
 *
 */
public class Word implements Cloneable{

	private Element ele;
//*
	public Word(Element ele) {
		// TODO Auto-generated constructor stub
//		this.ele = (Element)ele.clone();
		this.ele = ele;
	}
	
	public Word(){
		ele = new Element(LTPTag.WORD);
	}
//*/
	public int getID(){
		return Integer.parseInt(ele.getAttributeValue(LTPTag.ID));
	}
	
	public String getWS(){
		return ele.getAttributeValue(LTPTag.CONT);
	}
	
	public String getPOS(){
		return ele.getAttributeValue(LTPTag.POS);
	}
	/*
	public void setPos(String pos){
		ele.setAttribute("pos", pos);
	}
	//*/
	public String getNE(){
		return ele.getAttributeValue(LTPTag.NE);
	}
	
	public String getWSD(){
		return ele.getAttributeValue(LTPTag.WSD);
	}
	
	
	public String getWSDExplanation(){
		return ele.getAttributeValue(LTPTag.WSD_EXP);
	}
	
	public int getParserParent(){
		if(ele.getAttributeValue(LTPTag.PSR_PARENT) == null)
			return -3;
		return Integer.parseInt(ele.getAttributeValue(LTPTag.PSR_PARENT));
	}
	
	public String getParserRelation(){
		return ele.getAttributeValue(LTPTag.PSR_RELATE);
	}
		
	public boolean isPredicate(){
		return ele.getChildren().size()==0 ? false : true;
	}
	
	public ArrayList<SRL> getSRLs(){
		ArrayList<SRL> srl = new ArrayList<SRL>();
		for(int i = 0; i< ele.getChildren().size(); ++i){
			srl.add(
					new SRL(
							((Element)ele.getChildren().get(i)).getAttributeValue(LTPTag.SRL_TYPE)
							,Integer.parseInt(
									((Element)ele.getChildren().get(i)).getAttributeValue(LTPTag.SRL_BEGIN))
							,Integer.parseInt(
									((Element)ele.getChildren().get(i)).getAttributeValue(LTPTag.SRL_END))
							)
					);
		}
		return srl;
	}
	
	public void setID(int id){
		ele.setAttribute(LTPTag.ID, "" + id);
	}
	
	public void setWS(String content){
		ele.setAttribute(LTPTag.CONT, content);
	}
	
	public void setPOS(String pos){
		ele.setAttribute(LTPTag.POS, pos);
	}
	
	public void setNE(String ne){
		ele.setAttribute(LTPTag.NE, ne);
	}
	
	public void setWSD(String wsd, String explanation){
		ele.setAttribute(LTPTag.WSD, wsd);
		ele.setAttribute(LTPTag.WSD_EXP, explanation);
	}
	
	public void setParser(int parent, String relation){
		ele.setAttribute(LTPTag.PSR_PARENT, "" + parent);
		ele.setAttribute(LTPTag.PSR_RELATE, relation);
	}
	
	private void setSRL(ArrayList<SRL> srls){
		if(srls.isEmpty()){
			return;
		}
		for(int i = 0; i<srls.size(); ++i){
			Element srl = new Element(LTPTag.SRL_ARG);
			srl.setAttribute(LTPTag.SRL_TYPE, srls.get(i).type);
			srl.setAttribute(LTPTag.SRL_BEGIN, "" + srls.get(i).beg);
			srl.setAttribute(LTPTag.SRL_END, "" + srls.get(i).end);
			ele.addContent(srl);
		}
		return;
	}
	
	public boolean hasID(){
		return ele.getAttribute(LTPTag.ID) != null;
	}
	
	public boolean hasWS(){
		return ele.getAttribute(LTPTag.CONT) != null;
	}
	
	public boolean hasPOS(){
		return ele.getAttribute(LTPTag.POS) != null;
	}
	
	public boolean hasNE(){
		return ele.getAttribute(LTPTag.NE) != null;
	}
	
	public boolean hasWSD(){
		return ele.getAttribute(LTPTag.WSD) != null && ele.getAttribute(LTPTag.WSD_EXP) != null;
	}
	
	public boolean hasParser(){
		return ele.getAttribute(LTPTag.PSR_PARENT) != null && ele.getAttribute(LTPTag.PSR_RELATE) != null;
	}
	
	public Object clone() throws CloneNotSupportedException{
		Word wd = (Word) super.clone();
		
		wd.ele = (Element) ele.clone();
		
		return wd;
	}
	/*
	public String getSrl(){
		
	}
	//*/
	//需要将其它方法及属性隐藏
}
