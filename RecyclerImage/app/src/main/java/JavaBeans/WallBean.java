package JavaBeans;

public class WallBean {

    private long wallID;        //表白墙item的id

    private int startTime;   //一项的起始时间

    private int stopTime;    //终止时间

    private int picket;         //此时间段剩余的票数

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getPicket() {
        return picket;
    }

    public void setPicket(int picket) {
        this.picket = picket;
    }

    public int getStopTime() {

        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public long getWallID() {

        return wallID;

    }

    public void setWallID(long wallID) {
        this.wallID = wallID;
    }
}
