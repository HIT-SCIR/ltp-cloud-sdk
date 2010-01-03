#include "Word.h"

namespace HIT_IR_LTP{
	Word::Word( const TiXmlElement& e )
	:ele(e)
	{
		
	}

	Word::Word( const Word& w )
	:ele(w.ele)
	{
	}

	Word::Word()
	:ele("word")
	{
	}
	void Word::operator=( const Word& w )
	{
		this->ele = w.ele;
	}

	Word::~Word()
	{
		ele.Clear();
	}

	int Word::GetID() const
	{
		if(ele.Attribute("id")!=NULL)
		{
			return atoi(ele.Attribute("id"));
		}
		return -1;
	}

	std::string Word::GetWS() const
	{
		/*
		if (ele.Attribute("cont")!=NULL)
		{
			content = ele.Attribute("cont");
			return true;
		}
		//*/
		if (!HasWS())
		{
			return "";
		}
		return ele.Attribute("cont");
	}

	std::string Word::GetPOS() const
	{
		/*
		if (ele.Attribute("pos")!=NULL)
		{
			pos = ele.Attribute("pos");
			return true;
		}
		//*/
		if (!HasPOS())
		{
			return "";
		}
		return ele.Attribute("pos");

	}

	std::string Word::GetNE() const
	{
		/*
		if (ele.Attribute("ne")!=NULL)
		{
			ne = ele.Attribute("ne");
			return true;
		}
		//*/
		if (!HasNE())
		{
			return "";
		}
		return ele.Attribute("ne");
	}

	std::string Word::GetWSD() const
	{
		/*
		if (ele.Attribute("wsd")!=NULL)
		{
			wsd = ele.Attribute("wsd");
			return true;
		}
		//*/
		if (!HasWSD())
		{
			return "";
		}
		return ele.Attribute("wsd");
	}

	std::string Word::GetWSDExplanation() const
	{
		/*
		if (ele.Attribute("wsdexp")!=NULL)
		{
			explanation = ele.Attribute("wsdexp");
			return true;
		}
		//*/
		if (!HasWSD())
		{
			return "";
		}
		return ele.Attribute("wsdexp");
	}

	//************************************
	// Method:    getParseParent
	// FullName:  Word::getParseParent
	// Access:    public 
	// Returns:   int
	// Qualifier: when the parent is null, return -3;
	//************************************
	int Word::GetParserParent() const
	{
		if(ele.Attribute("parent")!=NULL)
		{
			return atoi(ele.Attribute("parent"));
		}
		return -3;
	}

	std::string Word::GetParserRelation() const
	{
		/*
		if (ele.Attribute("relate")!=NULL)
		{
			relation = ele.Attribute("relate");
			return true;
		}
		//*/
		if (!HasParser())
		{
			return "";
		}
		return ele.Attribute("relate");
	}

	bool Word::IsPredicate() const
	{
		if(ele.FirstChildElement("arg")!=NULL)
		{
			return true;
		}
		return false;
	}

	bool Word::GetSRLs( std::vector<SRL> &srls )
	{
		TiXmlElement *argPtr = NULL;
		if (( argPtr = ele.FirstChildElement("arg"))!=NULL)
		{
			SRL srlSingle = {
				argPtr->Attribute("type")
				, atoi(argPtr->Attribute("beg"))
				, atoi(argPtr->Attribute("end"))
			};
			srls.push_back(srlSingle);

			while((argPtr = argPtr->NextSiblingElement("arg")) != NULL)
			{
				SRL srlSingleTmp = {
					argPtr->Attribute("type")
					, atoi(argPtr->Attribute("beg"))
					, atoi(argPtr->Attribute("end"))
				};
				srls.push_back(srlSingleTmp);
			}
			return true;
		}
		return false;
	}

	void Word::SetID( int id )
	{
		ele.SetAttribute("id", id);
	}

	void Word::SetWS( const std::string& content )
	{
		ele.SetAttribute("cont", content.c_str());

	}

	void Word::SetPOS( const std::string& pos )
	{
		ele.SetAttribute("pos", pos.c_str());
	}

	void Word::SetNE( const std::string& ne )
	{
		ele.SetAttribute("ne", ne.c_str());
	}

	void Word::SetWSD( const std::string& wsd, const std::string& explanation )
	{
		ele.SetAttribute("wsd", wsd.c_str());
		ele.SetAttribute("wsdexp",explanation.c_str());
	}

	void Word::SetParser( int parent, const std::string& relation )
	{
		ele.SetAttribute("parent", parent);
		ele.SetAttribute("relate", relation.c_str());
	}

	void Word::SetSRLs( const std::vector<SRL> &srls )
	{
		int i = 0;
		for(std::vector<SRL>::const_iterator iter = srls.begin(); 
			iter != srls.end(); 
			++iter){
			TiXmlElement srlcell("arg");
			srlcell.SetAttribute("id", i);
			srlcell.SetAttribute("type", iter->type.c_str());
			srlcell.SetAttribute("beg", iter->beg);
			srlcell.SetAttribute("end", iter->end);
			++i;
		}
	}

	bool Word::HasID() const
	{
		if(ele.Attribute("id") != NULL){
			return true;
		}
		return false;
	}

	bool Word::HasWS() const
	{
		if(ele.Attribute("cont") != NULL){
			return true;
		}
		return false;

	}

	bool Word::HasPOS() const
	{
		if(ele.Attribute("pos") != NULL){
			return true;
		}
		return false;
	}

	bool Word::HasNE() const{
		if(ele.Attribute("ne") != NULL){
			return true;
		}
		return false;
	}

	bool Word::HasWSD() const
	{
		if (ele.Attribute("wsd") != NULL && ele.Attribute("wsdexp") != NULL){
			return true;
		}
		return false;
	}

	bool Word::HasParser() const
	{
		if(ele.Attribute("parent") != NULL && ele.Attribute("relate") != NULL){
			return true;
		}
		return false;
	}
}
