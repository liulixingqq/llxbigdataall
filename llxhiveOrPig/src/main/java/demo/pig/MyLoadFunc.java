package demo.pig;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.pig.LoadFunc;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import java.io.IOException;

/**
 * 1、输入流（getInputFormat）  ---> 输入的路径（setLocation）
 * 2、输入的格式(prepareRead)
 * 3、登录得到值getNext
 */
public class MyLoadFunc extends LoadFunc {

    //定义一个变量保存输入流
    private RecordReader reader ;

    @Override
    public void setLocation(String s, Job job) throws IOException {
        // 从HDFS输入的路径
        FileInputFormat.setInputPaths(job,new Path(s));
    }
    @Override
    public InputFormat getInputFormat() throws IOException {
        // 输入数据的格式：字符串
        return new TextInputFormat();
    }
    @Override
    public void prepareToRead(RecordReader recordReader, PigSplit pigSplit) throws IOException {
        // RecordReader reader: 代表HDFS的输入流
        this.reader = recordReader;
    }

    @Override
    public Tuple getNext() throws IOException {
        // 从输入流中读取一行，如何解析生成返回的tuple
        //数据： I love Beijing
        Tuple result = null;
        try{
            if(!this.reader.nextKeyValue()){
                return result;
            }

            String data = this.reader.getCurrentValue().toString();
            //分词
            String[] words = data.split(" ");

            result = TupleFactory.getInstance().newTuple();
            //每一个单词单独生成一个tuple(s)，再把这些tuple放入一个bag中。
            //在把这个bag放入result中
            //创建一个表
            DataBag bag = BagFactory.getInstance().newDefaultBag();
            for(String w:words){
                //为每个单词生成tuple
                Tuple aTuple = TupleFactory.getInstance().newTuple();
                aTuple.append(w); //将单词放入tuple

                //再把这些tuple放入一个bag中
                bag.add(aTuple);
            }
            //在把这个bag放入result中
            result.append(bag);

        }catch(Exception ex){
            ex.printStackTrace();
        }



        return result;
    }
}
