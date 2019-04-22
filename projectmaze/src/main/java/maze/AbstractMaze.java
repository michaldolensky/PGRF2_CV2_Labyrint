package maze;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class AbstractMaze {
    private int currentLevel = 0;
    private int squareSize = 40;
    private int heightBetweenLevels = 100;
    private Point3D startPosition;
    private List<int[][]> levels = new ArrayList<>();
    private List<String> textureUls = new ArrayList<>();


    public List<int[][]> getLevels() {
        return levels;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int getHeightBetweenLevels() {
        return heightBetweenLevels;
    }


    public Point3D getStartPosition(double offset) {
        return startPosition.addOffset(offset).mul(squareSize);
    }

    public List<String> getTextureUls() {
        return textureUls;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Point3D getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Point3D startPosition) {
        this.startPosition = startPosition;
    }

}
