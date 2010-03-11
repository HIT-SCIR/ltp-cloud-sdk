#include "LTML.h"
namespace HIT_IR_LTP{
	LTML::LTML()
		:xml()
	{
	}

	bool LTML::SetEncoding(const string& encodeType){
		xml.SetEncoding(encodeType);
		return true;
	}

	std::string LTML::GetXMLStr() const
	{
		std::string xmlStr;
		xml.SaveDOM(xmlStr);
		return xmlStr;
	}

	bool LTML::IsAnalyzed()
	{
		return false;
	}

	bool LTML::HasSent() const
	{
		return xml.QueryNote(LTPOption.NOTE_SENT);
	}

	bool LTML::HasWS() const
	{
		return xml.QueryNote(LTPOption.NOTE_WORD);
	}

	bool LTML::HasPOS() const
	{
		return xml.QueryNote(LTPOption.NOTE_POS);

	}

	bool LTML::HasNE() const
	{
		return xml.QueryNote(LTPOption.NOTE_NE);

	}

	bool LTML::HasParser() const
	{
		return xml.QueryNote(LTPOption.NOTE_PARSER);
	}

	bool LTML::HasWSD() const
	{
		return xml.QueryNote(LTPOption.NOTE_WSD);
	}

	bool LTML::HasSRL() const
	{
		return xml.QueryNote(LTPOption.NOTE_SRL);
	}

	void LTML::SetOver()
	{
		xml.SetOver();
	}

	LTML::~LTML()
	{
		xml.ClearDOM();
	}

	void LTML::InitNull()
	{
		//xml.InitXmlStructure();
		std::string ltmlNull;
		ltmlNull.append("<?xml version=\"1.0\" encoding=\"gbk\" ?>\
			<xml4nlp><note sent=\"n\" word=\"n\" pos=\"n\" ne=\"n\" parser=\"n\" wsd=\"n\" srl=\"n\" />\
				<doc></doc></xml4nlp>");
		//ltmlNull.append();
		xml.LoadXMLFromString(ltmlNull);
	}
}
