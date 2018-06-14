package demo.pig;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

import java.io.IOException;

/**
 * 注册jar包  register /root/temp/pig.jar
 * 为自定义函数起别名：define myfilter demo.pig.MyFilterFunction;
 * 用法：filter emp by demo.pig.IsSalaryTooHigh(sal)
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/15
 */
public class FilterFC extends FilterFunc {

    @Override
    public Boolean exec(Tuple tuple) throws IOException {
        Integer sal = (Integer)tuple.get(0);

        return sal<1000?true:false;
    }
}
