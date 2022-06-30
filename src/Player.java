import java.util.Vector;

public class Player {

    public enum PlayerType {USER, COMPUTER};
    private final PlayerType type;
    private final Vector<AnimalTool> tools;
    private final Panel[][] board;
    private Panel forcedToEatFrom;
    private final MyGui GUI;

    /**
     * constructor
     * @param type of the player
     * @param board the board to play on
     * @param GUI GUI
     */
    public Player(PlayerType type, Panel[][] board, MyGui GUI) {
        this.GUI = GUI;
        this.type = type;
        this.board = board;
        tools = new Vector<>();
        forcedToEatFrom = null;
    }

    /**
     *
     * @return the type of the player (PLAYER / COMPUTER)
     */
    public PlayerType getType() {
        return type;
    }

    /**
     * add tool to the players tools
     * @param tool to add
     */
    public void addTool(AnimalTool tool){
        tools.add(tool);
    }

    /**
     * remove tool from the players tools
     * @param tool to remove
     */
    public void removeTool(AnimalTool tool){
        tools.remove(tool);
    }

    /**
     * @return string which represents the player move
     */
    public String chooseMove() {
        if(noMoveAvailable()){
            return "NO MOVE AVAILABLE";
        }
        //computer play
        if(canEatList().size() > 0){
            return eatWithRandomTool();
        }
        return stepWithRandomTool();
    }

    public String chooseMoveWithGUI() {
        if(noMoveAvailable()){
            return "NO MOVE AVAILABLE";
        }
        String from = GUI.getSecondClick();
        String to = GUI.getFirstClick();
        if(from.equals("stop") || to.equals("stop")) {
            return "STOP";
        }
        if(from.isEmpty() || to.isEmpty() || from.length() != 2 || to.length() != 2)
            return "EMPTY";
        return convertIndexes(from + "-" + to);
    }

    /**
     * convert the indexes of the input (1 to 0 and so on)
     * @param userInput user input
     * @return converted input indexes number
     */
    private String convertIndexes(String userInput) {
        return  (Character.getNumericValue(userInput.charAt(0)) - 1) + "" +
                (Character.getNumericValue(userInput.charAt(1)) - 1) + "-" +
                (Character.getNumericValue(userInput.charAt(3)) - 1) + "" +
                (Character.getNumericValue(userInput.charAt(4)) - 1);

    }

    /**
     * @return true if there is no move that can be done
     */
    public boolean noMoveAvailable() {
        return canEatList().size() == 0 && canStepList().size() == 0;
    }

    /**
     * choose a random tool to eat with
     * @return string which represents the move
     */
    private String eatWithRandomTool() {
        Vector<AnimalTool> eaters = canEatList();
        AnimalTool multipleEater = getMultipleEater(eaters);
        Panel panel;
        if(multipleEater == null){
            panel = selectRandomToolFromList(eaters);
        }
        else {
            panel = findPanelByTool(multipleEater);
        }
        int fromRow = panel.getRow();
        int fromCol = panel.getColumn();
        Panel to = selectRandomPanelToMove(panel.getTool().availableEats());
        GUI.addLog("CPU - Eat from "  + (fromRow + 1) + "" + (fromCol + 1) + " to " + (to.getRow() + 1) + "" + (to.getColumn() + 1));
        return to.getRow() + "" + to.getColumn() + "-" + fromRow + "" + fromCol;
    }

    /**
     * @param eaters list of tools that can eat
     * @return tool that needs to make another eat
     */
    private AnimalTool getMultipleEater(Vector<AnimalTool> eaters) {
        for (AnimalTool tool :
                eaters) {
            if (tool.isMultipleEat)
                return tool;
        }
        return null;
    }

    /**
     * choose a random tool to step with
     * @return string which represents the move
     */
    private String stepWithRandomTool() {
        Vector<AnimalTool> steppers = canStepList();
        Panel panel = selectRandomToolFromList(steppers);
        int fromRow = panel.getRow();
        int fromCol = panel.getColumn();
        Panel to = selectRandomPanelToMove(panel.getTool().availableSteps());
        GUI.addLog("CPU - Move from "  + (fromRow + 1) + "" + (fromCol + 1) + " to " + (to.getRow() + 1) + "" + (to.getColumn() + 1));
        return to.getRow() + "" + to.getColumn() + "-" + fromRow + "" + fromCol;
    }

    /**
     * @param list of panels the tool can move to
     * @return 1 random panel
     */
    private Panel selectRandomPanelToMove(Vector<Panel> list) {
        return list.get(getRandomNumber(0, list.size()));
    }

    /**
     * @param list of tools
     * @return random panel with tool
     */
    private Panel selectRandomToolFromList(Vector<AnimalTool> list) {
        return findPanelByTool(list.get(getRandomNumber(0, list.size())));
    }

    /**
     * get random number in range
     * @param min include
     * @param max not include
     * @return number in range [min,max)
     */
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * @param tool tool to look for
     * @return the panel of the tool
     */
    private Panel findPanelByTool(AnimalTool tool) {
        for (int i = 0; i < Game.BOARD_SIZE; i++) {
            for (int j = 0; j < Game.BOARD_SIZE; j++) {
                if(tool.equals(board[i][j].getTool()))
                    return board[i][j];
            }
        }
        return null;
    }

    /**
     *
     * @return list of tools that can make a step
     */
    private Vector<AnimalTool> canStepList() {
        Vector<AnimalTool> steppers = new Vector<>();
        for (AnimalTool tool: tools) {
            if(tool.availableSteps().size() > 0)
                steppers.add(tool);
        }
        return steppers;
    }

    /**
     * @return true if there are more tools to the player
     */
    public boolean hasNoTools() {
        return tools.size() == 0;
    }

    /**
     *
     * @return list of tools that can make an eat
     */
    public Vector<AnimalTool> canEatList(){
        Vector<AnimalTool> eaters = new Vector<>();
        for (AnimalTool at: tools) {
            if(at.availableEats().size() > 0)
                eaters.add(at);
        }
        return eaters;
    }

    /**
     * making move on the board
     * @return GameStatus after the move
     */
    public Game.GameStatus makeMove(){
        if(hasNoTools())
            return Game.GameStatus.USER_WON;
        String input = chooseMove();
        if(input.equals("NO MOVE AVAILABLE")){
            return Game.GameStatus.TIE;
        }
        int fromRow = Character.getNumericValue(input.charAt(3));
        int fromCol = Character.getNumericValue(input.charAt(4));
        int toRow = Character.getNumericValue(input.charAt(0));
        int toCol = Character.getNumericValue(input.charAt(1));
        GUI.printCpu("Last move from: " + (fromRow + 1) + "" + (fromCol + 1) + " to " + (toRow + 1) + "" + (toCol + 1));
        AnimalTool.MoveResult moveResult = board[fromRow][fromCol].getTool().moveTo(board[toRow][toCol]);
        if(moveResult == AnimalTool.MoveResult.HAVE_MORE_TO_EAT){
            setStartingPanelToNextMove(board[toRow][toCol]);
            return Game.GameStatus.COMPUTER_TURN;
        }
        else {
            clearStartingPanelToNextMove();
            return Game.GameStatus.USER_TURN;
        }
    }

    public Game.GameStatus makeMoveWithGUI(){
        if(hasNoTools())
            return Game.GameStatus.COMPUTER_WON;
        String input = chooseMoveWithGUI();
        if(input.equals("EMPTY")) {
            GUI.printUser("Please choose a move.");
            return Game.GameStatus.USER_TURN;
        }
        if(input.equals("STOP")) {
            return Game.GameStatus.USER_SURRENDERED;
        }
        if(input.equals("NO MOVE AVAILABLE")){
            return Game.GameStatus.TIE;
        }
        int fromRow = Character.getNumericValue(input.charAt(3));
        int fromCol = Character.getNumericValue(input.charAt(4));
        int toRow = Character.getNumericValue(input.charAt(0));
        int toCol = Character.getNumericValue(input.charAt(1));
        AnimalTool.MoveResult moveResult = AnimalTool.MoveResult.ILLEGAL_MOVE;
        if(isLegalMove(fromRow, fromCol, toRow, toCol)){
            AnimalTool tool = board[fromRow][fromCol].getTool();
            moveResult = tool.moveTo(board[toRow][toCol]);
        }
        if(moveResult == AnimalTool.MoveResult.EAT || moveResult == AnimalTool.MoveResult.HAVE_MORE_TO_EAT)
            GUI.addLog("User - Eat from "  + (fromRow + 1) + "" + (fromCol + 1) + " to " + (toRow + 1) + "" + (toCol + 1));
        if(moveResult == AnimalTool.MoveResult.STEP)
            GUI.addLog("User - Move from "  + (fromRow + 1) + "" + (fromCol + 1) + " to " + (toRow + 1) + "" + (toCol + 1));
        if(moveResult == AnimalTool.MoveResult.ILLEGAL_MOVE){
            GUI.printUser("Illegal move, please try again!");
            return Game.GameStatus.USER_TURN;
        }
        else if(moveResult == AnimalTool.MoveResult.SURRENDERED){
            return Game.GameStatus.USER_SURRENDERED;
        }
        else if(moveResult == AnimalTool.MoveResult.HAVE_MORE_TO_EAT){
            setStartingPanelToNextMove(board[toRow][toCol]);
            GUI.printUser("Your turn again (keep eating)");
            return Game.GameStatus.USER_TURN;
        }
        else {
            GUI.printUser("Please choose a move.");
            clearStartingPanelToNextMove();
            return Game.GameStatus.COMPUTER_TURN;
        }
    }

    /**
     * clear the starting panel to next move
     */
    private void clearStartingPanelToNextMove() {
        forcedToEatFrom = null;
    }

    /**
     * set the starting panel to the next move
     * @param panel to start from
     */
    private void setStartingPanelToNextMove(Panel panel) {
        forcedToEatFrom = panel;
    }

    /**
     * check if the move is legal
     * @param fromRow row of the panel the tool stand on
     * @param fromCol column of the panel the tool stand on
     * @param toRow row of the panel the tool move to
     * @param toCol column of the panel the tool move to
     * @return true if its a legal move
     */
    private boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol) {
        AnimalTool tool = board[fromRow][fromCol].getTool();
        return !(forcedToEatFrom != null && forcedToEatFrom != board[fromRow][fromCol]) &&
                board[toRow][toCol].isEmpty() &&
                tool != null &&
                tool.player == this &&
                (tool.isValidEat(toRow,toCol) || tool.isValidStep(toRow, toCol));

    }

}
