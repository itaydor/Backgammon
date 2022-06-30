import java.util.Vector;

public class Kangaroo extends Elephant {

    private Panel lastEat;

    public Kangaroo(int row, int column, Player player, Panel[][] board, MyGui gameWithGui) {
        super(row, column, player, board, gameWithGui);
        lastEat = null;
    }

    /**
     * jump over an enemy
     * @param newLocation panel behind the enemy
     * @return MoveResult
     */
    @Override
    public MoveResult eatTo(Panel newLocation){
        lastEat = getEatenToolsPanel(getEatDirection(newLocation.getRow(), newLocation.getColumn()));
        changeLocation(newLocation);
        multipleEatOn();
        if(haveMoreToEat())
            return MoveResult.HAVE_MORE_TO_EAT;
        lastEat = null;
        multipleEatOff();
        return MoveResult.EAT;
    }

    /**
     * check if finish eating.
     * @return true if we have more eats to do.
     */
    @Override
    public boolean haveMoreToEat() {
        Vector<Panel> availableEats = availableEats();
        availableEats.remove(lastEat);
        return !availableEats.isEmpty();

//        int row = location.getRow();
//        int col = location.getColumn();
//        return  (row - 2 >= 0 && col - 2 >= 0 && !board[row - 1][col - 1].equals(lastEat) && isValidEat(row - 2,col - 2)) ||
//                (row + 2 < Game.BOARD_SIZE && col + 2 < Game.BOARD_SIZE && !board[row + 1][col + 1].equals(lastEat) && isValidEat(row + 2,col + 2)) ||
//                (row + 2 < Game.BOARD_SIZE && col - 2 >= 0 && !board[row + 1][col - 1].equals(lastEat) && isValidEat(row + 2,col - 2)) ||
//                (row - 2 >= 0 && col + 2 < Game.BOARD_SIZE && !board[row - 1][col + 1].equals(lastEat) && isValidEat(row - 2,col + 2));
    }

    /**
     * @return list of available eats
     */
    @Override
    protected Vector<Panel> availableEats(){
        Vector<Panel> panels = new Vector<>();
        int row = location.getRow();
        int col = location.getColumn();
        //check if row is legal,col is legal, its not trying to eat again the last tool in case of multi eating and check if its a valid eat.
        if(isQueen || isMultipleEat){
            if(isValidEat(row - 2,col - 2) && !board[row - 1][col - 1].equals(lastEat))
                panels.add(board[row - 2][col - 2]);
            if(isValidEat(row - 2,col + 2) && !board[row - 1][col + 1].equals(lastEat))
                panels.add(board[row - 2][col + 2]);
            if(isValidEat(row + 2,col + 2) && !board[row + 1][col + 1].equals(lastEat))
                panels.add(board[row + 2][col + 2]);
            if(isValidEat(row + 2,col - 2) && !board[row + 1][col - 1].equals(lastEat))
                panels.add(board[row + 2][col - 2]);
        }
        else{
            if(isValidEat(row - 2,col - 2))
                panels.add(board[row - 2][col - 2]);
            if(isValidEat(row - 2,col + 2))
                panels.add(board[row - 2][col + 2]);
            if(isValidEat(row + 2,col + 2))
                panels.add(board[row + 2][col + 2]);
            if(isValidEat(row + 2,col - 2))
                panels.add(board[row + 2][col - 2]);
        }
        return panels;
    }

    /**
     * @return the tool name
     */
    public String toString(){
        String queen = "";
        if(isQueen){
            queen = "Q";
        }
        if(this.player.getType() == Player.PlayerType.USER){
            return "M1" + queen;
        }
        else {
            return "M2" + queen;
        }
    }
}
