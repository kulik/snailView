package kulik.tac.com.snailadapterview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tac.kulik.snail.OnScrollListener;
import com.tac.kulik.snail.SnailAdapterView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Random;

public class MainActivity extends Activity {

    private Deque<Integer> backNavigationStack = new ArrayDeque<>();
    private SnailAdapterView viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewById = (SnailAdapterView) findViewById(R.id.snailview);
        final SnailAdapter adapter = new SnailAdapter(this);
        viewById.setAdapter(adapter);
        viewById.setOnScrellListener(new OnScrollListener() {
            @Override
            public void onScrolledToPos(int pos, View view) {
                Log.d("scrolled", "scrolled to " + pos);
            }
        });
        viewById.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("goto", "goto " + i);
                backNavigationStack.push(viewById.getPosition());
                viewById.setSelection(i);

            }
        });


        findViewById(R.id.move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Random r = new Random();
//                int pos = r.nextInt(adapter.getCount());
//                viewById.setSelection(pos);
                shuffleArray(adapter.colors);
                adapter.notifyDataSetChanged();
//                Toast.makeText(MainActivity.this, "pos " + pos,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            Integer pop = backNavigationStack.pop();
            viewById.setSelectionBack(pop);
        } catch (NoSuchElementException e) {
            Toast.makeText(this, "Backsteck is full", Toast.LENGTH_LONG).show();
        }
    }

    public static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


}
