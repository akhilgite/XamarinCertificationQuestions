package com.dexter.xamrincertificationquestions;

/**
 * Created by 10644291 on 16-11-2017.
 */

public class Quest {
    String question;
    String []options;
    String []answers;

    public Quest(String question, String[] options, String[] answers) {
        this.question = question;
        this.options = options;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
}
