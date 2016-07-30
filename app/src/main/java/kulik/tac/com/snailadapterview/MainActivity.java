package kulik.tac.com.snailadapterview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tac.kulik.snail.SnailAdapterView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SnailAdapterView viewById = (SnailAdapterView) findViewById(R.id.snailview);
        SnailAdapter adapter = new SnailAdapter(this);
        viewById.setAdapter(adapter);
    }


}
