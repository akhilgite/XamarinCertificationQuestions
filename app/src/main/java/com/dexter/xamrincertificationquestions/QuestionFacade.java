package com.dexter.xamrincertificationquestions;

import java.util.ArrayList;

/**
 * Created by 10644291 on 15-11-2017.
 */

public class QuestionFacade {
    private static QuestionFacade INSTANCE=null;
    private ArrayList<Question> wrongQuestions=null;

    private QuestionFacade(){
        wrongQuestions=new ArrayList<>();
    }

    public static QuestionFacade getInstance(){
        if (INSTANCE==null){
            INSTANCE=new QuestionFacade();
        }
        return INSTANCE;
    }

    public void addWrongQuestion(Question question){
        wrongQuestions.add(question);
    }

    public ArrayList<Question> getWrongQuestions(){
        return wrongQuestions;
    }

    public void clear(){
        wrongQuestions.clear();
    }
}
