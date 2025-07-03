package com.cfks.telefriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cfks.telefriends.watch.XTC;

public class AddFriendActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_friend);
    ((Button) findViewById(R.id.cancel_add_friends)).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        // TODO: Implement this method
        backMainActivity();
      }
    });
    
    Intent intent = getIntent();
    int watch_type = intent.getIntExtra("watch_type",-1);
    switch(watch_type){
        case WatchType.WATCH_XTC:
            XTC.toDo(this);
            break;
        default:
            Toast.makeText(this, "作者没做", Toast.LENGTH_SHORT).show();
            backMainActivity();
            break;
    }
  }
    
  private void backMainActivity() {
  	Intent intent = new Intent();
      intent.setClass(this, MainActivity.class);
      startActivity(intent);
      finish();
  }
}