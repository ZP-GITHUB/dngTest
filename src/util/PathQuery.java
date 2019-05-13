package util;

import pojo.Road;
import org.python.core.PyFloat;
import org.python.core.PyFunction;
import org.python.core.PyList;
import org.python.util.PythonInterpreter;

import java.util.List;
import java.util.Properties;

/**
 * @author Administrator
 */
public class PathQuery {

    private static PythonInterpreter interpreter;
    private static PyFunction func;

    static {
        Properties props = new Properties();
        props.put("python.home", "path to the Lib folder");
        props.put("python.console.encoding", "UTF-8");
        props.put("python.security.respectJavaAccessibility", "false");
        props.put("python.import.site", "false");
        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops, props, new String[0]);
        interpreter = new PythonInterpreter();
        interpreter.execfile("F:\\大学\\个人\\挑战杯\\data\\py_find_road.py");
        func = (PyFunction) interpreter.get("get_car_to_road", PyFunction.class);
    }

    /**
     * 调用归路算法
     *
     * @param deviceLongitude
     * @param deviceLatitude
     * @param roadList
     * @return
     */
    public static String query(Double deviceLongitude, Double deviceLatitude, List<Road> roadList) {
        return func.__call__(new PyFloat(deviceLongitude), new PyFloat(deviceLatitude), new PyList(roadList)).toString();
    }

}
