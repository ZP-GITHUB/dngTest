package dao;

import org.apache.ibatis.annotations.Param;
import pojo.OriginData;

import java.util.List;

/**
 * Created by ZP on 2019/2/23.
 */
public interface OriginDataDao {

    public List<OriginData> listSpecifyTimeOriginData(@Param("s") Long s,@Param("e") Long e);
}
