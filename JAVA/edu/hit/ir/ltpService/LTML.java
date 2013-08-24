package edu.hit.ir.ltpService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
//import java.awt.List;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStream;
//import java.text.Format;

import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.JDOMException;

/**
 * Title: LTML
 * Description: special for ltp xml
 * Organization: HIT-CIR
 * 
 * @author Zhonghua Han (zhhan@ir.hit.edu.cn)
 * @date Nov 10, 2009
 */
public class LTML {
	private SAXBuilder xml;
	private Document doc;
	private Element root;
	private Element note;
	// there is almost no infor in para element and the paragraph index is built
	// on the sentence actually
	private ArrayList<paraIndex> pIndex = new ArrayList<paraIndex>(); // paragraph
																	  // index
	private ArrayList<sentIndex> sIndex = new ArrayList<sentIndex>(); // sentence
																	  // index

	private String encoding = "utf-8";
	private int sentenceNum = 0;
	private int paragraphNum = 1;

	public LTML() {
		super();
		xml = new SAXBuilder();
	}

	public void build(File file) throws JDOMException, IOException {
		doc = xml.build(file);
		root = doc.getRootElement().getChild(LTPTag.DOC);
		note = doc.getRootElement().getChild(LTPTag.NOTE);
		initIndex();
	}

	public void build(String sourceStr) throws JDOMException, IOException {
		// System.out.println("here");
		doc = xml.build(new ByteArrayInputStream(sourceStr.getBytes()));
		root = doc.getRootElement().getChild(LTPTag.DOC);
		note = doc.getRootElement().getChild(LTPTag.NOTE);
		initIndex();
	}

	// *
	public void clear() {
		// xml = new SAXBuilder();
		doc = null;
		root = null;
		note = null;
		indexClear();
		sentenceNum = 0;
		paragraphNum = 1;
	}

	// */

	private boolean buildDOMFrame() throws JDOMException, IOException {
		Element rootEle = new Element(LTPTag.ROOT);

		Element noteEle = new Element(LTPTag.NOTE);
		noteEle.setAttribute(LTPTag.NOTE_SENT, "n");
		noteEle.setAttribute(LTPTag.NOTE_WORD, "n");
		noteEle.setAttribute(LTPTag.NOTE_POS, "n");
		noteEle.setAttribute(LTPTag.NOTE_NE, "n");
		noteEle.setAttribute(LTPTag.NOTE_PARSER, "n");
		noteEle.setAttribute(LTPTag.NOTE_WSD, "n");
		noteEle.setAttribute(LTPTag.NOTE_SRL, "n");
		rootEle.addContent(noteEle);

		rootEle.addContent(new Element(LTPTag.DOC));

		doc = new Document(rootEle);
		root = doc.getRootElement().getChild(LTPTag.DOC);
		note = doc.getRootElement().getChild(LTPTag.NOTE);

		indexClear();
		// doc.setRootElement();
		return true;
	}

	private void indexClear() {
		if (!pIndex.isEmpty()) {
			pIndex.clear();
		}
		// *
		if (!sIndex.isEmpty()) {
			sIndex.clear();
		}
		// */
	}

	private Format getFormat() {
		Format f = Format.getCompactFormat();
		f.setEncoding(encoding);
		// System.out.println("encoding: " + encoding);
		f.setIndent("  ");
		return f;
	}

	private void initIndex() {
		indexClear();

		for (int i = 0; i < root.getChildren().size(); ++i) {
			Element pele = (Element) root.getChildren().get(i);
			paraIndex paragraph = new paraIndex();
			// paragraph.sList = new ArrayList<sentIndex>();
			for (int j = 0; j < pele.getChildren().size(); ++j) {
				Element sele = (Element) pele.getChildren().get(j);
				sentIndex sentence = new sentIndex();
				// sentence.wList = new ArrayList<Word>();
				for (int p = 0; p < sele.getChildren().size(); ++p) {
					sentence.wList.add(new Word((Element) sele.getChildren()
					        .get(p)));
				}

				sentence.e = sele;

				paragraph.sList.add(sentence);
				sIndex.add(sentence);
			}
			paragraph.e = pele;
			pIndex.add(paragraph);
		}
	}

	/**
	 * Title: cloneWordList
	 * Description: word list deep copy
	 * 
	 * @param wordList
	 * @return
	 */
	private ArrayList<Word> cloneWordList(ArrayList<Word> wordList) {
		ArrayList<Word> alw = new ArrayList<Word>();
		if (wordList != null && !wordList.isEmpty()) {
			for (int i = 0; i < wordList.size(); ++i) {
				try {
					alw.add((Word) wordList.get(i).clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return alw;
	}

	/*
	 * public void build(String systemId) throws JDOMException, IOException {
	 * doc = xml.build(systemId); } //
	 */
	public void printXml() {
		XMLOutputter outputter = new XMLOutputter(getFormat());// Format.getPrettyFormat());
		try {
			// Document doc = new Document();
			outputter.output(doc, System.out);
			// outputter.output(doc, new FileOutputStream(new File("ltp.xml")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean hasSent() {
		return note.getAttributeValue(LTPTag.NOTE_SENT).equals("y");
	}

	public boolean hasWS() {
		return note.getAttributeValue(LTPTag.NOTE_WORD).equals("y");
	}

	public boolean hasPOS() {
		return note.getAttributeValue(LTPTag.NOTE_POS).equals("y");
	}

	public boolean hasNE() {
		return note.getAttributeValue(LTPTag.NOTE_NE).equals("y");
	}

	public boolean hasParser() {
		return note.getAttributeValue(LTPTag.NOTE_PARSER).equals("y");
	}

	public boolean hasWSD() {
		return note.getAttributeValue(LTPTag.NOTE_WSD).equals("y");
	}

	public boolean hasSRL() {
		return note.getAttributeValue(LTPTag.NOTE_SRL).equals("y");
	}

	/**
	 * <p>
	 * Title: SaveDom
	 * </p>
	 * <p>
	 * Description: save the dom tree into a file;
	 * </p>
	 * 
	 * @param filename
	 */
	public void saveDom(String filename) {
		XMLOutputter outputter = new XMLOutputter(getFormat());// Format.getPrettyFormat());
		try {
			outputter.output(doc, new FileOutputStream(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getXMLStr() {
		System.out.println("xml out putter:");
		XMLOutputter outputter = new XMLOutputter(getFormat());
		return outputter.outputString(doc);
	}

	/**
	 * <p>
	 * Title: getDom
	 * </p>
	 * <p>
	 * Description: get the dom tree;
	 * </p>
	 * 
	 * @return
	 */
	private Document getDom() {
		return doc;
	}

	/**
	 * <p>
	 * Title: CountParagraphInDocument
	 * </p>
	 * 
	 * @return the number of paragraphs in the document;
	 */
	public int countParagraph() {
		// return
		// doc.getRootElement().getChild(LTPTag.DOC).getChildren().size();
		return pIndex.size();
	}

	/**
	 * <p>
	 * Title: CountSentenceInParagraph
	 * </p>
	 * 
	 * @param paragraphIdx
	 * @return the number of sentences in a special paragraph
	 */
	public int countSentence(int paragraphIdx) {
		// return ((Element)
		// doc.getRootElement().getChild(LTPTag.DOC).getChildren("para").get(paragraphIdx)).getChildren().size();
		return pIndex.get(paragraphIdx).sList.size();
	}

	/**
	 * <p>
	 * Title: CountSentenceInDocument
	 * </p>
	 * 
	 * @return the number of sentences in the whole document;
	 */
	public int countSentence() {
		/*
		 * int counts = 0; List paras =
		 * doc.getRootElement().getChild(LTPTag.DOC).getChildren("para");
		 * for(int i=0; i<paras.size(); ++i){ counts += ((Element)
		 * paras.get(i)).getChildren().size(); } return counts; //
		 */
		return sIndex.size();
	}

	/**
	 * <p>
	 * Title: CountWordInSentence
	 * </p>
	 * <p>
	 * Description: according to the index of paragraph and sentence;
	 * </p>
	 * 
	 * @param paragraphIdx
	 * @param sentenceIdx
	 * @return the word number in a special sentence;
	 */
	private int countWordInSentence(int paragraphIdx, int sentenceIdx) {
		/*
		 * return ((Element) ( ((Element)(root.getChildren().get(paragraphIdx))
		 * ).getChildren().get(sentenceIdx) ) ).getChildren().size(); //
		 */
		return pIndex.get(paragraphIdx).sList.get(sentenceIdx).wList.size();
	}

	/**
	 * <p>
	 * Title: CountWordInSentence
	 * </p>
	 * <p>
	 * Description: according to the index of global sentence id;
	 * </p>
	 * 
	 * @param globalSentIdx
	 * @return the word number in a special sentence;
	 */
	private int countWordInSentence(int globalSentIdx) {
		/*
		 * List paras = root.getChildren("para"); for( int i= 0; i<paras.size();
		 * ++i){ List sents = ((Element)paras.get(i)).getChildren();
		 * if(globalSentIdx < sents.size()){ return
		 * ((Element)sents.get(globalSentIdx)).getChildren().size(); }else{
		 * globalSentIdx -= sents.size(); } } return 0; //
		 */
		return sIndex.get(globalSentIdx).wList.size();
	}

	// */

	/**
	 * <p>
	 * Title: CountWordInParagraph
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param paragraphIdx
	 * @return the word number in a special paragraph;
	 */
	private int countWordInParagraph(int paragraphIdx) {
		// *
		int counts = 0;
		List sents = ((Element) root.getChildren().get(paragraphIdx))
		        .getChildren();
		for (int i = 0; i < sents.size(); i++) {
			counts += ((Element) sents.get(i)).getChildren().size();
		}
		return counts;
		// */
	}

	/**
	 * <p>
	 * Title: CountWordInDocument
	 * </p>
	 * 
	 * @return the word number in the whole document;
	 */
	private int countWordInDocument() {
		int counts = 0;
		/*
		 * List paras = root.getChildren(); //
		 * System.out.println("paras size():" + paras.size()); for( int i= 0;
		 * i<paras.size(); ++i){ List sents =
		 * ((Element)paras.get(i)).getChildren("sent"); //
		 * System.out.println("	sents.size():" + sents.size()); for( int j= 0;
		 * j<sents.size(); ++j){ // System.out.println("		word.size():" +
		 * ((Element)sents.get(j)).getChildren("word").size()); counts +=
		 * ((Element)sents.get(j)).getChildren("word").size(); } } //
		 */
		for (int i = 0; i < sIndex.size(); ++i) {
			counts += sIndex.get(i).wList.size();
		}
		return counts;

	}

	/*
	 * public Paragraph getParagraph(int paragraphId){ return
	 * (Paragraph)root.getChildren().get(paragraphId); }
	 * 
	 * public List<Paragraph> getParagraphs(){ return
	 * (List<Paragraph>)root.getChildren(); } //
	 */
	/**
	 * <p>
	 * Title: getWord
	 * </p>
	 * <p>
	 * Description: according to the index of paragraph, sentence and word;
	 * </p>
	 * 
	 * @param paragraphId
	 * @param sentenceId
	 * @param wordId
	 * @return get the type of Word;
	 */
	private Word getWord(int paragraphId, int sentenceId, int wordId) {
		/*
		 * return new Word((Element) ((Element) ((Element)
		 * root.getChildren().get(
		 * paragraphId)).getChildren().get(sentenceId)).getChildren().get(
		 * wordId)); //
		 */
		Word wd = null;
		try {
			wd = (Word) (pIndex.get(paragraphId).sList.get(sentenceId).wList
			        .get(wordId).clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wd;
	}

	/**
	 * <p>
	 * Title: getWords
	 * </p>
	 * <p>
	 * Description: according to the index of paragraph and sentence;
	 * </p>
	 * 
	 * @param paragraphId
	 * @param sentenceId
	 * @return ArrayList<Word>;
	 */
	public ArrayList<Word> getWords(int paragraphId, int sentenceId) {
		/*
		 * List<Word> wordList = new LinkedList<Word>(); for( int i = 0;
		 * i<CountWordInSentence(paragraphId, sentenceId); ++i){
		 * wordList.add(getWord(paragraphId, sentenceId, i)); } return wordList;
		 * //
		 */
		return cloneWordList(pIndex.get(paragraphId).sList.get(sentenceId).wList);
	}

	/**
	 * <p>
	 * Title: getWord
	 * </p>
	 * <p>
	 * Description: according to the index of global word id; this method is not
	 * advocated;
	 * </p>
	 * 
	 * @param globalWordId
	 * @return get the type of Word;
	 */
	private Word getWord(int globalWordId) {
		/*
		 * List paras = root.getChildren("para"); for( int i= 0; i<paras.size();
		 * ++i){ List sents = ((Element)paras.get(i)).getChildren(); for( int j
		 * = 0; j<sents.size(); ++j){ List words =
		 * ((Element)sents.get(j)).getChildren(); if(globalWordId <
		 * words.size()){ return new Word((Element)words.get(globalWordId));
		 * }else{ globalWordId -= words.size(); } } } return null; //
		 */
		Word wd = null;
		for (int i = 0; i < sIndex.size(); ++i) {
			if (globalWordId < sIndex.get(i).wList.size()) {
				try {
					wd = (Word) sIndex.get(i).wList.get(globalWordId).clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			} else {
				globalWordId -= sIndex.get(i).wList.size();
			}
		}
		return wd;
	}

	/**
	 * <p>
	 * Title: getWordsFromSentence
	 * </p>
	 * <p>
	 * Description: according to the index of global sentence id;
	 * </p>
	 * 
	 * @param globalSentenceId
	 * @return ArrayList<Word>;
	 */
	public ArrayList<Word> getWords(int globalSentenceId) {
		/*
		 * List paras = root.getChildren("para");
		 * 
		 * for(int i=0; i<paras.size(); ++i){ int counts = ((Element)
		 * paras.get(i)).getChildren().size(); if( globalSentenceId <counts){
		 * Element sents= (Element)((Element)
		 * paras.get(i)).getChildren().get(globalSentenceId); List<Word>
		 * wordList = new LinkedList<Word>(); for(int j = 0;
		 * j<sents.getChildren().size(); ++j){ wordList.add(new
		 * Word((Element)sents.getChildren().get(j))); } return wordList; } }
		 * return null; //
		 */
		return cloneWordList(sIndex.get(globalSentenceId).wList);

	}

	/**
	 * <p>
	 * Title: getSentenceContent
	 * </p>
	 * <p>
	 * Description: according to the index of paragraph and sentence;
	 * </p>
	 * 
	 * @param paragraphIdx
	 * @param sentenceIdx
	 * @return the content of a sentence;
	 */
	public String getSentenceContent(int paragraphIdx, int sentenceIdx) {
		return ((Element) (((Element) (root.getChildren().get(paragraphIdx)))
		        .getChildren().get(sentenceIdx))).getAttributeValue("cont");
	}

	/**
	 * <p>
	 * Title: getSentenceContent
	 * </p>
	 * <p>
	 * Description: according to the index of global sentence id;
	 * </p>
	 * 
	 * @param globalSentIdx
	 * @return the content of a sentence;
	 */
	public String getSentenceContent(int globalSentIdx) {
		for (int i = 0; i < countParagraph(); ++i) {
			if (globalSentIdx < countSentence(i)) {
				return getSentenceContent(i, globalSentIdx);
			} else {
				globalSentIdx -= countSentence(i);
			}
		}
		return null;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void addSentence(ArrayList<Word> wordList, int paragraphIdx)
	        throws JDOMException, IOException {
		if (wordList.isEmpty()) {
			return;
		}

		if (sentenceNum == -1) {
			throw new IllegalArgumentException(
			        "AddSentence error: Had been set over, can not be write again");
		} else if (sentenceNum == 0) {
			buildDOMFrame();
			Word w = wordList.get(0);
			note.setAttribute(LTPTag.NOTE_SENT, "y");
			if (w.hasWS()) {
				note.setAttribute(LTPTag.NOTE_WORD, "y");
			}
			if (w.hasPOS()) {
				note.setAttribute(LTPTag.NOTE_POS, "y");
			}
			if (w.hasNE()) {
				note.setAttribute(LTPTag.NOTE_NE, "y");
			}
			if (w.hasWSD()) {
				note.setAttribute(LTPTag.NOTE_WSD, "y");
			}
			if (w.hasParser()) {
				note.setAttribute(LTPTag.NOTE_PARSER, "y");
			}

			for (int i = 0; i < paragraphNum; ++i) {
				Element pele = new Element(LTPTag.PARA);
				pele.setAttribute(LTPTag.ID, "" + i);
				root.addContent(pele);

				paraIndex paragraph = new paraIndex();
				paragraph.e = pele;
				// paragraph.sList = new ArrayList<sentIndex>();
				pIndex.add(paragraph);
			}
			// printXml();
		} else if (true == hasSent() && false == hasWS() && false == hasPOS()
		        && false == hasNE() && false == hasWSD() && false == hasSRL()) {
			throw new IllegalArgumentException(
			        "AddSentence error: Only sentence content can be set, there is no other attribute can be done");
		}

		// System.out.println(paragraphNum + "     " + root.getContentSize());
		if (paragraphIdx >= root.getContentSize()) {
			throw new IndexOutOfBoundsException(
			        "AddSentence error: Paragraph id is out of the range!");
		}

		Element pele = (Element) root.getChildren().get(paragraphIdx);
		Element sentEle = new Element(LTPTag.SENT);
		String contents = "";
		sentIndex sid = new sentIndex();
		sid.e = sentEle;
		// sid.wList = new ArrayList<Word>();

		for (int i = 0; i < wordList.size(); ++i) {
			if (checkWordMatch(wordList.get(i)) == false) {
				throw new IllegalArgumentException(
				        "AddSentence error: Some word's attribute missed!");
			}

			Element wEle = new Element(LTPTag.WORD);
			wEle.setAttribute(LTPTag.ID, "" + i);
			if (hasWS()) {
				wEle.setAttribute(LTPTag.CONT, wordList.get(i).getWS());
				contents += wEle.getAttributeValue(LTPTag.CONT);
			}
			if (hasPOS()) {
				wEle.setAttribute(LTPTag.POS, wordList.get(i).getPOS());
			}
			if (hasNE()) {
				wEle.setAttribute(LTPTag.NE, wordList.get(i).getNE());
			}
			if (hasParser()) {
				wEle.setAttribute(LTPTag.PSR_PARENT, ""
				        + wordList.get(i).getParserParent());
				wEle.setAttribute(LTPTag.PSR_RELATE, wordList.get(i)
				        .getParserRelation());
			}
			if (hasWSD()) {
				wEle.setAttribute(LTPTag.WSD, wordList.get(i).getWSD());
				wEle.setAttribute(LTPTag.WSD_EXP, wordList.get(i)
				        .getWSDExplanation());
			}
			// set it on the dom tree
			sentEle.addContent(wEle);
			// set it on the index
			sid.wList.add(new Word(wEle));
		}

		sentEle.setAttribute(LTPTag.ID, "" + pele.getChildren().size());
		sentEle.setAttribute(LTPTag.CONT, contents);
		// pele.addContent(sentEle);

		pIndex.get(paragraphIdx).sList.add(sid);
		pIndex.get(paragraphIdx).e.addContent(sentEle);
		sIndex.add(sid);

		++sentenceNum;
	}

	public void addSentence(String sentenceContent, int paragraphIdx)
	        throws JDOMException, IOException {
		if (sentenceNum == -1) {
			throw new IllegalArgumentException(
			        "AddSentence error: Had been set over, can not be write again");
		} else if (sentenceNum == 0) {
			buildDOMFrame();

			note.setAttribute(LTPTag.NOTE_SENT, "y");

			for (int i = 0; i < paragraphNum; ++i) {
				Element pele = new Element(LTPTag.PARA);
				pele.setAttribute(LTPTag.ID, "" + i);
				root.addContent(pele);

				paraIndex paragraph = new paraIndex();
				paragraph.e = pele;
				// paragraph.sList = new ArrayList<sentIndex>();
				pIndex.add(paragraph);
			}
		} else if (true == hasSent()
		        && (true == hasWS() || true == hasPOS() || true == hasNE()
		                || true == hasWSD() || true == hasSRL())) {
			throw new IllegalArgumentException(
			        "AddSentence error: Only sentence content can be set, there is no other attribute can be done");
		}

		if (paragraphIdx >= root.getContentSize()) {
			throw new IndexOutOfBoundsException(
			        "AddSentence error: Paragraph id is out of the range!");
		}

		Element sentEle = new Element(LTPTag.SENT);
		sentEle.setAttribute(LTPTag.ID,
		        "" + pIndex.get(paragraphIdx).sList.size());
		sentEle.setAttribute(LTPTag.CONT, sentenceContent);

		sentIndex sentence = new sentIndex();
		sentence.e = sentEle;
		// sentence.wList = new ArrayList<Word>();

		pIndex.get(paragraphIdx).e.addContent(sentEle);
		pIndex.get(paragraphIdx).sList.add(sentence);
		sIndex.add(sentence);

		++sentenceNum;
	}

	public void setParagraphNumber(int paragraphNumber) {
		if (sentenceNum != 0 || paragraphNumber < 1) {
			throw new IllegalArgumentException(
			        "setParagraphNumber error: Paragraph number can not be set again!");
		}
		paragraphNum = paragraphNumber;
	}

	public void setOver() {
		sentenceNum = -1;
	}

	private boolean checkWordMatch(Word w) {
		if (hasWS() == true && w.hasWS() == false) {
			return false;
		}
		if (hasPOS() == true && w.hasPOS() == false) {
			return false;
		}
		if (hasNE() == true && w.hasNE() == false) {
			return false;
		}
		if (hasParser() == true && w.hasParser() == false) {
			return false;
		}
		if (hasWSD() == true && w.hasWSD() == false) {
			return false;
		}
		return true;
	}
}

// wList not need to be initialized
class sentIndex {
	Element e;
	ArrayList<Word> wList = new ArrayList<Word>();
}

// sList not need to be initialized
class paraIndex {
	Element e;
	ArrayList<sentIndex> sList = new ArrayList<sentIndex>();
}