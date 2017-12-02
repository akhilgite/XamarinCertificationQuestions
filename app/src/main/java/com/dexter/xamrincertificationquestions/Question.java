package com.dexter.xamrincertificationquestions;
public class Question {
	private int ID;
	private String QUESTION;
	private String OPT_JSON;
	private String QUE_TYPE;
	private String ANSWER;
	public Question()
	{
		ID=0;
		QUESTION="";
		OPT_JSON="";
		QUE_TYPE="";
		ANSWER="";
	}
	public Question(String question, String optionJson, String questionType,
			String answer) {
		
		QUESTION = question;
		OPT_JSON = optionJson;
		QUE_TYPE = questionType;
		ANSWER = answer;
	}
	public int getID()
	{
		return ID;
	}

	public String getQUESTION() {
		return QUESTION;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setQUESTION(String QUESTION) {
		this.QUESTION = QUESTION;
	}

	public void setOPT_JSON(String OPT_JSON) {
		this.OPT_JSON = OPT_JSON;
	}

	public void setQUE_TYPE(String QUE_TYPE) {
		this.QUE_TYPE = QUE_TYPE;
	}

	public void setANSWER(String ANSWER) {
		this.ANSWER = ANSWER;
	}

	public String getOPT_JSON() {
		return OPT_JSON;

	}

	public String getQUE_TYPE() {
		return QUE_TYPE;
	}

	public String getANSWER() {
		return ANSWER;
	}

	public static class Options{
		String []options;
		public Options(String[] options){
			this.options=options;
		}
	}

	public static class Answers{
		String []multipleAnswers;
		public Answers(String[] multipleAnswers){
			this.multipleAnswers=multipleAnswers;
		}
	}
}
