import java.util.Vector;

public class Elephant extends AnimalTool {

    protected boolean isQueen;

    /**
     * constructor
     * @param row panel row
     * @param column panel column
     * @param player players whom this tool belongs to
     * @param board the board
     */
    public Elephant(int row, int column, Player player, Panel[][] board, MyGui gameWithGui) {
        super(row, column, player, board, gameWithGui);
        isQueen = false;
    }

    /**
     * make the tool to a queen
     */
    private void becameQueen(){
        isQueen = true;
        if (player.getType() == Player.PlayerType.USER) {
            GUI.addLog("User - New queen in " + (location.getRow() + 1) + "" + (location.getColumn() + 1));
        } else {
            GUI.addLog("CPU - New queen in " + (location.getRow() + 1) + "" + (location.getColumn() + 1));
        }
        GUI.putTool(this, location.getRow(), location.getColumn());
    }

    /**
     * move this tool the panel
     * @param panel to step to
     * @return MoveResult
     */
    @Override
    public MoveResult moveTo(Panel panel) {
        MoveResult moveResult = super.moveTo(panel);
        if(moveResult != MoveResult.HAVE_MORE_TO_EAT && gotToLastRow(panel)){
            becameQueen();
        }
        return moveResult;
    }

    /**
     * check if the tool got to the last row
     * @param panel to check
     * @return true if the tool got to the last row
     */
    private boolean gotToLastRow(Panel panel) {
        return  (panel.getRow() == 0 && player.getType() == Player.PlayerType.USER) || (panel.getRow() == Game.BOARD_SIZE - 1 && player.getType() == Player.PlayerType.COMPUTER);
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
        if(isQueen){
            return isInDistance(toRow, toCol, 2, 2);
        }
        else {
            if(!isInDistance("col", toCol, 2)) {
                return false;
            }
            int row = location.getRow();
            return player.getType() == Player.PlayerType.USER? toRow == row - 2 : toRow == row + 2;
        }
    }

    /**
     * check if eat is valid
     * @param row row pane we want to move to
     * @param col column pane we want to move to
     * @return  true if eat is valid
     */
    @Override
    public boolean isValidEat(int row, int col) {
        if(!Game.isValidIndexes(row, col))
            return false;
        Panel to = board[row][col];
        if(isQueen || isMultipleEat){
            return isEatUp(to) || isEatDown(to);
        }
        else {
            return super.isValidEat(row, col);
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

        if(isValidStep(row - 2, col - 2))
            panels.add(board[row - 2][col - 2]);
        if(isValidStep(row - 2, col + 2))
            panels.add(board[row - 2][col + 2]);
        if(isValidStep(row + 2, col + 2))
            panels.add(board[row + 2][col + 2]);
        if(isValidStep(row + 2, col - 2))
            panels.add(board[row + 2][col - 2]);

        return panels;
    }

    /**
     * @return list of available eats
     */
    protected Vector<Panel> availableEats(){
        if(isQueen || isMultipleEat){
            Vector<Panel> panels = new Vector<>();

            int row = location.getRow();
            int col = location.getColumn();

            if(isValidEat(row - 2,col - 2))
                panels.add(board[row - 2][col - 2]);
            if(isValidEat(row - 2,col + 2))
                panels.add(board[row - 2][col + 2]);
            if(isValidEat(row + 2,col + 2))
                panels.add(board[row + 2][col + 2]);
            if(isValidEat(row + 2,col - 2))
                panels.add(board[row + 2][col - 2]);

            return panels;
        }
        else {
            return super.availableEats();
        }
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
            return "E1" + queen;
        }
        else {
            return "E2" + queen;
        }
    }
}
