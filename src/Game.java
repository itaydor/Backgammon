import java.util.Arrays;

public class Game {

    public static final int BOARD_SIZE = 8;
    private final Player user;
    private final Player computer;
    private final Panel[][] board;
    private GameStatus gameStatus;
    public enum GameStatus {USER_WON, COMPUTER_WON, USER_TURN, COMPUTER_TURN, TIE, USER_SURRENDERED}
    public enum GameType {CHECKERS, ANIMALS_CHECKERS}
    private GameType gameType;

    private final MyGui GUI;


    /**
     * constructor
     */
    public Game(MyGui GUI, GameType gameType){
        this.gameType = gameType;
        this.GUI = GUI;
        board = new Panel[BOARD_SIZE][BOARD_SIZE];
        clearBoard();
        user = new Player(Player.PlayerType.USER, board, GUI);
        computer = new Player(Player.PlayerType.COMPUTER, board, GUI);
        gameStatus = GameStatus.USER_TURN;
    }

    /**
     * create empty board
     */
    private void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Panel(i, j);
            }
        }
    }

    /**
     * initial board
     */
    private void initBoard() {
        clearBoard();
        //initial player1 tools
        addTool(AnimalTool.ToolType.CAT, 7, 0, user);
        addTool(AnimalTool.ToolType.DOG, 7, 2, user);
        addTool(AnimalTool.ToolType.ELEPHANT, 7, 4, user);
        addTool(AnimalTool.ToolType.KANGAROO, 7, 6, user);
        addTool(AnimalTool.ToolType.DOG, 6, 1, user);
        addTool(AnimalTool.ToolType.ELEPHANT, 6, 3, user);
        addTool(AnimalTool.ToolType.KANGAROO, 6, 5, user);
        addTool(AnimalTool.ToolType.CAT, 6, 7, user);
        addTool(AnimalTool.ToolType.ELEPHANT, 5, 0, user);
        addTool(AnimalTool.ToolType.KANGAROO, 5, 2, user);
        addTool(AnimalTool.ToolType.CAT, 5, 4, user);
        addTool(AnimalTool.ToolType.DOG, 5, 6, user);
        //initial player2 tools
        addTool(AnimalTool.ToolType.CAT, 0, 1, computer);
        addTool(AnimalTool.ToolType.DOG, 0, 3, computer);
        addTool(AnimalTool.ToolType.ELEPHANT, 0, 5, computer);
        addTool(AnimalTool.ToolType.KANGAROO, 0, 7, computer);
        addTool(AnimalTool.ToolType.DOG, 1, 0, computer);
        addTool(AnimalTool.ToolType.ELEPHANT, 1, 2, computer);
        addTool(AnimalTool.ToolType.KANGAROO, 1, 4, computer);
        addTool(AnimalTool.ToolType.CAT, 1, 6, computer);
        addTool(AnimalTool.ToolType.ELEPHANT, 2, 1, computer);
        addTool(AnimalTool.ToolType.KANGAROO, 2, 3, computer);
        addTool(AnimalTool.ToolType.CAT, 2, 5, computer);
        addTool(AnimalTool.ToolType.DOG, 2, 7, computer);
        //adding the tools to each player
        addToolsToPlayers();
        GUI.makeVisible();
    }

    private void initCheckersBoard() {
        clearBoard();
        //initial player1 tools
        addTool(AnimalTool.ToolType.DAMKA, 7, 0, user);
        addTool(AnimalTool.ToolType.DAMKA, 7, 2, user);
        addTool(AnimalTool.ToolType.DAMKA, 7, 4, user);
        addTool(AnimalTool.ToolType.DAMKA, 7, 6, user);
        addTool(AnimalTool.ToolType.DAMKA, 6, 1, user);
        addTool(AnimalTool.ToolType.DAMKA, 6, 3, user);
        addTool(AnimalTool.ToolType.DAMKA, 6, 5, user);
        addTool(AnimalTool.ToolType.DAMKA, 6, 7, user);
        addTool(AnimalTool.ToolType.DAMKA, 5, 0, user);
        addTool(AnimalTool.ToolType.DAMKA, 5, 2, user);
        addTool(AnimalTool.ToolType.DAMKA, 5, 4, user);
        addTool(AnimalTool.ToolType.DAMKA, 5, 6, user);
        //initial player2 tools
        addTool(AnimalTool.ToolType.DAMKA, 0, 1, computer);
        addTool(AnimalTool.ToolType.DAMKA, 0, 3, computer);
        addTool(AnimalTool.ToolType.DAMKA, 0, 5, computer);
        addTool(AnimalTool.ToolType.DAMKA, 0, 7, computer);
        addTool(AnimalTool.ToolType.DAMKA, 1, 0, computer);
        addTool(AnimalTool.ToolType.DAMKA, 1, 2, computer);
        addTool(AnimalTool.ToolType.DAMKA, 1, 4, computer);
        addTool(AnimalTool.ToolType.DAMKA, 1, 6, computer);
        addTool(AnimalTool.ToolType.DAMKA, 2, 1, computer);
        addTool(AnimalTool.ToolType.DAMKA, 2, 3, computer);
        addTool(AnimalTool.ToolType.DAMKA, 2, 5, computer);
        addTool(AnimalTool.ToolType.DAMKA, 2, 7, computer);
        //adding the tools to each player
        addToolsToPlayers();
        GUI.makeVisible();
    }

    private void initBoardForTest() {
        clearBoard();
        //initial player1 tools
        addTool(AnimalTool.ToolType.DAMKA, 1, 0, user);
        addTool(AnimalTool.ToolType.DAMKA, 1, 2, user);
        addTool(AnimalTool.ToolType.DAMKA, 2, 3, user);
        addTool(AnimalTool.ToolType.DAMKA, 7, 0, user);
        addTool(AnimalTool.ToolType.DAMKA, 0, 1, computer);
        addToolsToPlayers();
        GUI.makeVisible();
    }

    /**
     * add tool to the board
     * @param toolType type of the animal tool
     * @param row row to put the tool
     * @param col column to put the tool
     * @param player whom this tool belongs to
     */
    private void addTool(AnimalTool.ToolType toolType, int row, int col, Player player){
        AnimalTool tool = null;
        switch (toolType){
            case DOG -> tool = new Dog(row, col, player, board, GUI);
            case CAT -> tool = new Cat(row, col, player, board, GUI);
            case ELEPHANT -> tool = new Elephant(row, col, player, board, GUI);
            case KANGAROO -> tool = new Kangaroo(row, col, player, board, GUI);
            case DAMKA -> tool = new DamkaTool(row, col, player, board, GUI);
        }
        /*if(toolType == AnimalTool.ToolType.DAMKA)
            ((DamkaTool)tool).makeQueen();*/
        board[row][col].setTool(tool);
        GUI.putTool(tool, row, col);
    }

    /**
     * add each tool to its players list of tools
     */
    private void addToolsToPlayers() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j].getTool() != null) {
                    if (board[i][j].getTool().player == user) {
                        user.addTool(board[i][j].getTool());
                    }
                    if (board[i][j].getTool().player == computer) {
                        computer.addTool(board[i][j].getTool());
                    }
                }
            }
        }
    }

    public void startWithGUI(){
        if(gameType == GameType.CHECKERS) {
            initCheckersBoard();
//            initBoardForTest();
        }
        else {
            initBoard();
        }
        makeRound();
    }

    public void makeRound(){
        gameStatus = user.makeMoveWithGUI();
        if(gameStatus == GameStatus.USER_TURN){
            return;
        }
        updateGameStatus();
        if (!isGameOn()){
            printEndingMessageToGUI();
            return;
        }
        do{
            gameStatus = computer.makeMove();
        }while (gameStatus == GameStatus.COMPUTER_TURN);
        updateGameStatus();
        if(!isGameOn()){
            printEndingMessageToGUI();
        }
    }

    /**
     * @return true if game is on
     */
    private boolean isGameOn() {
        return gameStatus == GameStatus.USER_TURN || gameStatus == GameStatus.COMPUTER_TURN;
    }

    /**
     * check and update if there is a winner
     */
    private void updateGameStatus() {
        if(user.hasNoTools()){
            gameStatus = GameStatus.COMPUTER_WON;
        }
        if(computer.hasNoTools()){
            gameStatus = GameStatus.USER_WON;
        }
    }

    public void printEndingMessageToGUI() {
        switch (gameStatus){
            case USER_WON -> {
                GUI.printUser("Congratulations, User has won :)");
                GUI.addLog("User won");
            }
            case USER_SURRENDERED, COMPUTER_WON -> {
                GUI.printUser("Sorry, Computer has won :(");
                GUI.addLog("CPU won");
            }
            case TIE -> {
                GUI.printUser("Well, unfortunately it’s a Tie...");
                GUI.addLog("It's a Tie");
            }
        }
        GUI.printCpu("Press restart for new game.");
        GUI.addLog("Press restart for new game.");
        GUI.stopGame();
    }

    public static boolean isValidIndexes(int ...indexes){
        return Arrays.stream(indexes).filter(ind -> ind < 0 || ind >= BOARD_SIZE).findAny().isEmpty();
    }
}
