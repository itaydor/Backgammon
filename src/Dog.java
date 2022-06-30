import java.util.Vector;

public class Dog extends AnimalTool{

    /**
     * constructor
     * @param row panel row
     * @param column panel column
     * @param player players whom this tool belongs to
     * @param board the board
     */
    public Dog(int row, int column, Player player, Panel[][] board, MyGui gameWithGui) {
        super(row, column, player, board, gameWithGui);
    }

    /**
     * Dog cannot do more then 1 eat so return always false
     * @return false
     */
    @Override
    public boolean haveMoreToEat() {
        return false;
    }

    /**
     * @return list of available steps
     */
    @Override
    protected Vector<Panel> availableSteps() {
        Vector<Panel> panels = new Vector<>();
        int row = location.getRow();
        int col = location.getColumn();

        if(isValidStep(row - 1, col - 1))
            panels.add(board[row - 1][col - 1]);
        if(isValidStep(row - 1, col + 1))
            panels.add(board[row - 1][col + 1]);
        if(isValidStep(row + 1, col - 1))
            panels.add(board[row + 1][col - 1]);
        if(isValidStep(row + 1, col + 1))
            panels.add(board[row + 1][col + 1]);

        return panels;
    }

    /**
     * @return list of available eats
     */
    @Override
    protected Vector<Panel> availableEats() {
        Vector<Panel> panels = new Vector<>();
        int row = location.getRow();
        int col = location.getColumn();

        if(isValidEat(row - 2, col - 2))
            panels.add(board[row - 2][col - 2]);
        if(isValidEat(row - 2, col + 2))
            panels.add(board[row - 2][col + 2]);
        if(isValidEat(row + 2, col - 2))
            panels.add(board[row + 2][col - 2]);
        if(isValidEat(row + 2, col + 2))
            panels.add(board[row + 2][col + 2]);

        return panels;
    }

    /**
     * check if the step is valid
     * @param toRow row panel we want to move to
     * @param toCol column panel we want to move to
     * @return true if the step is valid
     */
    @Override
    public boolean isValidStep(int toRow, int toCol) {
        if(!Game.isValidIndexes(toRow, toCol) || !isEmptyPanel(toRow, toCol) || !isInDistance("col", toCol, 1))
            return false;

        return player.getType() == Player.PlayerType.USER? toRow == location.getRow() - 1 : toRow == location.getRow() + 1;
    }

    /**
     * @return the tool name
     */
    @Override
    public String toString(){
        if(this.player.getType() == Player.PlayerType.USER){
            return "D1";
        }
        else {
            return "D2";
        }
    }
}
