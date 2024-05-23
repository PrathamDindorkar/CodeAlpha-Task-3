package com.example.flashcardquiz;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddFlashcardActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText answerEditText;
    private Button saveButton;

    private List<Flashcard> flashcardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        saveButton = findViewById(R.id.saveButton);

        flashcardList = loadFlashcards();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEditText.getText().toString();
                String answer = answerEditText.getText().toString();

                if (!question.isEmpty() && !answer.isEmpty()) {
                    flashcardList.add(new Flashcard(question, answer));
                    saveFlashcards();
                    Toast.makeText(AddFlashcardActivity.this, "Flashcard saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddFlashcardActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<Flashcard> loadFlashcards() {
        SharedPreferences sharedPreferences = getSharedPreferences("flashcards", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("flashcardList", null);
        Type type = new TypeToken<ArrayList<Flashcard>>() {}.getType();
        List<Flashcard> flashcards = gson.fromJson(json, type);

        return flashcards == null ? new ArrayList<Flashcard>() : flashcards;
    }

    private void saveFlashcards() {
        SharedPreferences sharedPreferences = getSharedPreferences("flashcards", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(flashcardList);
        editor.putString("flashcardList", json);
        editor.apply();
    }
}
