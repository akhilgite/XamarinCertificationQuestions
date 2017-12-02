package com.dexter.xamrincertificationquestions;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by 10644291 on 15-11-2017.
 */

public class Utility {
    public static String toJson(Object options){
        Gson gson=new Gson();
        return gson.toJson(options);
    }

    public static Question.Options fromOptionJson(String str){
        Gson gson=new Gson();
        return gson.fromJson(str,Question.Options.class);
    }

    public static Question.Answers fromAnswerJson(String str){
        Gson gson=new Gson();
        return gson.fromJson(str,Question.Answers.class);
    }
}
