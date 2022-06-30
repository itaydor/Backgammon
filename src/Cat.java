import java.util.Vector;

public class Cat extends AnimalTool {

    /**
     * constructor
     *
     * @param row         panel row
     * @param column      panel column
     * @param player      players whom this tool belongs to
     * @param board       the board
     * @param gameWithGui
     */
    public Cat(int row, int column, Player player, Panel[][] board, MyGui gameWithGui) {
        super(row, column, player, board, gameWithGui);
    }

    /**
     * check if the step is valid
     *
     * @param toRow row panel we want to move to
     * @param toCol column panel we want to move to
     * @return true if the step is valid
     */
    @Override
    public boolean isValidStep(int toRow, int toCol) {
        if (!Game.isValidIndexes(toRow, toCol) || !isEmptyPanel(toRow, toCol))
            return false;

        return isInDistance(toRow, toCol, 1, 1) ||
                isInDistance(toRow, toCol, 1, 0) ||
                isInDistance(toRow, toCol, 0, 1);
    }

    /**
     * check if the eat is valid
     *
     * @param row row pane we want to move to
     * @param col column pane we want to move to
     * @return true if the eat is valid
     */
    @Override
    public boolean isValidEat(int row, int col) {
        if (!Game.isValidIndexes(row, col))
            return false;
        Panel to = board[row][col];
        return to.isEmpty() && (isEatUp(to) || isEatDown(to));
    }

    /**
     * @return list of available steps
     */
    @Override
    protected Vector<Panel> availableSteps() {
        Vector<Panel> panels = new Vector<>();
        int row = location.getRow();
        int col = location.getColumn();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int rowToMove = row + i;
                int colToMove = col + j;
                if (isValidStep(rowToMove, colToMove))
                    panels.add(board[rowToMove][colToMove]);
            }
        }
        return panels;
    }

    /**
     * @return the tool name
     */
    @Override
    public String toString() {
        if (this.player.getType() == Player.PlayerType.USER) {
            return "C1";
        } else {
            return "C2";
        }
    }
}
