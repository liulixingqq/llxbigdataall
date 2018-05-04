package demo.mr.partition;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/4
 */
public class Employee implements Writable{

    //定义员工的属性  7654,MARTIN,SALESMAN,7698,1981/9/28,1250,1400,30
    private int empno;//员工号
    private String ename;//姓名
    private String job;//职位
    private int mgr;//经理的员工号
    private String hiredate;//入职日期
    private int sal;//月薪
    private int comm;//奖金
    private int deptno;//部门号

    @Override
    public String toString() {
        return "Employee{" + "empno=" + empno + ", ename='" + ename + '\'' + ", job='" + job + '\'' + ", mgr=" + mgr + ", hiredate='" + hiredate + '\'' + ", sal=" + sal + ", comm=" + comm + ", deptno=" + deptno + '}';
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.empno);
        dataOutput.writeUTF(this.ename);
        dataOutput.writeUTF(this.job);
        dataOutput.writeInt(this.mgr);
        dataOutput.writeUTF(this.hiredate);
        dataOutput.writeInt(this.sal);
        dataOutput.writeInt(this.comm);
        dataOutput.writeInt(this.deptno);

    }
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.empno = dataInput.readInt();
        this.ename = dataInput.readUTF();
        this.job = dataInput.readUTF();
        this.mgr =dataInput.readInt();
        this.hiredate = dataInput.readUTF();
        this.sal = dataInput.readInt();
        this.comm = dataInput.readInt();
        this.deptno = dataInput.readInt();
    }

    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getMgr() {
        return mgr;
    }

    public void setMgr(int mgr) {
        this.mgr = mgr;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    public int getSal() {
        return sal;
    }

    public void setSal(int sal) {
        this.sal = sal;
    }

    public int getComm() {
        return comm;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }

    public int getDeptno() {
        return deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }
}
