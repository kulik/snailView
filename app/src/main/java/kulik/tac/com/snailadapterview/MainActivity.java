package kulik.tac.com.snailadapterview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tac.kulik.snail.SnailAdapterView;

import java.util.Random;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SnailAdapterView viewById = (SnailAdapterView) findViewById(R.id.snailview);
        final SnailAdapter adapter = new SnailAdapter(this);
        viewById.setAdapter(adapter);
        viewById.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewById.setSelection(i);
            }
        });

        findViewById(R.id.move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int pos = r.nextInt(adapter.getCount());
                viewById.setSelection(pos);
                Toast.makeText(MainActivity.this, "pos " + pos,Toast.LENGTH_LONG).show();
            }
        });
    }


}
