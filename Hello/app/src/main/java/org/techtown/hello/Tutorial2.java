package org.techtown.hello;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Tutorial2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);

        ImageButton nextbtn = (ImageButton)findViewById(R.id.nextbtn);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_next=new Intent(Tutorial2.this, Tutorial3.class);
                startActivity(intent_next);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });

    }
}
