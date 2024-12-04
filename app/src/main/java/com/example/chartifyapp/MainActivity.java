package com.example.chartifyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText userInput;
    private Button sendButton;
    private OkHttpClient client;

    private static final String GEMINI_API_URL = "Your_apiKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatContainer = findViewById(R.id.chatContainer);
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);

        client = new OkHttpClient();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = userInput.getText().toString().trim();
                if (!userMessage.isEmpty()) {
                    addChatBubble(userMessage, true);
                    userInput.setText("");
                    fetchChatbotResponse(userMessage);
                }
            }
        });
    }

    private void addChatBubble(String message, boolean isUser) {
        TextView chatBubble = new TextView(this);
        chatBubble.setText(message);
        chatBubble.setTextSize(16);
        chatBubble.setPadding(12, 8, 12, 8);
        chatBubble.setBackgroundResource(isUser ? R.drawable.user_bubble : R.drawable.bot_bubble);
        chatBubble.setTextColor(getResources().getColor(isUser ? android.R.color.white : android.R.color.black));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        params.gravity = isUser ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START;
        chatBubble.setLayoutParams(params);

        chatContainer.addView(chatBubble);
        ((ScrollView) chatContainer.getParent()).fullScroll(View.FOCUS_DOWN);
    }

    private void fetchChatbotResponse(String userMessage) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("prompt",  userMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> addChatBubble("Maaf, terjadi kesalahan.", false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String botResponse = jsonResponse.optString("response", "Maaf, tidak ada jawaban.");
                        runOnUiThread(() -> addChatBubble(botResponse, false));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> addChatBubble("Kesalahan parsing data.", false));
                    }
                } else {
                    runOnUiThread(() -> addChatBubble("Maaf, server tidak merespon.", false));
                }
            }
        });
    }
}
