package com.dexter.xamrincertificationquestions;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends Activity {
	RecyclerView recyclerView;
	ArrayList<Question> wrongQuestionsList;
	QuestionFacade questionFacade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		questionFacade=QuestionFacade.getInstance();
		wrongQuestionsList=questionFacade.getWrongQuestions();

		//get rating bar object
		RatingBar bar=(RatingBar)findViewById(R.id.ratingBar1); 
		bar.setNumStars(5);
		bar.setStepSize(0.5f);
		//get text view
		TextView textViewResult=(TextView)findViewById(R.id.textResult);
		//get score
		Bundle b = getIntent().getExtras();
		int score= b.getInt("score");
		//display score
		bar.setRating(score);
		switch (score){
			case 0:
			case 1:
			case 2: textViewResult.setText("Opps, try again and keep learning");
			break;
			case 3:
			case 4:textViewResult.setText("Congratulations, You passed the Xamarin Exam");
			break;
			case 5:textViewResult.setText("Congratulations, You passed the Xamarin Exam");
			break;
		}

		recyclerView=(RecyclerView)findViewById(R.id.recycler_wrong_answer);
		if (wrongQuestionsList.size()>0){
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			recyclerView.setAdapter(new WrongQuestionsAdapter(ResultActivity.this));
			recyclerView.setVisibility(View.VISIBLE);
		}else {
			recyclerView.setVisibility(View.GONE);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_result, menu);
		return true;
	}

}
