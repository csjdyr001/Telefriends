package com.cfks.telefriends;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import com.squareup.okhttp.FormEncodingBuilder;
import java.io.IOException;
import org.json.JSONObject;
import com.cfks.telefriends.utils.共享数据;

public class LoginActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    EditText emailEditor;
    EditText passwordEditor;
    int count = 0;
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        共享数据.初始化数据(this,"Telefriends");
        if(Build.VERSION.SDK_INT>=23){
            ActivityCompat.requestPermissions(this,MainActivity.getAllPermissions(this),1001);
        }
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        emailEditor = findViewById(R.id.email);
        passwordEditor = findViewById(R.id.password);
        emailEditor.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEditor.setText(共享数据.取文本("email"));
        passwordEditor.setText(共享数据.取文本("password"));
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeBottom() {
            }

        });
        ((Button)findViewById(R.id.login)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO: Implement this method
                String email = emailEditor.getText().toString();
                String password = passwordEditor.getText().toString();
                Toast.makeText(LoginActivity.this, "登录中…", Toast.LENGTH_SHORT).show();
                loginApi(email,password);
            }
        });
    }
    
    private void loginApi(final String email,final String pwd){
        // 构建表单参数
        FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        formBuilder.add("email", email);
        formBuilder.add("password",pwd);
        NetUtils.post(this,ApiConfig.loginApi,formBuilder,new NetUtils.NetCallback(){
            @Override
            public void onSucceed(JSONObject json) throws Exception {
                // TODO: Implement this method
                Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                if(json.getInt("code") == 200){
                    String token = json.getString("token");
                    共享数据.置文本("email",email);
                    共享数据.置文本("password",pwd);
                    共享数据.置文本("token",token);
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        });
    }
    
    private class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
}
