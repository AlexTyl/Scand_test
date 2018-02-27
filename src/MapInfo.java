import com.sun.glass.ui.Size;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;


public class MapInfo {

    private static MapInfo ourInstance;

    private Size size = new Size();
    private HashSet<Point> blocks = new HashSet<>();
    private ArrayList<Box> boxes = new ArrayList<>();
    private Point start = new Point();
    private Point finish = new Point();

    public static MapInfo getInstance() {
        return ourInstance == null ? ourInstance = getMapInfo() : ourInstance;
    }

    private MapInfo(Size size, HashSet<Point> blocks, ArrayList<Box> boxes, Point start, Point finish) {
        this.size = size;
        this.blocks = blocks;
        this.boxes = boxes;
        this.start = start;
        this.finish = finish;
    }


    private static MapInfo getMapInfo(){

        Point finish = new Point(7 , 4);
        ArrayList<Box> boxes = new ArrayList<>();
        HashSet<Point> blocks = new HashSet<>();
        Box box = new Box(new Point(2, 1));
        Box box1 = new Box(new Point(6, 2));
        Box box2 = new Box(new Point(7, 3));
        boxes.add(box);
        blocks.add(box.getCurrentPosition());
        boxes.add(box1);
        blocks.add(box1.getCurrentPosition());
        boxes.add(box2);
        blocks.add(box2.getCurrentPosition());

        return new MapInfo(new Size(12, 17), blocks, boxes, null, finish);
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public HashSet<Point> getBlocks() {
        return blocks;
    }

    public void setBlocks(HashSet<Point> blocks) {
        this.blocks = blocks;
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(ArrayList<Box> boxes) {
        this.boxes = boxes;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getFinish() {
        return finish;
    }

    public void setFinish(Point finish) {
        this.finish = finish;
    }
}
