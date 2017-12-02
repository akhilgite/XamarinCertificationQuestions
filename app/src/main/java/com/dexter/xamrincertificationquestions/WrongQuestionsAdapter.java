package com.dexter.xamrincertificationquestions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 10644291 on 15-11-2017.
 */

public class WrongQuestionsAdapter extends RecyclerView.Adapter<WrongQuestionsAdapter.ItemHolder>{
    QuestionFacade questionFacade;
    ArrayList<Question> questionArrayList;
    LayoutInflater inflater;

    public WrongQuestionsAdapter(Context context){
        questionFacade=QuestionFacade.getInstance();
        questionArrayList=questionFacade.getWrongQuestions();
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView=inflater.inflate(R.layout.item_wrong_answer,parent,false);
        return new ItemHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Question currentQuestion=questionArrayList.get(position);
        holder.textViewQuestion.setText("Q: "+currentQuestion.getQUESTION());
        Question.Answers answers=Utility.fromAnswerJson(currentQuestion.getANSWER());
        String strAnswer="";
        if (answers.multipleAnswers.length>1){
            for (int i = 0; i < answers.multipleAnswers.length; i++) {
                strAnswer+=answers.multipleAnswers[i];
                if (i!=answers.multipleAnswers.length-1)
                    strAnswer+="\n";
            }
        }else{
            strAnswer=answers.multipleAnswers[0];
        }
        holder.textViewAnswer.setText(strAnswer);
    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{
        TextView textViewQuestion;
        TextView textViewAnswer;
        public ItemHolder(View itemView) {
            super(itemView);
            textViewQuestion=(TextView)itemView.findViewById(R.id.textview_question);
            textViewAnswer=(TextView)itemView.findViewById(R.id.textview_answer);
        }
    }
}
