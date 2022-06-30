import java.util.Vector;

public abstract class AnimalTool {

    protected Panel location;
    protected Panel[][] board;
    protected final Player player;
    protected boolean isMultipleEat;
    public enum ToolType {DOG, CAT, ELEPHANT, KANGAROO, DAMKA}
    public enum MoveResult {STEP, ILLEGAL_MOVE, EAT, HAVE_MORE_TO_EAT, SURRENDERED}
    public enum Direction {UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NONE}
    protected MyGui GUI;

    /**
     * constructor
     * @param row panel row
     * @param column panel column
     * @param player players whom this tool belongs to
     * @param board the board
     */
    public AnimalTool(int row, int column, Player player, Panel[][] board, MyGui GUI){
        this.GUI = GUI;
        location = board[row][column];
        this.player = player;
        this.board = board;
        isMultipleEat = false;
    }

    /**
     * check if there is enemy on the panel.
     * @param panel - panel on the board
     * @return true if there is an enemy on the panel.
     */
    protected boolean isEnemy(Panel panel) {
        return panel.getTool() != null && panel.getTool().player != this.player;
    }

    /**
     * make a move to panel (step or eat)
     * @param panel - move to this panel
     * @return MoveResult
     */
    public MoveResult moveTo(Panel panel) {
        MoveResult moveResult = MoveResult.ILLEGAL_MOVE;
        if(isValidEat(panel.getRow(), panel.getColumn())){
            moveResult = eatTo(panel);
        }
        else if(isValidStep(panel.getRow(), panel.getColumn())){
            moveResult = stepTo(panel);
        }
        return moveResult;
    }

    /**
     * make step with the tool
     * @param to panel to step to
     * @return MoveResult
     */
    public MoveResult stepTo(Panel to){
        changeLocation(to);
        return MoveResult.STEP;
    }

    /**
     * eat and move to newLocation
     * @param newLocation panel behind the enemy
     * @return MoveResult
     */
    public MoveResult eatTo(Panel newLocation){
        removeTool(getEatenToolsPanel(getEatDirection(newLocation.getRow(), newLocation.getColumn())));
        changeLocation(newLocation);
        multipleEatOn();
        if(haveMoreToEat())
            return MoveResult.HAVE_MORE_TO_EAT;
        multipleEatOff();
        return MoveResult.EAT;
    }

    /**
     * remove the tool from panelToEat and from its players list of tools
     * @param panelToEat from the tool
     */
    private void removeTool(Panel panelToEat) {
        GUI.removeTool(panelToEat.getRow(), panelToEat.getColumn());
        AnimalTool tool = panelToEat.getTool();
        tool.player.removeTool(tool);
        panelToEat.removeTool();
    }

    /**
     * change tool location to newLocation
     * @param newLocation to move
     */
    protected void changeLocation(Panel newLocation) {
        GUI.removeTool(location.getRow(), location.getColumn());
        location.removeTool();
        location = newLocation;
        GUI.putTool(this, newLocation.getRow(), newLocation.getColumn());
        newLocation.setTool(this);
    }

    /**
     * check if the step is valid
     * @param row row panel we want to move to
     * @param col column panel we want to move to
     * @return true if the move is valid
     */
    public abstract boolean isValidStep(int row, int col);

    /**
     * check if eat direction is valid
     * @param row row pane we want to move to
     * @param col column pane we want to move to
     * @return  true if eat direction is valid
     */
    public boolean isValidEat(int row, int col) {
        if(!Game.isValidIndexes(row, col))
            return false;
        Panel to = board[row][col];

        return player.getType() == Player.PlayerType.USER? isEatUp(to) : isEatDown(to);
    }

    /**
     * check if the panel is under this panel and between those panels there is an enemy.
     * @param panel behind the enemy
     * @return true if the panel is under this panel and between those panels there is an enemy.
     */
    protected boolean isEatDown(Panel panel) {
        Direction direction = getEatDirection(panel.getRow(), panel.getColumn());
        return direction == Direction.DOWN_LEFT || direction == Direction.DOWN_RIGHT;
    }

    /**
     * check if the panel is above this panel and between those panels there is an enemy.
     * @param panel behind the enemy
     * @return true if the panel is above this panel and between those panels there is an enemy.
     */
    protected boolean isEatUp(Panel panel) {
        Direction direction = getEatDirection(panel.getRow(), panel.getColumn());
        return direction == Direction.UP_LEFT || direction == Direction.UP_RIGHT;
    }

    /**
     * check if finish eating.
     * @return true if we have more eats to do.
     */
    public boolean haveMoreToEat() {
        return !availableEats().isEmpty();
    }

    /**
     * @return list of available steps this tool can do.
     */
    protected abstract Vector<Panel> availableSteps();

    /**
     * @return list of available eats this tool can do.
     */
    protected Vector<Panel> availableEats(){
        Vector<Panel> panels = new Vector<>();
        int row = location.getRow();
        int col = location.getColumn();
        if(isAvailableEat(row - 2, col - 2))
            panels.add(board[row - 2][col - 2]);
        if(isAvailableEat(row - 2, col + 2))
            panels.add(board[row - 2][col + 2]);
        if(isAvailableEat(row + 2, col + 2))
            panels.add(board[row + 2][col + 2]);
        if(isAvailableEat(row + 2, col - 2))
            panels.add(board[row + 2][col - 2]);
        return panels;
    }

    private boolean isAvailableEat(int row, int col) {
        return Game.isValidIndexes(row, col) && isValidEat(row, col);
    }

    /**
     * get eat direction
     * @param row row the tool move to
     * @param col column the tool move to
     * @return the direction of the eat
     */
    protected Direction getEatDirection(int row, int col){
        if(!Game.isValidIndexes(row, col) || !isEmptyPanel(row, col) || !isInDistance(row, col, 2, 2))
            return Direction.NONE;

        Direction direction = getDirection(row, col);

        return switch (direction){
            case NONE -> Direction.NONE;
            case UP_LEFT -> isEnemy(board[location.getRow() - 1][location.getColumn() - 1])? direction : Direction.NONE;
            case UP_RIGHT -> isEnemy(board[location.getRow() - 1][location.getColumn() + 1])? direction : Direction.NONE;
            case DOWN_LEFT -> isEnemy(board[location.getRow() + 1][location.getColumn() - 1])? direction : Direction.NONE;
            case DOWN_RIGHT -> isEnemy(board[location.getRow() + 1][location.getColumn() + 1])? direction : Direction.NONE;
            case UP -> isEnemy(board[location.getRow() - 1][location.getColumn()])? direction : Direction.NONE;
            case DOWN -> isEnemy(board[location.getRow() + 1][location.getColumn()])? direction : Direction.NONE;
            case LEFT -> isEnemy(board[location.getRow()][location.getColumn() - 1])? direction : Direction.NONE;
            case RIGHT -> isEnemy(board[location.getRow()][location.getColumn() + 1])? direction : Direction.NONE;
        };
    }

    protected boolean isInDistance(String type, int ind, int distance){
        return switch (type){
            case "row" -> location.getRow() - distance == ind || location.getRow() + distance == ind;
            case "col" -> location.getColumn() - distance == ind || location.getColumn() + distance == ind;
            default -> false;
        };
    }

    protected boolean isInDistance(int row, int col, int rowDis, int colDis){
        return (location.getRow() - rowDis == row || location.getRow() + rowDis == row) &&
                (location.getColumn() - colDis == col || location.getColumn() + colDis == col);
    }

    /**
     * @param direction the direction of the eat
     * @return the panel on the eaten tool
     */
    protected Panel getEatenToolsPanel(Direction direction) {
        int row = location.getRow();
        int col = location.getColumn();
        Panel panelToClear = null;
        switch (direction){
            case UP_LEFT -> panelToClear = board[row - 1][col - 1];
            case UP_RIGHT -> panelToClear = board[row - 1][col + 1];
            case DOWN_LEFT -> panelToClear = board[row + 1][col - 1];
            case DOWN_RIGHT -> panelToClear = board[row + 1][col + 1];
        }
        return panelToClear;
    }

    /**
     * turn on the multiple eat mode
     */
    public void multipleEatOn() {
        isMultipleEat = true;
    }

    /**
     * turn off the multiple eat mode
     */
    public void multipleEatOff() {
        isMultipleEat = false;
    }

    protected boolean isEmptyPanel(int row, int col){
        return board[row][col].isEmpty();
    }

    protected Direction getDirection(int toRow, int toCol){
        if(toRow == location.getRow() && toCol == location.getColumn())
            return Direction.NONE;
        if(toRow < location.getRow() && toCol < location.getColumn())
            return Direction.UP_LEFT;
        if(toRow < location.getRow() && toCol > location.getColumn())
            return Direction.UP_RIGHT;
        if(toRow > location.getRow() && toCol > location.getColumn())
            return Direction.DOWN_RIGHT;
        if(toRow > location.getRow() && toCol < location.getColumn())
            return Direction.DOWN_LEFT;
        if(toRow == location.getRow() && toCol < location.getColumn())
            return Direction.LEFT;
        if(toRow == location.getRow() && toCol > location.getColumn())
            return Direction.RIGHT;
        if(toRow > location.getRow() && toCol == location.getColumn())
            return Direction.DOWN;
        // toRow < location.getRow() && toCol == location.getColumn()
        return Direction.UP;
    }

//    protected Direction getAttackDirection(){
//        return player.getType() == Player.PlayerType.USER? Direction.UP : Direction.DOWN;
//    }

}
