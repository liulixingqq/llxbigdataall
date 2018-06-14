package demo.hive;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestMain {
    public static void main(String[] args) {
        String sql = "select * from mytest1";

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try{
            //获取连接
            conn = JDBCUtils.getConnection();

            //创建statement  sql运行环境
            st = conn.createStatement();

            //查询SQL
            rs = st.executeQuery(sql);

            while(rs.next()){
                ////姓名  薪水
                int id = rs.getInt("tid");
                String name = rs.getString("tname");
                System.out.println(id+"\t"+name);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            //释放资源
            JDBCUtils.release(conn, st, rs);
        }
    }
}
