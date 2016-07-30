import com.tac.kulik.snail.SnailAdapterView;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by kulik on 29.07.16.
 */
public class TestSize extends TestCase{
    float[] w8 = new float[] {
            1f,
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
            0.4f*0.4f*0.6f,
            0.4f*0.4f*0.4f,
            0.4f*0.4f*0.4f,
    };

    float[] h8 = new float[] {
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
            0.4f*0.4f*0.6f,
            0.4f*0.4f*0.4f,
            0.4f*0.4f*0.4f*0.6f,
            0.4f*0.4f*0.4f*0.4f,
    };
    float[] w7 = new float[] {
            1f,
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
            0.4f*0.4f*0.6f,
            0.4f*0.4f*0.4f,
    };
    float[] h7 = new float[] {
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
            0.4f*0.4f*0.6f,
            0.4f*0.4f*0.4f,
            0.4f*0.4f*0.4f*0.6f
    };
    float[] w6 = new float[] {
            1f,
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
            0.4f*0.4f,
    };
    float[] h6 = new float[] {
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
            0.4f*0.4f*0.6f,
            0.4f*0.4f*0.4f,
    };
    float[] w5 = new float[] {
            1f,
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
    };
    float[] h5 = new float[] {
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
            0.4f*0.4f,
    };
    float[] w4 = new float[] {
            1f,
            0.6f,
            0.4f,
            0.4f,
    };
    float[] h4 = new float[] {
            0.6f,
            0.4f,
            0.4f*0.6f,
            0.4f*0.4f,
    };
    float[] w3 = new float[] {
            1f,
            0.6f,
            0.4f,
    };
    float[] h3 = new float[] {
            0.6f,
            0.4f,
            0.4f,
    };

    float[][] wn = new float[][] {
            w3,w4,w5,w6,w7,w8
    };
    float[][] hn = new float[][] {
            h3,h4,h5,h6,h7,h8
    };

    public void testHeight() {
     for (int j = 0;j<=5; j++) {
         float[] wk = wn[j];
         float[] hk = hn[j];
         tHeightN(wk.length, wk, hk);
         System.out.println("size: " + wk.length);
     }
    }

    private void tHeightN(int n, float[] wk, float[] hk) {
        for (int i = 0; i < n; i++) {
            float w = SnailAdapterView.getWidthByIndex(i, n);
            float h = SnailAdapterView.getHeightByIndex(i,n);
            System.out.println("i:" + i + ", width: " + w + ", height: " + h);
            Assert.assertEquals(wk[i], w);
            Assert.assertEquals(hk[i], h);
        }
    }
}
