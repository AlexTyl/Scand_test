

public enum  Direction {
    Right,
    Left,
    Up,
    Down,
    Default;

    public int getStepByOx() {
        return this == Direction.Left? -1: this == Direction.Right? 1: 0;
    }
    public int getStepByOy() {
        return this == Direction.Up? -1: this == Direction.Down? 1: 0;
    }
}
