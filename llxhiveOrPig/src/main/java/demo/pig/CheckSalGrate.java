package demo.pig;

import com.sun.tools.corba.se.idl.StringGen;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

import java.io.IOException;

/**
 *emp1 = foreach emp generate ename,demo.pig.CheckSalaryGrade(sal);
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/15
 */
public class CheckSalGrate extends EvalFunc<String> {

    @Override
    public String exec(Tuple tuple) throws IOException {

        Integer sal = (Integer)tuple.get(0);
        if(sal <1000){

            return "grate A";
        }else {
            return "grate B";
        }

    }
}
