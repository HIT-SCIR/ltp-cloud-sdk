LTP Web Servic使用文档v1.0
==========================

#### 作者

* 徐伟杰 2013年8月9日创建文档

版权所有：哈尔滨工业大学社会计算与信息检索研究中心

## 目录

# LTP简介

语言技术平台(Language Technology Platform，LTP)是哈工大社会计算与信息检索研究中心历时十年开发的一整套中文语言处理系统。LTP制定了基于XML的语言处理结果表示，并在此基础上提供了一整套自底向上的丰富而且高效的中文语言处理模块(包括词法、句法、语义等6项中文处理核心技术)，以及基于动态链接库(Dynamic Link Library, DLL)的应用程序接口，可视化工具，并且能够以网络服务(Web Service)的形式进行使用。

从2006年9月5日开始该平台对外免费共享目标代码，截止目前，已经有国内外400多家研究单位共享了LTP，也有国内外多家商业公司购买了LTP，用于实际的商业项目中。2010年12月获得中国中文信息学会颁发的行业最高奖项：“钱伟长中文信息处理科学技术奖”一等奖。

2011年6月1日，为了与业界同行共同研究和开发中文信息处理核心技术，我中心正式将LTP开源。
#LTP Web Service
LTP提供针对自然语言处理任务，基于云端的编程接口。用户可以通过使用LTP Web Service Client调用云端服务。

* 免安装：用户只需要下载LTP Web Service客户端源代码，编译执行后即可获得分析结果，无需调用静态库或下载模型文件。
* 硬件：LTP Web Service Client几乎可以运行于任何硬件配置的计算机上，用户不需要购买高性能的机器，即可快捷的获得分析结果。
* 跨平台跨语言：LTP Web Service客户端几乎可以运行于任何操作系统之上，无论是Windows、Linux各个发行版或者Mac OS。
* 跨编程语言：时至今日，LTP Web Service Client已经提供了包括C++，Java，C#在内的编程接口，其他语言的编程接口也在开发之中。
在运算资源有限，编程语言受限的情况下，Web Service无疑是用户使用LTP更好的选择。

#获得Service Client

作为安装的第一步，你需要获得LTP Web Service Client。你可以从下面链接获得最新的LTP Web Service Client。

* Github项目托管：https://github.com/HIT-SCIR/ltp-service

里面有C#，JAVA，C++，Python，Ruby版本的Client。你可以根据自己的习惯选择相应版本的Client。各部分的编译方式下面会分版本介绍。

#原理以及数据表示

在ltp web service中，client与server之间采用http协议通信。client以post方式提交的数据到server，server将数据以xml的方式返回给client。

client在提交数据时，首先需要在http请求头部中添加用户名密码以做验证。

client提交的post请求主要有以下几个字段。

|字段名 | 含义 |
| ------ | ---- |
| s | 输入字符串，在xml选项x为n的时候，代表输入句子；为y时代表输入xml |
| x | 用以指明是否使用xml |
| c | 用以指明输入编码方式 |
| t | 用以指明分析目标，t可以为分词（ws）,词性标注（pos），命名实体识别（ner），依存句法分析（dp），语义角色标注（srl）或者全部任务（all）|

当用户提交请求成功后，服务器端返回相应的分析结果（XML串），返回结果如下图所示。
```xml
<?xml version="1.0" encoding=" gbk " ?>
<xml4nlp>
    <note sent="y" word="y" pos="y" ne="y" parser="y" wsd="y" srl="y" />
    <doc>
        <para id="0">
            <sent id="0" cont="我们都是中国人">
                <word id="0" cont="我们" wsd="Aa02" wsdexp="我_我们" pos="r" ne="O" parent="2" relate="SBV" />
                <word id="1" cont="都" wsd="Ka07" wsdexp="都_只_不止_甚至" pos="d" ne="O" parent="2" relate="ADV" />
                <word id="2" cont="是" wsd="Ja01" wsdexp="是_当做_比作" pos="v" ne="O" parent="-1" relate="HED">
                    <arg id="0" type="A0" beg="0" end="0" />
                    <arg id="1" type="AM-ADV" beg="1" end="1" />
                </word>
                <word id="3" cont="中国" wsd="Di02" wsdexp="国家_行政区划" pos="ns" ne="S-Ns" parent="4" relate="ATT" />
                <word id="4" cont="人" wsd="Aa01" wsdexp="人_人民_众人" pos="n" ne="O" parent="2" relate="VOB" />
            </sent>
        </para>
    </doc>
</xml4nlp>

```
服务器的默认编码方式为gbk。LTP数据表示标准称为LTML。LTML标准要求如下：
结点标签分别为xml4nlp, note, doc, para, sent, word, arg共七种结点标签；

1.    xml4nlp为根结点，无任何属性值；
2.	note为标记结点，具有的属性分别为：sent, word, pos, ne, parser, wsd, srl；分别代表分句，分词，词性标注，命名实体识别，依存句法分析，词义消歧，语义角色标注；值为”n”，表明未做，值为”y”则表示完成，如pos=”y”，表示已经完成了词性标注；
3.	doc为篇章结点，以段落为单位包含文本内容；无任何属性值；
4.	para为段落结点，需含id属性，其值从0开始；
5.	sent为句子结点，需含属性为id，cont；id为段落中句子序号，其值从0开始；cont为句子内容；
6.	word为分词结点，需含属性为id, cont；id为句子中的词的序号，其值从0开始，cont为分词内容；可选属性为pos, ne, wsd, wsdexp, parent, relate:
    1.  pos的内容为词性标注内容；
    * ne为命名实体内容；
    * wsd与wsdexp成对出现，wsd为词义消歧内容，wsdexp为相应的解释说明
    * parent与relate成对出现，parent为依存句法分析的父亲结点id号，relate为相对应的关系；
    * arg为语义角色信息结点，任何一个谓词都会带有若干个该结点；其属性为id, type, beg, end；id为序号，从0开始；type代表角色名称；beg为开始的词序号，end为结束的序号；

各结点及属性的逻辑关系说明如下：

1.    各结点层次关系可以从图中清楚获得，凡带有id属性的结点是可以包含多个；
2.	如果sent=”n”即未完成分句，则不应包含sent及其下结点；
3.	如果sent=”y” word=”n”即完成分句，未完成分词，则不应包含word及其下结点；
4.	其它情况均是在sent=”y” word=”y”的情况下：
    1. 如果pos=”y”则分词结点中必须包含pos属性；
    * 如果ne=”y”则分词结点中必须包含ne属性；
    * 如果parser=”y”则分词结点中必须包含parent及relate属性；
    * 如果wsd=”y”则分词结点中必须包含wsd及wsdexp属性；
    * 如果srl=”y”则凡是谓词（predicate）的分词会包含若干个arg结点；

#C++ 版Client
LTP需要与服务器进行交互，以完成对文本的分析，分析的返回结果存储在以DOM形式组织的XML中，客户端接到分析结果后可通过提供的接口对结果进行分析。

C++ 接口的操作可分为如下两个类：
* LTPService类为与服务器交互类，负责验证、连接分析参数设置等；
* LTML类为返回数据(XML)解析函数，专门负责数据的生成和提取； 

LTP的C++各类均定义在命名空间HIT_IR_LTP中。因此在书写程序时需包含：
using namespace HIT_IR_LTP;

##编译说明

无论[WINDOWS] 还是[LINUX]，首先必需修改头文件LTPOption.h（位于__ltpService目录下），注释掉系统LINUX_OS或WIN_OS；

* [WINDOWS]

在LTPOption.h中，注释掉“#define LINUX_OS”,保留“#define WIN_OS”；
开发IDE为Microsoft Visual Studio 2008，值得注意的是项目中只能有一个main函数入口，在给的若干个例程中（Example）都包含有main函数；

* [LINUX]

在LTPOption.h中，注释掉“#define WIN_OS”,保留“#define LINUX_OS”；
	安装LINUX下所需的库，运行如下命令：
```
./configure
make
make install

```
编译例程，命令如下：
```
g++ example1.cpp -I /usr/ local/include \
-L /usr/ local/lib -lutil -lxml4nlp –lservice \
-o test
```
##主要函数接口
###LTPService类
LTPService.h位于__ltpService内；
该类主要负责与服务器交互，并将返回结果以Ltml对象返回。

**LTPService**

功能：

构造函数，初始化用户信息

参数：

| 参数名 | 参数描述 |
|-------|----------|
| const std::string& authorization | 用户验证信息，信息格式为：”username:password” |

**bool SetEncoding**

功能：

设置字符编码。

参数：

| 参数名 | 参数描述 |
|-------|----------|
| const std::string& encodingType | 默认为gbk（LTPOption.GBK），目前仅支持UTF-8(LTPOption.UTF8)及GBK，GB2312。编码的定义请参考LTPOption |

返回值：

成功返回true，失败返回false

**bool Analyze**

功能：

发送字符串类型待分析信息，得到服务器分析结果。

参数：

| 参数名 | 参数描述 |
|-------|----------|
| const std::string& option | 分析方式，分析的方式LTPOption.xxx。可参阅LTPOption类 |
|const std::string& analyzeString|待分析的字串|
|LTML& ltml_out|保存分析结果，为一个LTML类|

返回值：

成功返回true，失败返回false

**bool Analyze**

功能：

发送LTML类型待分析信息，得到服务器分析结果。也可以根据自己的需求完成部分中文信息处理，保存在LTML类里，让服务器帮你完成其他任务。

参数：

| 参数名 | 参数描述 |
|-------|----------|
|const std::string& option|分析方式，分析的方式LTPOption.xxx。可参阅LTPOption类|
|const LTML& ltml_in|待分析的信息，为LTML类|
|LTML& ltml_out|保存分析结果，为一个LTML类|

返回值：

成功返回true，失败返回false

###LTML类
LTML.h位于__ltpService内；
LTML类提供XML操作方法，包括XML的生成，XML中信息的提取。
该类是对返回的数据(XML串)进行解析的主要对象。

**bool GetWords** 

功能：

提取LTML分析结果

参数：

| 参数名 | 参数描述 |
|-------|----------|
|std::vector<Word> &wordList|分析结果序列，保存在Word类里|
|int sentenceIdx|选择提取的句子序号|

返回值：

成功返回true，失败返回false

**bool GetSentenceContent**

功能：

提取分句结果

参数：

|参数名 | 参数描述 |
|-------|----------|
| string &content | 分析结果序列，保存在Word类里|
|paragraphIdx|选择提取的段落号|
|int sentenceIdx|选择提取的句子号|

返回值：

成功返回true，失败返回false

以下几个方法是向LTML对象写数据的方法。注意以下几个问题：

1.    请保证调用以下方法的LTML对象为空的，或首先调用过ClearDOM方法，以保证LTML对象中数据一致；
2.	输入的数据必须保证一致，例如，如果第一个词完成了词性标注，则其余词也须完成词性标注，因为LTML对象是根据第一个词或第一句话生成的note结点；

已经调用SetOver的LTML对象不允许调用以下方法，否则会抛异常；凡是由LTPService对象返回的Ltml对象都调用过SetOver方法；

**bool AddSentence**

功能：

在LTML中写入句子。输入完成部分分析的单词序列

参数：

|参数名 | 参数描述 |
|-------|----------|
| const vector<Word> &wordList | 插入的组成句子的单词序列。其中note结点是根据第一个sentence第一个词生成的，必须保证后面句子中的词与第一个词一致，否则会抛异常。|
|int paragraphId|选择插入句子的段落号|

返回值：

成功返回true，失败返回false

**bool AddSentence**

功能：

在LTML中写入句子。输入尚未做分词的字串

参数：

|参数名 | 参数描述 |
|-------|----------|
| const std::string sentenceContent| 插入的组成句子的单词序列。其中note结点是根据第一个sentence第一个词生成的，必须保证后面句子中的词与第一个词一致，否则会抛异常。|
|int paragraphId|选择插入句子的段落号|

返回值：

成功返回true，失败返回false

###LTMLOption
作为全局常量定义了分析方式类型

|选项名 | 含义述 |
|-------|--------|
|LTPOption.WS|分词|
|LTPOption.POS|词性标注|
|LTPOption. NE|命名实体识别|
|LTPOption.WSD|词义消歧|
|LTPOption. PARSER|依存句法分析|
|LTPOption.SRL|语义角色标注|


##C++调用示例

* 用例一：发送string类型的分析对象，得到分析结果，并将结果按分词、ID、词性、命名实体、依存关系、词义消歧、语义角色标注的顺序输出。

```cpp
using namespace HIT_IR_LTP;
int main(){
    LTPService ls("username:password");
	LTML ltml;
	if (!ls.Analyze(LTPOption.ALL,"我们都是赛尔人。", ltml)) {
		cerr<<"Authorization is denied!"<<endl;
		exit(EXIT_FAILURE);
	}
	int sentNum = ltml.CountSentence();
	for ( int i = 0; i<sentNum; ++i) { 
		string sentCont;
		ltml.GetSentenceContent(sentCont, i);
		cout<< sentCont <<endl;
		vector<Word> wordList;
		ltml.GetWords(wordList, i);
		//按句子打印输出
		for( vector<Word>::iterator iter = wordList.begin(); iter!= wordList.end(); ++iter ){
			cout<<iter->GetWS()<<"\t"<<iter->GetID();
			cout<<"\t"<<iter->GetPOS();
			cout<<"\t"<<iter->GetNE();
			cout<<"\t"<<iter->GetParserParent()<<"\t"<<iter->GetParserRelation();
			cout<<"\t"<<iter->GetWSD()<<"\t"<<iter->GetWSDExplanation();
			cout<<endl;
			if( iter->IsPredicate() ){
				vector<SRL> srls;
				iter->GetSRLs(srls);
				for(vector<SRL>::iterator iter = srls.begin(); iter != srls.end(); ++iter){
					cout<<"\t"<<iter->type
						<<"\t"<<iter->beg
						<<"\t"<<iter->end
						<<endl;
				}
			}
		}
	}
	return 0;
}

```
该用例首先实例化一个新的Service Client对象，用户名和密码被保存在这个对象中。然后，client发起一个请求，并指明分析目标位分词。请求结果返回并保存在一个LTML对象中。

* 用例二：将待分析数据保存在LTML类中，发送LTML类型的分析对象得到分析结果。用例首先进行分词，将所得的词按用户词表进行合并或拆分，并对其进行依存句法分析。本例中将“午夜”与“巴赛罗那”进行了合并。

```cpp
using namespace HIT_IR_LTP;
int main(){
    LTPService ls("username:password");
	LTML ltmlBeg;
	try{
		if(!ls.Analyze(LTPOption.WS, "午夜巴塞罗那是对爱情的一次诙谐、充满智慧、独具匠心的冥想。", ltmlBeg)) {
			cerr<<"Authorization is denied!"<<endl;
			exit(EXIT_FAILURE);
		}	
		vector<Word> wordList;
		ltmlBeg.GetWords(wordList, 0);
		//输出分词结果
		for( vector<Word>::iterator iter = wordList.begin(); iter!= wordList.end(); ++iter )
		{
			cout<<iter->GetID()<<"\t"<<iter->GetWS()<<endl;
		}
		cout<<endl;

		//将“午夜”与“巴赛罗那”合并，其它的词不变
		vector<Word> mergeList;
		Word mergeWord;
		mergeWord.SetWS(wordList.at(0).GetWS() + wordList.at(1).GetWS());
		mergeList.push_back(mergeWord);

		for (vector<Word>::iterator iter = wordList.begin()+2; iter != wordList.end(); ++iter)
		{
			Word others;
			others.SetWS(iter->GetWS());
			mergeList.push_back(others);
		}
		LTML ltmlSec;
		ltmlSec.AddSentence(mergeList, 0);
		ltmlSec.SetOver();
		LTML ltmlOut;
		ls.Analyze(LTPOption.PARSER, ltmlSec, ltmlOut);

		//输出合并分词后PARSER结果
		cout<<"merge and get parser results."<<endl;
		vector<Word> outList;
		ltmlOut.GetWords(outList,0);
		for (vector<Word>::iterator iter = outList.begin(); iter != outList.end(); ++iter)
		{
	cout<<iter->GetID()<<"\t"<<iter->GetWS()<<"\t"<<iter->GetPOS()<<"\t"<<iter->GetParserParent()<<"\t"<<iter->GetParserRelation()<<endl;				
		}
		cout<<endl;
	}catch(exception& e){
		std::cerr<<e.what();
	}
	return 0;
}

```
该用例首先将原始句子发出，返回得到一个ltml的结果。将结果中的单词合并放到一个wordlist中。同时建立一个空的ltml类，将wordlist内容填入ltml类里面。最后再次发回，得到结果。

#JAVA 版Client

LTP需要与服务器进行交互，以完成对文本的分析，分析的返回结果存储在以DOM形式组织的XML中，客户端接到分析结果后可通过提供的接口对结果进行分析。

需要的工具包：

*    Httpclient.jar
*	Httpcore.jar
*	Jdom.jar（1.1或更高）
*	Commons-logging.jar

Java 接口的操作主要有以下两个类：

*	LTPService类为与服务器交互类，负责验证、连接分析参数设置等；
*	Ltml类为返回数据(XML)解析函数，专门负责数据的提取；

##编译说明

*   eclipse编译：

第一步导入库函数：

右击你创建的工程的根目录，点击Properties进入Properties, 在Properties页面中选中Java Build Path，选中Libraries标签，点击Add External JARs。选中LTPService.jar即可。

***缺啊图片***

第二步编译：

在文件头部import相应的类，既能顺利编译

* 命令行编译：
    * 由于需要将LTPServer.jar临时添加到你的环境变量中，需要增加参数 
cp /path1/xxx.jar:/path2/xxx.jar  添加的jar包linux下用用”:”隔开，windows下用”;”隔开。符号”.”表示当前路径，在运行的时候需要将当前路径添加到环境变量中。
编译  ：
```
 javac -cp /…/LTPServer.jar /…/xxxx.java
```
运行 ：
```
 java  -cp .:/…/LTPServer.jar /…/xxxx
```
    * 若用户因为没有安装Httpclient.jar，Httpcore.jar，Jdom.jar，Commons-logging.jar这几个工具包引发错误，我们在/JAVA/ tools package/文件夹中为你提供了相应的jar包。可以自己根据缺失情况按照上述方式引用。
    * 若用户想要修改重新编译再制作成jar包可以按照以下步骤操作：
        1. 如当前在JAVA/下，带编译文件在JAVA/edu/hit/ir/ltpService/目录，在需要引入工具包的情况下编译：```javac –cp ./ tools package/*  /edu/hit/ir/ltpService/*.java```
        2. 按照edu/hit/ir/ltpService/层次建立文件夹，将上述步骤中生成的*.class文件移入你建立的ltpService文件夹中。                               
        3. 执行：```jar cvf LTPService.jar edu/```即可生成LTPService.jar 工具包

##主要函数接口

###LTPService类

位于edu.hit.ir.ltpService .LTPService
该类主要负责与服务器交互，并将返回结果以Ltml对象返回。

**LTPService**

功能：

构造函数，初始化用户信息。

参数：

|参数名 | 参数描述 |
|-------|----------|
| String authorization | 用户验证信息，信息格式为：”username:password”|

**bool SetEncoding**

功能：

设置字符编码。

参数：

|参数名 | 参数描述 |
|-------|----------|
| String encodeType | 待销毁的词性标注器的指针|

返回值：

销毁成功时返回0，否则返回-1

**LTML Analyze**

功能：

发送字符串类型待分析信息，得到服务器分析结果。

参数：

|参数名 | 参数描述 |
|-------|----------|
| String option | 分析方式，分析的方式LTPOption.xxx。可参阅LTPOption类|
|String analyzeString|待分析的字串|

返回值：

返回分析结果，为一个LTML类

**LTML Analyze**

功能：

发送LTML类型待分析信息，得到服务器分析结果。也可以根据自己的需求完成部分中文信息处理，保存在LTML类里，让服务器帮你完成其他任务。

参数：

|参数名 | 参数描述 |
|-------|----------|
| String option | 分析方式，分析的方式LTPOption.xxx。可参阅LTPOption类|
|LTML ltmlIn|待分析的信息，为LTML类|
返回值：

返回分析结果，为一个LTML类

###LTML类

位于edu.hit.ir.ltpService.LTML
该类是对返回的数据(xml)进行解析的主要对象。

**ArrayList<Word>  getWords**

功能：

提取LTML分析结果。

参数：

|参数名 | 参数描述 |
|-------|----------|
| int paragraphId | 选择提取的段落序号|
|int sentenceIdx|选择提取的句子序号|
返回值：

提取的LTML分析结果。

**String getSentenceContent**

功能：

提取分句结果。

参数：

|参数名 | 参数描述 |
|-------|----------|
| int paragraphIdx | 选择提取的段落号针|
|int sentenceIdx|选择提取的句子号|

返回值：

提取的分句结果

以下几个方法是向LTML对象写数据的方法。注意以下几个问题：

1.    请保证调用以下方法的LTML对象为空的，或首先调用过ClearDOM方法，以保证LTML对象中数据一致；
2.	输入的数据必须保证一致，例如，如果第一个词完成了词性标注，则其余词也须完成词性标注，因为LTML对象是根据第一个词或第一句话生成的note结点；

已经调用SetOver的LTML对象不允许调用以下方法，否则会抛异常；凡是由LTPService对象返回的Ltml对象都调用过SetOver方法；

**void addSentence**

功能：

在LTML中写入句子。输入完成部分分析的单词序列。

参数：

|参数名 | 参数描述 |
|-------|----------|
| ArrayList<Word> wordList | 插入的组成句子的单词序列。其中note结点是根据第一个sentence第一个词生成的，必须保证后面句子中的词与第一个词一致，否则会抛异常。|
|int paragraphId|选择插入句子的段落号|

**void addSentence**

功能：

在LTML中写入句子。输入尚未做分词的字串

参数：

|参数名 | 参数描述 |
|-------|----------|
| String sentenceContent |插入的组成句子字串。|
|int paragraphId|选择插入句子的段落号|

###LTMLOption
作为全局常量定义了分析方式类型

|选项名 | 含义述 |
|LTPOption.WS|分词|
|LTPOption.POS|词性标注|
|LTPOption. NE|命名实体识别|
|LTPOption.WSD|词义消歧|
|LTPOption. PARSER|依存句法分析|
|LTPOption.SRL|语义角色标注|

##JAVA调用示例

* 用例一：发送string类型的分析对象，得到分析结果。并将结果按分词、ID、词性、命名实体、依存关系、词义消歧、语义角色标注的顺序输出。

```java
public static void main(String[] args) {
    	LTPService ls = new LTPService("username:password"); 
		try {
			LTML ltml = ls.analyze(LTPOption.ALL,"我们都是赛尔人。");
			int sentNum = ltml.countSentence();
			for(int i = 0; i< sentNum; ++i){
//				逐句访问
				ArrayList<Word> wordList = ltml.getWords(i);
				System.out.println(ltml.getSentenceContent(i));
				for(int j = 0; j < wordList.size(); ++j){
					System.out.print("\t" + wordList.get(j).getWS());
					System.out.print("\t" + wordList.get(j).getPOS());
					System.out.print("\t" + wordList.get(j).getNE());
					System.out.print("\t" + wordList.get(j).getWSD() + "\t" + wordList.get(j).getWSDExplanation());
					System.out.print("\t" + wordList.get(j).getParserParent() + "\t" + wordList.get(j).getParserRelation());
//					如果是谓词则输出
					if(ltml.hasSRL() && wordList.get(j).isPredicate()){
						ArrayList<SRL> srls = wordList.get(j).getSRLs();
						System.out.println();
						for(int k = 0; k <srls.size(); ++k){
							System.out.println("\t\t" + srls.get(k).type + "\t" + srls.get(k).beg + "\t" + srls.get(k).end);
						}
					}
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

```
该用例首先实例化一个新的Service Client对象，用户名和密码被保存在这个对象中。然后，client发起一个请求，并指明分析目标位分词。请求结果返回并保存在一个LTML对象中。

* 例二：将待分析数据保存在LTML类中，发送LTML类型的分析对象得到分析结果。用例首先进行分词，将所得的词按用户词表进行合并或拆分，并对其进行依存句法分析。
本例中将“午夜”与“巴赛罗那”进行了合并。

```java
public static void main(String[] args) {
    	LTPService ls = new LTPService("username:password"); 
		try {
			LTML ltmlBeg = ls.analyze(LTPOption.WS,"午夜巴塞罗那是对爱情的一次诙谐、充满智慧、独具匠心的冥想。");
			LTML ltmlSec = new LTML();
			int sentNum = ltmlBeg.countSentence();
			for(int i = 0; i< sentNum; ++i){
				ArrayList<Word> wordList = ltmlBeg.getWords(i);
				for(int j = 0; j < wordList.size(); ++j){
					System.out.print("\t" + wordList.get(j).getID());
					System.out.print("\t" + wordList.get(j).getWS());
					System.out.println();
				}				
//				merge
				ArrayList<Word> mergeList = new ArrayList<Word>();
				Word mergeWord = new Word();
				mergeWord.setWS(wordList.get(0).getWS()+wordList.get(1).getWS());
				mergeList.add(mergeWord);
				for(int j = 2; j < wordList.size(); ++j){
					Word others = new Word();
					others.setWS(wordList.get(j).getWS());
					mergeList.add(others);
				}
				ltmlSec.addSentence(mergeList, 0);
			}
			ltmlSec.setOver();
			System.out.println("\nmerge and get parser results.");
			LTML ltmlOut = ls.analyze(LTPOption.PARSER, ltmlSec);
			for(int i = 0; i< sentNum; ++i){
				ArrayList<Word> wordList = ltmlOut.getWords(i);
				for(int j = 0; j < wordList.size(); ++j){
					System.out.print("\t" + wordList.get(j).getID());
					System.out.print("\t" + wordList.get(j).getWS());
					System.out.print("\t" + wordList.get(j).getPOS());
					System.out.print("\t" + wordList.get(j).getParserParent() + "\t" + wordList.get(j).getParserRelation());					
					System.out.println();
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

```
该用例首先将原始句子发出，返回得到一个ltml的结果。将结果中的单词合并放到一个wordlist中。同时建立一个空的ltml类，将wordlist内容填入ltml类里面。最后再次发回，得到结果。

#Python 版Client

LTP需要与服务器进行交互，以完成对文本的分析，分析的返回结果存储在以DOM形式组织的XML中，客户端接到分析结果后可通过提供的接口对结果进行分析。
Python 接口的操作主要有以下两个类：

*    ltpservice.LTPService类为与服务器交互类，负责验证、连接分析参数设置等。
*	LTML.LTML类为返回数据(XML)解析函数，专门负责数据的提取。

## 安装Module

如果你不希望把ltpservice安装在本地，你可以直接用

```
PYTHONPATH={PATH_TO_YOUR_PROJECT} python your_script.py
```

调用ltpservice。

如果你希望安装ltpservice，你可以用如下命令

```
[sudo] python setup.py install
```

安装ltpservice。
##主要函数接口
###LTPService类
位于ltpservice..LTPService
该类主要负责与服务器交互，并将返回结果以Ltml对象返回。

**LTPService**

功能：

构造函数，初始化用户信息

参数：

|参数名 | 参数描述 |
|-------|----------|
| authorization | 用户验证信息，信息格式为：”username:password”|
|encoding|可选参数encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

**Analyze**

功能：

发送字符串类型待分析信息，得到服务器分析结果

参数：

|参数名 | 参数描述 |
|-------|----------|
| input | 分析内容，可以为string类型的字符串，也可以为由用户指定的待分析的LTML对象。|
|opt|分析方式包括：分词（LTPOption.WS），词性标注（LTPOption.POS），命名实体识别（LTPOption. NE），词义消歧（LTPOption.WSD），依存句法分析（LTPOption. PARSER），语义角色标注（LTPOption.SRL）。|


返回值：

返回分析结果，为一个LTML类

###LTML类

LTML类位于ltpservice.LTML内；
LTML类提供XML操作方法，包括XML的生成，XML中信息的提取。
该类是对返回的数据(XML串)进行解析的主要对象。

**LTML**

功能：

构造函数，生成一个LTML类

参数：

|参数名 | 参数描述 |
|-------|----------|
| xmlstr | 可选参数xmlstr为XML串，用于生成LTML对象；也可以缺省，生成空的LTML对象|
|encoding|可选参数encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

**get_sentence**

功能：

get_sentence

参数：

|参数名 | 参数描述 |
|-------|----------|
| pid | 选择提取的段落号|
|sid|选择提取的句子号|
|encoding|可选参数encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

返回值：

返回中文分析结果序列，为str列表

**count_paragraph**

功能：

计算分析结果中段落数。

返回值：

返回分析结果中段落数

**count_sentence**

功能：

计算LTML中的句子数量

参数：

|参数名 | 参数描述 |
|-------|----------|
| pid | 可选参数，选择提取的段落号。当缺省时返回全篇的句子数量|

返回值：

返回句子数量

**tostring**

功能：

将LTML转为字符串。

参数：

|参数名 | 参数描述 |
|-------|----------|
| encoding | 可选参数encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

返回值：

返回转换生成的XML字符串

**build_from_words**

功能：

向LTML对象写入完成一定分析内容的单词序列

参数：

|参数名 | 参数描述 |
|-------|----------|
| words | 如果输入分词序列，words为一个str的列表，例如[“我”,”爱”,”北京”,”天安门”]。如果输入词性标注序列，words为一个(str,str)的列表，例如[(“我”,”p”),(”爱”,”v”),(”北京”,”ns”),(”天安门”,”n”)]|
|encoding|可选参数encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

**build**

功能：

向LTML对象写入一个未完成分词的待分析句子

参数：

|参数名 | 参数描述 |
|-------|----------|
| buffer | 待分析句子|
|encoding|可选参数encoding设置字符编码，默认为可选参数encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

##Python调用示例

* 例一：发送string类型的分析对象，得到分析结果

```python
# -*- coding:utf8 -*-
import ltpservice
from account import username, password

client = ltpservice.LTPService("%s:%s" % (username, password))
result = client.analysis("我爱北京天安门。天安门上太阳伞。", ltpservice.LTPOption.WS)

pid = 0
for sid in xrange(result.count_sentence(pid)):
    print "|".join([word.encode("utf8") for word in result.get_words(pid, sid)])
```

该用例首先，import ltpservice这个package，然后实例化一个新的Service Client对象，用户名和密码被保存在这个对象中
。然后，client发起一个请求，并指明分析目标位分词。
请求结果返回并保存在一个LTML对象中。

*  例二：将待分析数据保存在LTML类中，发送LTML类型的分析对象得到分析结果

```python
# -*- coding:utf8 -*-
import ltpservice
from account import username, password

client = ltpservice.LTPService("%s:%s" % (username, password))
ltml = ltpservice.LTML()
ltml .build_from_words(["我", "爱", "北京", "天安门"])

result = client.analysis(ltml, ltpservice.LTPOption.PARSER)    
print result.tostring()
```
首先，import ltpservice这个package，然后实例化一个新的Service Client对象，用户名和密码被保存在这个对象中。然后建立一个空的ltml类，将句子内容填入ltml类里面。最后发送请求得到结果

#C# 版Client
LTP需要与服务器进行交互，以完成对文本的分析，分析的返回结果存储在以DOM形式组织的XML中，客户端接到分析结果后可通过提供的接口对结果进行分析。

C# 接口的操作主要有以下两个类：
*   LTPService类为与服务器交互类，负责验证、连接分析参数设置等。
*	LTML类为返回数据(XML)解析函数，专门负责数据的提取。

##编译说明

*  Visual Studio 编译：

  * 第一步：添加引用的库函数：

  右击你创建的工程的引用目录，点击添加引用后弹出对话框，在浏览页面中选中你要添加的动态链接库便可。

  * 第二步：编译

  在头部添加使用名空间：```using ltp_service```，即可顺利编译

*   命令行编译：
  * 由于编译时需要调用动态链接库，所以执行下面命令
    
     编译 ：```csc  /reference:/…/ltp-service.dll /…/YouProgram.cs```
     运行 ：   ```YouProgram.exe```
   * 若用户想自己对源代码进行修改，自己生成.dll文件可以执行下面指令
    
     ```csc	/target:library  /out:ltp-service.dll *.cs```

##主要函数接口
###LTPService类
位于名空间ltp_service下。
该类主要负责与服务器交互，并将返回结果以Ltml对象返回。

**LTPService**

功能：

构造函数，初始化用户信息

参数：

|参数名 | 参数描述 |
|-------|----------|
| String authorization|用户验证信息，信息格式为：”username:password”|

**bool SetEncoding**

功能：

设置字符编码。

参数：

|参数名 | 参数描述 |
|-------|----------|
| String encodeType |默认为gbk（LTPOption.GBK），目前仅支持UTF-8(LTPOption.UTF8)及GBK，GB2312。编码的定义请参考LTPOption|

返回值：

成功返回true，失败返回false

**LTML Analyze**

功能：

发送字符串类型待分析信息，得到服务器分析结果。

参数：

|参数名 | 参数描述 |
|-------|----------|
| String option | 分析方式，分析的方式LTPOption.xxx。可参阅LTPOption类|
|String analyzeString|待分析的字串|

返回值：

待分析的字串

**LTML Analyze**

功能：

发送LTML类型待分析信息，得到服务器分析结果。也可以根据自己的需求完成部分中文信息处理，保存在LTML类里，让服务器帮你完成其他任务。

参数：

|参数名 | 参数描述 |
|-------|----------|
| String option | 分析方式，分析的方式LTPOption.xxx。可参阅LTPOption类|
|LTML ltmlIn|待分析的信息，为LTML类|

返回值：

返回分析结果，为一个LTML类

###LTML类

位于名空间ltp_service
该类是对返回的数据(xml)进行解析的主要对象。

**List<Word> GetWords**

功能：

提取LTML分析结果

参数：

|参数名 | 参数描述 |
|-------|----------|
| int paragraphId | （可缺省）选择提取的段落序号 |
|int sentenceIdx |选择提取的句子序号（当段落参数缺省时为全篇中句子序号）|

返回值：

提取的LTML分析结果

**String GetSentenceContent**

功能：

提取分句结果。

参数：

|参数名 | 参数描述 |
|-------|----------|
| int paragraphIdx | 选择提取的段落号|
|int sentenceIdx|选择提取的句子号（当段落参数缺省时为全篇中句子序号）|

返回值：

提取的分句结果

**int CountSentence**

功能：

int CountSentence

参数：

|参数名 | 参数描述 |
|-------|----------|
| int paragraphIdx | （可缺省）选择提取的段落号（当缺省时则计算全篇的句子数量）|

返回值：

选定范围的句子数量

以下几个方法是向LTML对象写数据的方法。注意以下几个问题：

1.    请保证调用以下方法的LTML对象为空的，或首先调用过ClearDOM方法，以保证LTML对象中数据一致；
2.    输入的数据必须保证一致，例如，如果第一个词完成了词性标注，则其余词也须完成词性标注，因为LTML对象是根据第一个词或第一句话生成的note结点；

已经调用SetOver的LTML对象不允许调用以下方法，否则会抛异常；凡是由LTPService对象返回的LTML对象都调用过SetOver方法；

**void AddSentence**

功能：

在LTML中写入句子。输入完成部分分析的单词序列

参数：

|参数名 | 参数描述 |
|-------|----------|
| List<Word> wordList | 插入的组成句子的单词序列。其中note结点是根据第一个sentence第一个词生成的，必须保证后面句子中的词与第一个词一致，否则会抛异常。|
|int paragraphId|选择插入句子的段落号|

**void AddSentence**

功能：

功能：在LTML中写入句子。输入尚未做分词的字串

参数：

|参数名 | 参数描述 |
|-------|----------|
| String sentenceContent | 插入的组成句子字串。 |
|int paragraphId |选择插入句子的段落号|

###LTMLOption
作为全局常量定义了分析方式类型

|选项名 | 含义述 |
|-------|---------|
|LTPOption.WS|分词|
|LTPOption.POS|词性标注|
|LTPOption. NE|命名实体识别|
|LTPOption.WSD|词义消歧|
|LTPOption. PARSER|依存句法分析|
|LTPOption.SRL|语义角色标注|

##C#调用示例
*  例一：发送string类型的分析对象，得到分析结果。并将结果按分词、ID、词性、命名实体、依存关系、词义消歧、语义角色标注的顺序输出。

```cs
static void Main()
        {
                LTPService ltpService = new LTPService(strAuthorize);
                String strSentence_new = "今天天气好晴朗，处处好风光。好听吗？";
                LTML ltml = ltpService.Analyze(LTPOption.ALL, strSentence_new);
                int sentNum = ltml.CountSentence();
                for (int i = 0; i < sentNum; ++i)
                {
                    string sentCont = ltml.GetSentenceContent(i);
                    Console.WriteLine(sentCont);
                    List<Word> wordList = ltml.GetWords(i);
                    foreach (Word curWord in wordList)
                    {
                        Console.Write(curWord.GetWS() + "\t" + curWord.GetID());
                        Console.Write("\t" + curWord.GetPOS());
                        Console.Write("\t" + curWord.GetNE());
                        Console.Write("\t" + curWord.GetParserParent() + "\t" + curWord.GetParserRelation());
                        Console.Write("\t" + curWord.GetWSD() + "\t" + curWord.GetWSDExplanation());
                        Console.WriteLine();
                        if (curWord.IsPredicate())
                        {
                            List<SRL> srls = curWord.GetSRLs();
                            Console.WriteLine(srls.Count);
                            foreach (SRL srl in srls)
                            {
                                Console.WriteLine(srl.ToString());
                            }
                        }
                    }
                }           
        }
```
该用例首先实例化一个新的Service Client对象，用户名和密码被保存在这个对象中。然后，client发起一个请求，并指明分析目标位分词。请求结果返回并保存在一个LTML对象中。

* 例二：将待分析数据保存在LTML类中，发送LTML类型的分析对象得到分析结果。用例首先进行分词，将所得的词按用户词表进行合并或拆分，并对其进行依存句法分析。
本例中将“午夜”与“巴赛罗那”进行了合并。

```cs
public static void Main() {
        LTPService ls = new LTPService("username:password"); 
        try {
            LTML ltmlBeg = ls.Analyze(LTPOption.WS," 午夜巴塞罗那是对爱情的一次诙谐、充满智慧、独具匠心的冥想。");
            LTML ltmlSec = new LTML();
            int sentNum = ltmlBeg.CountSentence();
            for(int i = 0; i< sentNum; ++i){
                List<Word> wordList = ltmlBeg.GetWords(i);
                foreach (Word curWord in wordList)
                {
                    Console.WriteLine("\t" + curWord.GetID());
                    Console.WriteLine("\t" + curWord.GetWS());
                    Console.WriteLine();
                }               
//              merge
                List<Word> mergeList = new List<Word>();
                Word mergeWord = new Word();
                mergeWord.SetWS(wordList[0].GetWS()+wordList[1].GetWS());
                mergeList.Add(mergeWord);
                for(int j = 2; j < wordList.Count; ++j){
                    Word others = new Word();
                    others.SetWS(wordList[j].GetWS());
                    mergeList.Add(others);
                }
                ltmlSec.AddSentence(mergeList, 0);
            }
            ltmlSec.SetOver();
            Console.WriteLine("\nmerge and get parser results.");
            LTML ltmlOut = ls.Analyze(LTPOption.PARSER, ltmlSec);
            for(int i = 0; i< sentNum; ++i){
                List<Word> wordList = ltmlOut.GetWords(i);
                foreach (Word curWord in wordList){
                    Console.WriteLine("\t" + curWord.GetID());
                    Console.WriteLine("\t" + curWord.GetWS());
                    Console.WriteLine("\t" + curWord.GetPOS());
                    Console.WriteLine("\t" + curWord.GetParserParent() + "\t" + curWord.GetParserRelation());                    
                    Console.WriteLine();
                }
            }
        }           
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
        }    
    }
```
该用例首先将原始句子发出，返回得到一个ltml的结果。将结果中的单词合并放到一个wordlist中。同时建立一个空的ltml类，将wordlist内容填入ltml类里面。最后再次发回，得到结果。

#Ruby版Client
LTP需要与服务器进行交互，以完成对文本的分析，分析的返回结果存储在以DOM形式组织的XML中，客户端接到分析结果后可通过提供的接口对结果进行分析。

Python 接口的操作主要有以下两个类：

*   LTPService类为与服务器交互类，负责验证、连接分析参数设置等。
*	LTML类为返回数据(XML)解析函数，专门负责数据的存取。

##编译说明

* 安装gem包

  在Ruby目录下执行如下命令：
```
gem build ltpservice.gemspec
[sudo] gem install ltpservice-0.0.0.gem
```

* 运行

  以样例为例，执行如下命令：
```ruby test/example1.rb```

##主要函数接口
###LTPService类
位于ltpservice 包内
该类主要负责与服务器交互，并将返回结果以Ltml对象返回。

**LTPService**

功能：

构造函数，初始化用户信息

参数：

|参数名 | 参数描述 |
|-------|----------|
| username | 用户名 |
|password|用户密码|

**Analyze**

功能：

发送字符串类型待分析信息，得到服务器分析结果

参数：

|参数名 | 参数描述 |
|-------|----------|
| input | 分析内容，可以为String类型的字符串，也可以为由用户指定的待分析的LTML对象。|
|opt|（可选参数）分析方式, 详细见下LTPOption|
|encoding|可选参数）encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

返回值：

返回分析结果，为一个LTML类

###LTML类
LTML类位于ltpservice包内；
LTML类提供XML操作方法，包括XML的生成，XML中信息的提取。
该类是对返回的数据(XML串)进行解析的主要对象。

**LTML**

功能：

构造函数，生成一个LTML类

参数：

|参数名 | 参数描述 |
|-------|----------|
| xmlstr | 可选参数xmlstr为XML串，用于生成LTML对象；也可以缺省，生成空的LTML对象|

**get_sentence**

功能：

取得LTML中的分析结果

参数：

|参数名 | 参数描述 |
|-------|----------|
| pid | 选择提取的段落号 |
|sid|选择提取的句子号|

返回值：

返回中文分析结果序列，为str数组里

**count_paragraph**

功能：

计算分析结果中段落数

返回值：

返回分析结果中段落数

**count_sentence**

功能：

计算LTML中的句子数量

参数：

|参数名 | 参数描述 |
|-------|----------|
| pid | 可选参数，选择提取的段落号。当缺省时返回全篇的句子数量 |
|LTML ltmlIn|待分析的信息，为LTML类|

返回值：

返回句子数量

**to_s**

功能：

将LTML转为字符串

返回值：

将LTML转为字符串

**build_from_words**

功能：

向LTML对象写入完成一定分析内容的单词序列

参数：

|参数名 | 参数描述 |
|-------|----------|
| words | 输入分词序列，words为一个str的列表，例如[“我”,”爱”,”北京”,”天安门”]。 |
|encoding |（可选参数）encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

**build**

功能：

向LTML对象写入一个未完成分词的待分析句子

参数：

|参数名 | 参数描述 |
|-------|----------|
| buffer | 待分析句子|
|encoding|可选参数encoding设置字符编码，默认为utf-8，目前仅支持UTF-8及GBK，GB2312|

###LTMLOption
作为全局常量定义了分析方式类型

|选项名 | 含义述 |
|-------|---------|
|LTPOption.WS|分词|
|LTPOption.POS|词性标注|
|LTPOption. NE|命名实体识别|
|LTPOption.WSD|词义消歧|
|LTPOption. PARSER|依存句法分析|
|LTPOption.SRL|语义角色标注|

##Ruby调用示例
*   例一：发送string类型的分析对象，得到分析结果

```ruby
require 'ltpservice'

client = LTPService.new('username', 'password')
ltml_out = client.analyze('我爱北京天安萌', 'UTF-8')
pid=0
for sid in 0...ltml_out.count_sentence(pid)
ltml_out.get_words(pid,sid).each{|word|
puts "#{word} "
}
puts "\n"
end
```
首先，import ltpservice这个package，然后实例化一个新的Service Client对象，用户名和密码被保存在这个对象中。然后，client发起一个请求，并指明分析目标位分词。请求结果返回并保存在一个LTML对象中。然后从该对象取出分析结果

* 例二：将待分析数据保存在LTML类中，发送LTML类型的分析对象得到分析结 

```ruby
# encoding: UTF-8
require 'ltpservice'
client = LTPService.new('username', 'password')
ltml = LTML.new
ltml.build_from_words(['我', '爱', '北京', '天安门'], 'UTF-8')
ltml_out=client.analyze(ltml)
pid=0
for sid in 0...ltml_out.count_sentence(pid)
ltml_out.get_words(pid,sid).each{|word|
puts "#{word} "
}
puts "\n"
end
```
首先，import ltpservice这个package，然后实例化一个新的Service Client对象，用户名和密码被保存在这个对象中。然后建立一个空的ltml类，将句子内容填入ltml类里面。最后发送请求得到结果