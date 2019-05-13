package test;

import dao.RoadDao;
import dao.stuDao;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import pojo.Road;
import pojo.stutest;
import util.MyBatisUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ZP on 2019/2/23.
 */
public class ZPTEST {

    private SqlSession openSession = MyBatisUtil.getSession();;
    private stuDao mapper = openSession.getMapper(stuDao.class);

    @Test
    public void getstutest(){
        List<stutest> stutestList = mapper.getstu();
        System.out.println(stutestList.toString());
        MyBatisUtil.closeSession();
    }


    private RoadDao roadDao;
    @Test
    public void TimetoLong() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**
         * 开始时间
         */
        long startTime = sf.parse("2016-10-31 00:00:00").getTime() / 1000L;

        /**
         * 结束时间
         */
        long endTime = sf.parse("2016-10-31 00:05:00").getTime() / 1000L;

        System.out.println(startTime);
        System.out.println(endTime);

        long testtime = 1554630052;
        System.out.println(sf.format(new Date(testtime*1000)));

//        List<Road> roads = roadDao.listRoadByName("咸宁西路");

    }


    @Test
    public void testJython1(){
        PythonInterpreter interp = new PythonInterpreter();
//        PyString str = Py.newStringOrUnicode("颜军");
        System.out.println("Hello, brave new world");
        interp.exec("import sys");
        interp.exec("print sys");
        interp.set("a", new PyInteger(42));
        interp.exec("print a");
        interp.exec("x = 2+2");
        PyObject x = interp.get("x");
        System.out.println("x: "+x);
        System.out.println("Goodbye, cruel world!");

    }

    @Test
    public void testJython(){
        System.setProperty("python.home","D:\\jython2.7.0");
        String python = "F:\\大学\\个人\\挑战杯\\data\\helloworld.py";
        PythonInterpreter interp = new PythonInterpreter();
        interp.execfile(python);
        interp.cleanup();
//        interp.close();
    }



}
