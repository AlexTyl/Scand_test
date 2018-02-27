import java.awt.*;
import java.util.*;

public class Robot {

    private Point currentPosition;
    private Stack<Point> robotPath;
    private Stack<Point> boxPath;
    private Box ownBox;
    private ArrayList<RobotMoveListener> robotMoveListeners = new ArrayList<>();


    public Robot(Point currentPosition){
        this.currentPosition = currentPosition;
    }

    private void moveTo(Point finish){
        robotPath = createPath(currentPosition, finish, false);
        while (!robotPath.empty()){
            currentPosition =  robotPath.pop();
            sendMove();
        }
    }

    private void moveToWithBox(Point finish){
        robotPath = createPath(currentPosition, finish, true);
        while (!robotPath.empty()){
            Point oldPosition = new Point(currentPosition.x, currentPosition.y);
            currentPosition =  robotPath.pop();
            int x = oldPosition.x - currentPosition.x;
            int y = oldPosition.y - currentPosition.y;
            this.ownBox.setCurrentPosition(new Point(ownBox.getCurrentPosition().x - x, ownBox.getCurrentPosition().y - y));
            sendMove();
        }
    }

    private void sendMove(){
        for (RobotMoveListener robotMoveListener : robotMoveListeners){
            robotMoveListener.robotMoveReaction(this);
        }
    }

    private void createBoxPath(){
        boxPath = createPath(ownBox.getCurrentPosition(), MapInfo.getInstance().getFinish(), false);
    }

    public void takeBox(Box box){
        this.ownBox = box;
    }

    public void moveBoxPath(){
        createBoxPath();

        Integer currentRoadTurn = -1;
        Integer currentRoadTurnLong = 0;
        Direction currentDirection = Direction.Default;
        HashMap<Integer, HashMap<Direction, Integer>> road = new HashMap<>();

        Point firtsPoint = ownBox.getCurrentPosition();
        Point secondPoint = boxPath.pop();

        while (true){
            if(firtsPoint.x < secondPoint.x){
                if(currentDirection == Direction.Right){
                    currentRoadTurnLong++;
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                } else {
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                    currentRoadTurn++;
                    currentRoadTurnLong = 1;
                    currentDirection = Direction.Right;
                }
            } else
            if(firtsPoint.x > secondPoint.x){
                if(currentDirection == Direction.Left){
                    currentRoadTurnLong++;
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                } else {
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                    currentRoadTurn++;
                    currentRoadTurnLong = 1;
                    currentDirection = Direction.Left;
                }
            } else
            if(firtsPoint.y < secondPoint.y){
                if(currentDirection == Direction.Down){
                    currentRoadTurnLong++;
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                } else {
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                    currentRoadTurn++;
                    currentRoadTurnLong = 1;
                    currentDirection = Direction.Down;
                }
            } else
            if(firtsPoint.y > secondPoint.y){
                if(currentDirection == Direction.Up){
                    currentRoadTurnLong++;
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                } else {
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                    currentRoadTurn++;
                    currentRoadTurnLong = 1;
                    currentDirection = Direction.Up;
                }
            }
            if(!boxPath.empty()){
                firtsPoint = secondPoint;
                secondPoint = boxPath.pop();
            } else {
                if(currentRoadTurnLong == 1){
                    HashMap<Direction, Integer> currentRoad = new HashMap<>();
                    currentRoad.put(currentDirection, currentRoadTurnLong);
                    road.put(currentRoadTurn, currentRoad);
                }
                break;
            }
        }

        road.forEach((integer, directionIntegerHashMap) -> {
            if(integer == -1) return;
            directionIntegerHashMap.forEach((direction, roadTurnLong) -> {
                if(direction == Direction.Down){
                    int x = this.ownBox.getCurrentPosition().x;
                    int y = this.ownBox.getCurrentPosition().y;
                    this.moveTo(new Point(x, y - 1));
                    x = this.currentPosition.x;
                    y = this.currentPosition.y;
                    this.moveToWithBox(new Point(x, y + roadTurnLong));
                }
                if(direction == Direction.Up){
                    int x = this.ownBox.getCurrentPosition().x;
                    int y = this.ownBox.getCurrentPosition().y;
                    this.moveTo(new Point(x, y + 1));
                    x = this.currentPosition.x;
                    y = this.currentPosition.y;
                    this.moveToWithBox(new Point(x, y - roadTurnLong));
                }
                if(direction == Direction.Right){
                    int x = this.ownBox.getCurrentPosition().x;
                    int y = this.ownBox.getCurrentPosition().y;
                    this.moveTo(new Point(x - 1, y));
                    x = this.currentPosition.x;
                    y = this.currentPosition.y;
                    this.moveToWithBox(new Point(x + roadTurnLong, y));
                }
                if(direction == Direction.Left){
                    int x = this.ownBox.getCurrentPosition().x;
                    int y = this.ownBox.getCurrentPosition().y;
                    this.moveTo(new Point(x + 1, y));
                    x = this.currentPosition.x;
                    y = this.currentPosition.y;
                    this.moveToWithBox(new Point(x - roadTurnLong, y));
                }
            });
        });


    }

    private Stack<Point> createPath(Point start, Point finish, boolean isRobotWithBox){

        Stack<Point> path = new Stack<>();

        if(start.equals(finish)) return path;

        Integer currentRange = 0;
        HashSet<Point> currentPoints = new HashSet<>();
        currentPoints.add(start);
        Map<Integer, HashSet<Point>> rangeOfReach = new HashMap<>();
        rangeOfReach.put(currentRange, currentPoints);
        MapInfo a = MapInfo.getInstance();

        while (!rangeOfReach.get(currentRange).contains(finish)){
            HashSet<Point> buffer = new HashSet<>();

            for (Point point: rangeOfReach.get(currentRange)){
                if(point.x >= 0 && point.y >= 0 && (!MapInfo.getInstance().getBlocks().contains(new Point(point.x + 1, point.y))
                        || isRobotWithBox)){
                    buffer.add(new Point(point.x + 1, point.y));
                }
                if(point.x >= 0 && point.y >= 0 && (!MapInfo.getInstance().getBlocks().contains(new Point(point.x - 1, point.y))
                        || isRobotWithBox)){
                    buffer.add(new Point(point.x - 1, point.y));
                }
                if(point.x >= 0 && point.y >= 0 && (!MapInfo.getInstance().getBlocks().contains(new Point(point.x, point.y + 1))
                        || isRobotWithBox)){
                    buffer.add(new Point(point.x, point.y + 1));
                }
                if(point.x >= 0 && point.y >= 0 && (!MapInfo.getInstance().getBlocks().contains(new Point(point.x, point.y - 1))
                        || isRobotWithBox)){
                    buffer.add(new Point(point.x, point.y - 1));
                }
            }

            rangeOfReach.put(++currentRange, buffer);

        }

        Point point = new Point(finish.x, finish.y);
        path.push(finish);


        for(; currentRange > 0; currentRange--){
            HashSet<Point> points = rangeOfReach.get(currentRange);
            if(points.contains(new Point(point.x + 1, point.y))){
                path.push(new Point(point.x + 1, point.y));
                point = new Point(point.x + 1, point.y);
                continue;
        }
            if(points.contains(new Point(point.x - 1, point.y))){
                path.push(new Point(point.x - 1, point.y));
                point = new Point(point.x - 1, point.y);
                continue;
            }
            if(points.contains(new Point(point.x, point.y + 1))){
                path.push(new Point(point.x, point.y + 1));
                point = new Point(point.x, point.y + 1);
                continue;
            }
            if(points.contains(new Point(point.x, point.y - 1))){
                path.push(new Point(point.x, point.y - 1));
                point = new Point(point.x, point.y - 1);
            }
        }

        return path;

    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }



    public void setRobotMoveListeners(RobotMoveListener robotMoveListeners) {
        this.robotMoveListeners.add(robotMoveListeners);
    }

    public Box getOwnBox() {
        return ownBox;
    }

    public void setOwnBox(Box ownBox) {
        this.ownBox = ownBox;
    }
}
