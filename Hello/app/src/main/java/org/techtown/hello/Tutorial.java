package org.techtown.hello;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Tutorial extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ImageButton nextbtn = (ImageButton)findViewById(R.id.nextbtn);


        //로딩화면
        Intent intent_load = new Intent(this, Loading.class);
        startActivity(intent_load);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_next=new Intent(Tutorial.this, Tutorial2.class);
                startActivity(intent_next);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });

    }
}
