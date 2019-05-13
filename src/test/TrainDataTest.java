package test;

import dao.OriginDataDao;
import dao.RoadDao;
import dao.TrainDataDao;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import pojo.OriginData;
import pojo.Road;
import pojo.TrainData;
import util.MyBatisUtil;
import util.PathQuery;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public class TrainDataTest {


    private SqlSession openSession = MyBatisUtil.getSession();

    private RoadDao roadDao = openSession.getMapper(RoadDao.class);


    private OriginDataDao originDataDao = openSession.getMapper(OriginDataDao.class);


    private TrainDataDao trainDataDao = openSession.getMapper(TrainDataDao.class);

    /**
     * TrainData缓存
     */
    Map<String, TrainData> trainDataMap = new LinkedHashMap<String, TrainData>();

    List<Road> roads;

    Map<Long, List<OriginData>> listMap = new HashMap<Long, List<OriginData>>();

    /**
     * 并发统计每个路段车流量的情况
     *
     * @throws ParseException
     */
    @Test
    public void createTrainData() throws ParseException {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /**
         * 10分钟间隔秒数
         */
        long minuteStep = 600;

        /**
         * 1小时间隔秒数
         */
        long hourStep = 3600;

        /**
         * 开始时间
         */
        long startTime = sf.parse("2016-10-31 00:00:00").getTime() / 1000L;

        /**
         * 结束时间
         */
        long endTime = sf.parse("2016-10-31 00:30:00").getTime() / 1000L;


        long calStart = System.currentTimeMillis();

        System.out.println("开始时间: " + new Timestamp(calStart).toString());

        /**
         * 咸宁西路的所有路段集合
         */
        roads = roadDao.listRoadByName("咸宁西路");

        long s = startTime;
        long e = endTime;

        while (s < e) {
            List<OriginData> originData = originDataDao.listSpecifyTimeOriginData(s, s + minuteStep);
            listMap.put(s, originData);
            s += minuteStep;
        }

        for (int x = 0; x < roads.size(); x++) {
            long start = startTime;
            long end = endTime;
            while (start < end) {
                UUID uuid = UUID.randomUUID();

                TrainData trainData = new TrainData();
                Road road = roads.get(x);
                trainData.setTrainDataId(uuid.toString());
                trainData.setRoadId(roads.get(x).getRoadId());
                trainData.setStartLongitude(road.getStartLongitude());
                trainData.setStartLatitude(road.getStartLatitude());
                trainData.setEndLongitude(road.getEndLongitude());
                trainData.setEndLatitude(road.getEndLatitude());
                trainData.setStartTime(new Timestamp(start * 1000L));
                trainData.setEndTime(new Timestamp((start + minuteStep) * 1000L));
                trainData.setCarNumber(0);
                trainData.setCreateTime(new Timestamp(System.currentTimeMillis()));
                trainDataMap.put(road.getRoadId() + start, trainData);
                start += minuteStep;
            }
        }

        Iterator<Map.Entry<String, TrainData>> iterator = trainDataMap.entrySet().iterator();
        CountDownLatch countDownLatch = new CountDownLatch(6);

        int count = 0;

        while (iterator.hasNext()) {
            Map.Entry<String, TrainData> entry = iterator.next();
            TrainData data = entry.getValue();
            new Thread(new countThread(data.getRoadId(), data.getStartTime(), data.getEndTime(), countDownLatch)).start();
            count++;
            if (count == 8) {
                try {
                    countDownLatch.await();
                    countDownLatch = new CountDownLatch(8);
                    count = 0;
                } catch (InterruptedException a) {
                    a.printStackTrace();
                }
            }
        }

        Iterator<Map.Entry<String, TrainData>> iterator2 = trainDataMap.entrySet().iterator();

        while (iterator2.hasNext()) {
            // TrainData trainData = iterator2.next().getValue();
            trainDataDao.saveTrainData(iterator2.next().getValue());
        }

        long calEndTime = System.currentTimeMillis();

        System.out.println("结束时间: " + new Timestamp(calEndTime).toString());
        System.out.println("耗时: " + (calEndTime - calStart) / 1000.0 / 3600.0 + " 小时.....");

    }

    class countThread implements Runnable {

        private Timestamp subStartTime;
        private Timestamp subEndTime;

        private CountDownLatch countDownLatch;
        private String roadId;

        public String getRoadId() {
            return roadId;
        }

        public void setRoadId(String roadId) {
            this.roadId = roadId;
        }

        private Set<String> set = new HashSet<String>();

        public Timestamp getSubStartTime() {
            return subStartTime;
        }

        public void setSubStartTime(Timestamp subStartTime) {
            this.subStartTime = subStartTime;
        }

        public Timestamp getSubEndTime() {
            return subEndTime;
        }

        public void setSubEndTime(Timestamp subEndTime) {
            this.subEndTime = subEndTime;
        }

        public countThread(String roadId, Timestamp subStartTime, Timestamp subEndTime, CountDownLatch countDownLatch) {
            this.roadId = roadId;
            this.subStartTime = subStartTime;
            this.subEndTime = subEndTime;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {

            Long start = subStartTime.getTime() / 1000L;
            Long end = subEndTime.getTime() / 1000L;

            /**
             * 获取这个路段特定的点
             */
            List<OriginData> originData = listMap.get(start);

            for (int x = 0; x < originData.size(); x++) {
                OriginData o = originData.get(x);
                String roadId = PathQuery.query(o.getLongitude(), o.getLatitude(), roads);
                if (this.roadId.equals(roadId)) {
                    if (set.contains(o.getDeviceId())) {
                        continue;
                    } else {
                        synchronized (countThread.class) {
                            set.add(o.getDeviceId());
                            TrainData t = trainDataMap.get(roadId + start);
                            t.setCarNumber(t.getCarNumber() + 1);
                        }
                    }
                }
            }
            countDownLatch.countDown();
        }

    }

}
