package dao;

import org.apache.ibatis.annotations.Param;
import pojo.Road;

import java.util.List;

/**
 * Created by ZP on 2019/2/23.
 */
public interface RoadDao {

    public List<Road> listRoadByName(@Param("road") String road);
}
