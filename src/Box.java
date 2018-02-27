import java.awt.*;


public class Box {


    private Point currentPosition;
    private boolean hasPersonalRobot;

    @Override
    public int hashCode() {
        return currentPosition.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getClass() != obj.getClass()) return false;
        Box box = (Box) obj;
        return this.currentPosition.equals(box.currentPosition) && this.hasPersonalRobot == box.hasPersonalRobot;
    }

    public Box(Point currentPosition){
        this.currentPosition = currentPosition;
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition) {
        MapInfo.getInstance().getBlocks().remove(this.currentPosition);
        this.currentPosition = currentPosition;
        if(this.currentPosition.equals(MapInfo.getInstance().getFinish())){
            return;
        }
        MapInfo.getInstance().getBlocks().add(currentPosition);
    }

    public boolean isHasPersonalRobot() {
        return hasPersonalRobot;
    }

    public void setHasPersonalRobot(boolean hasPersonalRobot) {
        this.hasPersonalRobot = hasPersonalRobot;
    }
}
