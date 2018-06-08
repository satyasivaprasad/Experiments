package sample.rx.swipeuptodismissanim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View swipeImage = findViewById(R.id.swipe_image);
        View swipeParent = findViewById(R.id.swipe_parent);
        swipeParent.setOnTouchListener(new SwipeUpToDismissListener(swipeImage));
    }
}
