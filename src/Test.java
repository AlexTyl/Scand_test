import java.awt.*;


public class Test {

    public static void main(String[] args){

        Robot robot = new Robot(new Point(0, 3));
        robot.setRobotMoveListeners(robot1 -> System.out.println("robot " + robot1.getCurrentPosition()));


        for (Box box : MapInfo.getInstance().getBoxes()){
            robot.takeBox(box);
            robot.moveBoxPath();
        }


    }

}
