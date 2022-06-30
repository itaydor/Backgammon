import java.util.Vector;

public class DamkaTool extends Elephant{

    /**
     * constructor
     *
     * @param row         panel row
     * @param column      panel column
     * @param player      players whom this tool belongs to
     * @param board       the board
     * @param GUI         GUI
     */
    public DamkaTool(int row, int column, Player player, Panel[][] board, MyGui GUI) {
        super(row, column, player, board, GUI);
    }

    /**
     * check if the step is valid
     * @param toRow row panel we want to move to
     * @param toCol column panel we want to move to
     * @return true if the step is valid
     */
    @Override
    public boolean isValidStep(int toRow, int toCol) {
        if(!Game.isValidIndexes(toRow, toCol) || !isEmptyPanel(toRow, toCol))
            return false;
        int row = location.getRow();
        int col = location.getColumn();
        if(isQueen){
            return  (((toRow == row - 1) && (toCol == col - 1 || toCol == col + 1)) ||
                    ((toRow == row + 1) && (toCol == col - 1 || toCol == col + 1)));
        }
        else {
            if(player.getType() == Player.PlayerType.USER){
                return  (toRow == row - 1) && (toCol == col - 1 || toCol == col + 1);
            }
            else {
                return  (toRow == row + 1) && (toCol == col - 1 || toCol == col + 1);
            }
        }
    }

    /**
     * @return list of available steps
     */
    @Override
    protected Vector<Panel> availableSteps() {
        Vector<Panel> panels = new Vector<>();
        int row = location.getRow();
        int col = location.getColumn();
        if(isQueen){
            if(row - 1 >= 0 && col - 1 >= 0 && isValidStep(row - 1, col - 1))
                panels.add(board[row - 1][col - 1]);
            if(row - 1 >= 0 && col + 1 < Game.BOARD_SIZE && isValidStep(row - 1, col + 1))
                panels.add(board[row - 1][col + 1]);
            if(row + 1 < Game.BOARD_SIZE && col + 1 < Game.BOARD_SIZE && isValidStep(row + 1, col + 1))
                panels.add(board[row + 1][col + 1]);
            if(row + 1 < Game.BOARD_SIZE && col - 1 >= 0 && isValidStep(row + 1, col - 1))
                panels.add(board[row + 1][col - 1]);
        }
        else {
            if(player.getType() == Player.PlayerType.USER){
                if(row - 1 >= 0 && col - 1 >= 0 && isValidStep(row - 1, col - 1))
                    panels.add(board[row - 1][col - 1]);
                if(row - 1 >= 0 && col + 1 < Game.BOARD_SIZE && isValidStep(row - 1, col + 1))
                    panels.add(board[row - 1][col + 1]);
            }
            else {
                if(row + 1 < Game.BOARD_SIZE && col + 1 < Game.BOARD_SIZE && isValidStep(row + 1, col + 1))
                    panels.add(board[row + 1][col + 1]);
                if(row + 1 < Game.BOARD_SIZE && col - 1 >= 0 && isValidStep(row + 1, col - 1))
                    panels.add(board[row + 1][col - 1]);
            }
        }
        return panels;
    }

    public void makeQueen(){
        isQueen = true;
    }
}
