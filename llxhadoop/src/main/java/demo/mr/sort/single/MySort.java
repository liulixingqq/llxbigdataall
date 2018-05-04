package demo.mr.sort.single;

import org.apache.hadoop.io.IntWritable;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/4
 */
public class MySort extends IntWritable.Comparator {

    @Override
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        return super.compare(b1, s1, l1, b2, s2, l2);
    }


}
