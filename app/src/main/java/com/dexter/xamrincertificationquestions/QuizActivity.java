package com.dexter.xamrincertificationquestions;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class QuizActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    private final static String TAG= "QuizActivity";
	List<Question> quesList;
	int score=0;
	int qid=0;
	Question currentQ;
	TextView txtQuestion;
	Button butNext;
	LinearLayout container;
	RadioGroup radioGroup;
    ArrayList<String> checkedAnswers;
    QuestionFacade questionFacade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
        generateJson();
		DbHelper db=new DbHelper(this);
		quesList=db.getAllQuestions();
		currentQ=quesList.get(qid);
		txtQuestion=(TextView)findViewById(R.id.textView1);
        container=(LinearLayout)findViewById(R.id.container_radio_group);
		radioGroup=new RadioGroup(this);
        container.addView(radioGroup);
        checkedAnswers=new ArrayList<>();
        questionFacade=QuestionFacade.getInstance();

		butNext=(Button)findViewById(R.id.button1);
		setQuestionView();
		butNext.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
                //Question.Options options=null;
                Question.Answers answers=null;
                if (currentQ.getANSWER()!=null){
                    if (Utility.fromAnswerJson(currentQ.getANSWER()) instanceof Question.Answers){
                        answers= (Question.Answers) Utility.fromAnswerJson(currentQ.getANSWER());
                    }
                }

                /*if (currentQ.getOPT_JSON()!=null){
                    if (Utility.fromJson(currentQ.getOPT_JSON()) instanceof Question.Options){
                        options= (Question.Options) Utility.fromJson(currentQ.getOPT_JSON());
                    }
                }*/

                if (currentQ.getQUE_TYPE().equals(Constants.QUESTION_TYPE_SINGLE) && radioGroup!=null && radioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(QuizActivity.this, "Please select any option", Toast.LENGTH_SHORT).show();
                    return;
                }else if (currentQ.getQUE_TYPE().equals(Constants.QUESTION_TYPE_MULTIPLE) && checkedAnswers.size()==0){
                    Toast.makeText(QuizActivity.this, "Please select any option", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentQ.getQUE_TYPE().equals(Constants.QUESTION_TYPE_SINGLE)){
                    RadioButton answer=(RadioButton)radioGroup.getChildAt(radioGroup.getCheckedRadioButtonId());
                    assert answers != null;
                    if(answers.multipleAnswers[0].equals(answer.getText())){
                        score++;
                        Log.d("score", "Your score"+score);
                    }else {
                        questionFacade.addWrongQuestion(currentQ);
                    }
                }else {
                    assert answers != null;
                    int i = 0;
                    for (; i < answers.multipleAnswers.length; i++) {
                        if (checkedAnswers.size()==answers.multipleAnswers.length){
                            if(!checkedAnswers.contains(answers.multipleAnswers[i])){
                                questionFacade.addWrongQuestion(currentQ);
                                break;
                            }
                        }else {
                            questionFacade.addWrongQuestion(currentQ);
                            break;
                        }
                    }
                    checkedAnswers.clear();
                    if (i==answers.multipleAnswers.length){
                        score++;
                        Log.d("score", "Your score"+score);
                    }
                }

				if(qid<5){					
					currentQ=quesList.get(qid);
                    if (currentQ.getQUE_TYPE().equals(Constants.QUESTION_TYPE_SINGLE)){
                        container.removeAllViews();
                        radioGroup=new RadioGroup(QuizActivity.this);
                        container.addView(radioGroup);
                        setQuestionView();
                    }else {
                        container.removeAllViews();
                        loadMultipleChoiceQuestionsView();
                    }
				}else{
                    Log.d(TAG, "onClick: Total Score: "+score);
                    Log.d(TAG, "onClick: Total Wrong Answer: "+questionFacade.getWrongQuestions().size());
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
					Bundle b = new Bundle();
					b.putInt("score", score); //Your score
					intent.putExtras(b); //Put your score to your next Intent
					startActivity(intent);
					finish();
				}
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_quiz, menu);
		return true;
	}
	private void setQuestionView(){
		txtQuestion.setText(currentQ.getQUESTION());
		Question.Options options= (Question.Options) Utility.fromOptionJson(currentQ.getOPT_JSON());
		radioGroup.removeAllViews();
		for (int i = 0; i < options.options.length; i++) {
			RadioButton radioButton =new RadioButton(this);
			radioButton.setText(options.options[i]);
			radioButton.setId(i);
			radioGroup.addView(radioButton);
		}
		qid++;
	}

	private void loadMultipleChoiceQuestionsView(){
        txtQuestion.setText(currentQ.getQUESTION());
        Question.Options options= (Question.Options) Utility.fromOptionJson(currentQ.getOPT_JSON());
        for (int i = 0; i < options.options.length; i++) {
            CheckBox checkBox =new CheckBox(this);
            checkBox.setText(options.options[i]);
            checkBox.setId(i);
            checkBox.setOnCheckedChangeListener(QuizActivity.this);
            container.addView(checkBox);
        }
        qid++;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkedAnswers.add(buttonView.getText().toString());
    }

    private void generateJson(){
        Quest quest1=new Quest("In Xamarin.Forms, you can create a two-way binding by changing the ______ property. ",
                new String[]{"Type",
                        "Mode",
                        "Source",
                        "Style"
                },new String[]{"Mode"});

        Quest quest2=new Quest("Which Xamarin.Forms Page type is used for stack-based navigation?",
                new String[]{"ControlPage",
                        "StackPage",
                        "UINavigationController",
                        "NavigationPage"
                },new String[]{"NavigationPage"});


        Quest quest3=new Quest("True or False: Xamarin apps support modern .NET features like Tasks and LINQ. ",
                new String[]{"True",
                        "False"
                },new String[]{"True"});


        Quest quest4=new Quest("True or False: When using worker/background threads in your mobile application (iOS, Android and Windows), you should avoid accessing UI control properties and methods and instead switch to the UI thread to update the UI.",
                new String[]{"True",
                        "False"
                },new String[]{"True"});

        Quest quest5=new Quest("In the context of web services, what is REST?",
                new String[]{"An architecture based on stateless and cacheable calls and simple HTTP",
                        "The underlying protocol of Windows Communication Framework (WCF)",
                        "The 3rd party API licensed by service providers to access their public API’s",
                        "A Linux based protocol for serving data to remote clients"
                },new String[]{"An architecture based on stateless and cacheable calls and simple HTTP"});

        Quest quest6=new Quest("An iOS application can be developed using: (Select all that apply)",
                new String[]{"Visual Studio for MAC",
                        "MonoDevelop on Linux",
                        "Visual Studio on Windows"
                },new String[]{"Visual Studio for MAC","Visual Studio on Windows"});//multi

        Quest quest7=new Quest("The two ways Xamarin recommends sharing code across your mobile projects are:",
                new String[]{"HTML and JavaScript",
                        "Shared Projects and shared binaries (such as Portable Class Libraries and .NET Standard libraries)",
                        ".NET Class libraries and Copy/Paste",
                        "File Linking and .NET Class Libraries"
                },new String[]{"Shared Projects and shared binaries (such as Portable Class Libraries and .NET Standard libraries)"});

        Quest quest8=new Quest("One technique you can use with Shared Projects is partial methods. If you do not implement the partial method anywhere in your code, then all calls to that method will _________.",
                new String[]{"Result in compilation error",
                        "Result in runtime error",
                        "Be removed from compiled code"
                },new String[]{"Be removed from compiled code"});

        Quest quest9=new Quest("What kind of library can be used across multiple .NET platforms?",
                new String[]{".NET 3.5 class library",
                        ".NET 4.0 class library",
                        ".NET 4.5 class library",
                        "Portable Class Library"
                },new String[]{"Portable Class Library"});

        Quest quest10=new Quest("True or False: When using Shared Projects, you must provide interfaces or abstractions for any platform-specific features used.",
                new String[]{"True",
                        "False"
                },new String[]{"True"});

     //===========================END OF PART 1===========================================================>

        Quest quest11=new Quest("True or False: Portable Class Libraries restrict the usable"+
                "APIs to be the intersection of the target platforms API set.",
                new String[]{"True",
                        "False"
                },new String[]{"True"});

        Quest quest12=new Quest("True or False: The NuGet package manager is a great place to find and distribute cross-platform libraries.",
                new String[]{"True",
                        "False"
                },new String[]{"True"});

        Quest quest13=new Quest("When using Xamarin.iOS and Xamarin.Android to define platform-specific UI, the portions of code you can typically share include: (Select all that apply)",
                new String[]{"Business Logic",
                        "Data Layer",
                        "User interface",
                        "Navigation management"
                },new String[]{"Business Logic","Data Layer"});

        Quest quest14=new Quest("True or False: On its own, a Shared Project generates a .dll assembly which can then be added to your platform-specific projects.",
                new String[]{"True",
                        "False"
                },new String[]{"False"});

        Quest quest15=new Quest("In an Android XML layout file, what is the correct format for the id attribute?",
                new String[]{"<android:id>my_button</android:id>",
                        "id=\"@+id/my_button\"",
                        "android:id=\"@+id/my_button\"",
                        "android:id=\"my_button\""
                },new String[]{"android:id=\"@+id/my_button\""});

        Quest quest16=new Quest("An Android application is a collection of collaborating parts called _______.",
                new String[]{"Intents",
                        "Bindings",
                        "Activities",
                        "Elements"
                },new String[]{"Activities"});

        Quest quest17=new Quest("What kind of information is contained in the " +
                "AndroidManifest.XML file?",
                new String[]{"Android-generated constants for resources  such as strings, audio and video.",
                        "Permissions, activity registration and OS version compatibility.",
                        "The XML layouts of all the user interfaces.",
                        "A list of all the intents present in the Android operation system."
                },new String[]{"Permissions, activity registration and OS version compatibility."});

        Quest quest18=new Quest("In Android, to programmatically send an email/message you can use:",
                new String[]{"An Intent with an ActionSend action.",
                        "A Broadcast Receiver",
                        "A Service",
                        "MSMailComposeViewController"
                },new String[]{"An Intent with an ActionSend action."});

        Quest quest19=new Quest("In Xamarin.Android, what is the extension used for a layout file? ",
                new String[]{"xml",
                        ".xaml",
                        ".layout",
                        ".axml"
                },new String[]{".axml"});

        Quest quest20=new Quest("Which Android resource folder would you place " +
                "your .axml files into?",
                new String[]{"drawable",
                        "layout",
                        "values",
                        "assets"
                },new String[]{"layout"});
        //===========================END OF PART 2===========================================================>

        Quest quest21=new Quest("True or False: Portable Class Libraries restrict the usable"+
                "APIs to be the intersection of the target platforms API set.",
                new String[]{"True",
                        "False"
                },new String[]{"True"});

        Quest quest22=new Quest("True or False: The NuGet package manager is a great place to find and distribute cross-platform libraries.",
                new String[]{"True",
                        "False"
                },new String[]{"True"});

        Quest quest23=new Quest("When using Xamarin.iOS and Xamarin.Android to define platform-specific UI, the portions of code you can typically share include: (Select all that apply)",
                new String[]{"Business Logic",
                        "Data Layer",
                        "User interface",
                        "Navigation management"
                },new String[]{"Business Logic","Data Layer"});

        Quest quest24=new Quest("True or False: On its own, a Shared Project generates a .dll assembly which can then be added to your platform-specific projects.",
                new String[]{"True",
                        "False"
                },new String[]{"False"});

        Quest quest25=new Quest("In an Android XML layout file, what is the correct format for the id attribute?",
                new String[]{"<android:id>my_button</android:id>",
                        "id=\"@+id/my_button\"",
                        "android:id=\"@+id/my_button\"",
                        "android:id=\"my_button\""
                },new String[]{"android:id=\"@+id/my_button\""});

        Quest quest26=new Quest("An Android application is a collection of collaborating parts called _______.",
                new String[]{"Intents",
                        "Bindings",
                        "Activities",
                        "Elements"
                },new String[]{"Activities"});

        Quest quest27=new Quest("What kind of information is contained in the " +
                "AndroidManifest.XML file?",
                new String[]{"Android-generated constants for resources  such as strings, audio and video.",
                        "Permissions, activity registration and OS version compatibility.",
                        "The XML layouts of all the user interfaces.",
                        "A list of all the intents present in the Android operation system."
                },new String[]{"Permissions, activity registration and OS version compatibility."});

        Quest quest28=new Quest("In Android, to programmatically send an email/message you can use:",
                new String[]{"An Intent with an ActionSend action.",
                        "A Broadcast Receiver",
                        "A Service",
                        "MSMailComposeViewController"
                },new String[]{"An Intent with an ActionSend action."});

        Quest quest29=new Quest("In Xamarin.Android, what is the extension used for a layout file? ",
                new String[]{"xml",
                        ".xaml",
                        ".layout",
                        ".axml"
                },new String[]{".axml"});

        Quest quest30=new Quest("Which Android resource folder would you place " +
                "your .axml files into?",
                new String[]{"drawable",
                        "layout",
                        "values",
                        "assets"
                },new String[]{"layout"});


        ArrayList<Quest> questArrayList=new ArrayList<>();
        questArrayList.add(quest1);
        questArrayList.add(quest2);
        questArrayList.add(quest3);
        questArrayList.add(quest4);
        questArrayList.add(quest5);
        questArrayList.add(quest6);
        questArrayList.add(quest7);
        questArrayList.add(quest8);
        questArrayList.add(quest9);
        questArrayList.add(quest10);
        questArrayList.add(quest11);
        questArrayList.add(quest12);
        questArrayList.add(quest13);
        questArrayList.add(quest14);
        questArrayList.add(quest15);
        questArrayList.add(quest16);
        questArrayList.add(quest17);
        questArrayList.add(quest18);
        questArrayList.add(quest19);
        questArrayList.add(quest20);

        questArrayList.add(quest21);
        questArrayList.add(quest22);
        questArrayList.add(quest23);
        questArrayList.add(quest24);
        questArrayList.add(quest25);
        questArrayList.add(quest26);
        questArrayList.add(quest27);
        questArrayList.add(quest28);
        questArrayList.add(quest29);
        questArrayList.add(quest30);

        writeToFile(Utility.toJson(questArrayList));
    }

    private String FILE_NAME= "/mnt/sdcard/ques3.txt";
    private void writeToFile(String json){
        FileOutputStream fos = null;
        try{

            fos = new FileOutputStream(FILE_NAME);
            byte[] buffer =json.getBytes();
                fos.write(buffer, 0, buffer.length);
                fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
