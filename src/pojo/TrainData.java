package pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Administrator
 */
public class TrainData implements Serializable {

    private static final long serialVersionUID = -7435936430880302747L;

    private String trainDataId;

    private String roadId;

    private Double startLongitude;
    private Double startLatitude;

    private Double endLongitude;
    private Double endLatitude;

    private Integer carNumber;

    private Timestamp startTime;
    private Timestamp endTime;

    private Timestamp createTime;
    private Timestamp updateTime;

    @Override
    public int hashCode() {
        return roadId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TrainData) {
            if (((TrainData) obj).getStartTime().equals(startTime) && ((TrainData) obj).getEndTime().equals(endTime)) {
                return true;
            }
        }
        return false;
    }

    public String getTrainDataId() {
        return trainDataId;
    }

    public void setTrainDataId(String trainDataId) {
        this.trainDataId = trainDataId;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public Double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public Double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public Double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public Double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public Integer getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(Integer carNumber) {
        this.carNumber = carNumber;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public TrainData() {
    }
}
