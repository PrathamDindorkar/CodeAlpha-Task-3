package com.example.flashcardquiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private Button submitButton;
    private TextView scoreTextView;

    private List<Flashcard> flashcardList;
    private int currentFlashcardIndex;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        submitButton = findViewById(R.id.submitButton);
        scoreTextView = findViewById(R.id.scoreTextView);

        flashcardList = loadFlashcards();
        currentFlashcardIndex = 0;
        score = 0;

        displayNextFlashcard();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                displayNextFlashcard();
            }
        });
    }

    private void displayNextFlashcard() {
        if (currentFlashcardIndex < flashcardList.size()) {
            Flashcard flashcard = flashcardList.get(currentFlashcardIndex);
            questionTextView.setText(flashcard.getQuestion());
            answerEditText.setText("");
        } else {
            Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show();
            scoreTextView.setText("Final Score: " + score);
        }
    }

    private void checkAnswer() {
        String userAnswer = answerEditText.getText().toString();
        String correctAnswer = flashcardList.get(currentFlashcardIndex).getAnswer();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect! Correct answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }

        scoreTextView.setText("Score: " + score);
        currentFlashcardIndex++;
    }

    private List<Flashcard> loadFlashcards() {
        SharedPreferences sharedPreferences = getSharedPreferences("flashcards", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("flashcardList", null);
        Type type = new TypeToken<ArrayList<Flashcard>>() {}.getType();
        List<Flashcard> flashcards = gson.fromJson(json, type);

        return flashcards == null ? new ArrayList<Flashcard>() : flashcards;
    }
}
