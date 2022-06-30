import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class MyGui implements ActionListener {

    private JFrame frame = new JFrame();
    private JPanel logsPanel = new JPanel();
    private MyBtn[][] myBtns = new MyBtn[8][8];
    private int numOfClicks = 0;
    private MyBtn firstClick;
    private MyBtn secondClick;
    private JButton restartGameBtn = new JButton("Restart");
    private JButton playMusicBtn = new JButton();
    private JButton pauseMusicBtn = new JButton();
    private JButton replayMusicBtn = new JButton();
    private JButton stopMusicBtn = new JButton();
    private JButton showLogsBtn = new JButton("Show Logs");
    private JLabel messageToUser = new JLabel();
    private JLabel cpuLestMove = new JLabel();
    private JTextArea logsArea = new JTextArea();
    private ImageIcon bearIcon = new ImageIcon(this.getClass().getResource("main/resources/Bear.png"));
    private ImageIcon bullIcon = new ImageIcon(this.getClass().getResource("main/resources/Bull.png"));
    private ImageIcon falconIcon = new ImageIcon(this.getClass().getResource("main/resources/Falcon.png"));
    private ImageIcon leopardIcon = new ImageIcon(this.getClass().getResource("main/resources/Leopard.png"));
    private ImageIcon redIcon;
    private ImageIcon blackIcon;
    private ImageIcon crownIcon;
    private Clip clip = null;
    private Game game;
    private Game.GameType gameType;

    public MyGui() {
        try {
//            File file = new File(this.getClass().getResource("main/resources/safari.wav").toURI());
            InputStream resourceAsStream = this.getClass().getResourceAsStream("main/resources/safari.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(resourceAsStream));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ignored) {
        }
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                myBtns[i][j] = new MyBtn(i + 1, j + 1);
                myBtns[i][j].setFocusable(false);
                myBtns[i][j].addActionListener(this);
                mainPanel.add(myBtns[i][j]);
            }
        }

        restartGameBtn.setFocusable(false);
        restartGameBtn.addActionListener(this);

        showLogsBtn.setFocusable(false);
        showLogsBtn.addActionListener(this);

        messageToUser.setForeground(Color.GREEN);
        messageToUser.setOpaque(true);
        messageToUser.setPreferredSize(new Dimension(320, 25));
        messageToUser.setFont(new Font("MV Boli", Font.BOLD, 18));

        cpuLestMove.setForeground(Color.RED);
        cpuLestMove.setOpaque(true);
        cpuLestMove.setPreferredSize(new Dimension(270, 25));
        cpuLestMove.setFont(new Font("MV Boli", Font.BOLD, 18));

        JPanel messagesPanel = new JPanel();
        messagesPanel.setPreferredSize(new Dimension(700, 50));
        messagesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        messagesPanel.add(messageToUser);
        messagesPanel.add(cpuLestMove);
        messagesPanel.add(restartGameBtn);

        redIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("main/resources/red.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        blackIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("main/resources/black.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        crownIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("main/resources/crown.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

        Image playImg = new ImageIcon(this.getClass().getResource("main/resources/play.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        playMusicBtn.setFocusable(false);
        playMusicBtn.setIcon(new ImageIcon(playImg));
        playMusicBtn.addActionListener(this);

        Image pauseImg = new ImageIcon(this.getClass().getResource("main/resources/pause.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        pauseMusicBtn.setFocusable(false);
        pauseMusicBtn.setIcon(new ImageIcon(pauseImg));
        pauseMusicBtn.addActionListener(this);

        Image replayImg = new ImageIcon(this.getClass().getResource("main/resources/replay.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        replayMusicBtn.setFocusable(false);
        replayMusicBtn.setIcon(new ImageIcon(replayImg));
        replayMusicBtn.addActionListener(this);

        Image stopImg = new ImageIcon(this.getClass().getResource("main/resources/stop.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        stopMusicBtn.setFocusable(false);
        stopMusicBtn.setIcon(new ImageIcon(stopImg));
        stopMusicBtn.addActionListener(this);

        JPanel audioPanel = new JPanel();
        audioPanel.setPreferredSize(new Dimension(700, 50));
        audioPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        audioPanel.add(playMusicBtn);
        audioPanel.add(pauseMusicBtn);
        audioPanel.add(stopMusicBtn);
        audioPanel.add(replayMusicBtn);
        audioPanel.add(showLogsBtn);

        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(700, 100));
        footer.setLayout(new BorderLayout());
        footer.add(messagesPanel, BorderLayout.NORTH);
        footer.add(audioPanel, BorderLayout.SOUTH);

        logsArea.setText("Moves:");
        logsArea.setColumns(16);
        logsArea.setLayout(new SpringLayout());
        logsArea.setRows(33);

        JScrollPane sp = new JScrollPane(logsArea);
        sp.setBounds(2, 0, 190, 561);
        sp.setHorizontalScrollBar(new JScrollBar());
        sp.setVerticalScrollBar(new JScrollBar());
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().
                addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));

        logsPanel.setVisible(false);
        logsPanel.setLayout(null);
        logsPanel.setPreferredSize(new Dimension(200, 500));
        logsPanel.add(sp);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Animals Damka");
        ImageIcon image = new ImageIcon(this.getClass().getResource("main/resources/checkers.png"));
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);
        frame.add(logsPanel, BorderLayout.EAST);

        selectGameType();

        game = new Game(this, gameType);
    }

    private void selectGameType() {
        Object[] options = {"Regular Checkers", "Animals Checkers"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Which game do you wish to play?",
                "Chose game",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        switch (choice){
            case 0 -> gameType = Game.GameType.CHECKERS;
            case 1 -> gameType = Game.GameType.ANIMALS_CHECKERS;
        }
    }

    public void start() {
        game.startWithGUI();
    }

    public String getFirstClick() {
        if (firstClick == null)
            return "";
        return firstClick.getPos();
    }

    public String getSecondClick() {
        if (secondClick == null)
            return "";
        return secondClick.getPos();
    }

    public void putTool(AnimalTool animal, int row, int col) {
        drawAnimalInLocation(animal, row, col);
        setTextColorInLocation(animal, row, col);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void setTextColorInLocation(AnimalTool animal, int row, int col) {
        if(gameType == Game.GameType.CHECKERS && !((DamkaTool)animal).isQueen)
            return;
        if (animal.player.getType() == Player.PlayerType.USER)
            myBtns[row][col].setForeground(Color.GREEN);
        else
            myBtns[row][col].setForeground(Color.RED);
    }

    private void drawAnimalInLocation(AnimalTool animal, int row, int col) {
        if (animal instanceof Dog)
            myBtns[row][col].setIcon(bearIcon);
        else if (animal instanceof Cat)
            myBtns[row][col].setIcon(leopardIcon);
        else if (animal instanceof Kangaroo)
            myBtns[row][col].setIcon(falconIcon);
        else if (animal instanceof DamkaTool) {
            if (((DamkaTool) animal).isQueen)
                myBtns[row][col].setIcon(crownIcon);
            else if(animal.player.getType() == Player.PlayerType.USER)
                myBtns[row][col].setIcon(blackIcon);
            else
                myBtns[row][col].setIcon(redIcon);
        } else if (animal instanceof Elephant)
            myBtns[row][col].setIcon(bullIcon);
    }

    public void removeTool(int row, int col) {
        myBtns[row][col].setIcon(null);
        myBtns[row][col].setDefaultColors();
    }

    public void printUser(String str) {
        messageToUser.setText(str);
    }

    public void printCpu(String str) {
        cpuLestMove.setText(str);
    }

    public void addLog(String str) {
        logsArea.setText(logsArea.getText() + "\n" + str);
    }

    public void makeVisible() {
        frame.setVisible(true);
    }

    public void stopGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                myBtns[i][j].setEnabled(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == restartGameBtn)
            restartGame();
        else if (source == playMusicBtn || source == pauseMusicBtn || source == stopMusicBtn || source == replayMusicBtn)
            handleAudioPlayer(source);
        else if (source == showLogsBtn)
            showHideLogsPanel();
        else {
            handleClicks(source);
        }
    }

    private void showHideLogsPanel() {
        boolean visible = logsPanel.isVisible();
        if (visible) {
            logsPanel.setVisible(false);
            showLogsBtn.setText("Show Logs");
        } else {
            logsPanel.setVisible(true);
            showLogsBtn.setText("Hide Logs");
        }
    }

    private void handleAudioPlayer(Object source) {
        if (source == playMusicBtn)
            clip.start();
        else if (source == pauseMusicBtn)
            clip.stop();
        else if (source == replayMusicBtn) {
            clip.setMicrosecondPosition(0);
            clip.start();
        } else {
            clip.stop();
            clip.setMicrosecondPosition(0);
        }

    }

    private void handleClicks(Object source) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (myBtns[i][j].equals(source)) {
                    myBtns[i][j].click();
                    numOfClicks++;
                    if (numOfClicks == 1)
                        firstClick = myBtns[i][j];
                    else
                        secondClick = myBtns[i][j];
                    break;
                }
            }
        }
        if (numOfClicks == 2) {
            game.makeRound();
            firstClick.click();
            firstClick = null;
            secondClick.click();
            secondClick = null;
            numOfClicks = 0;
        }
    }

    private void restartGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                removeTool(i, j);
                myBtns[i][j].setEnabled(true);
            }
        }
        firstClick = null;
        secondClick = null;
        cpuLestMove.setText("");
        logsArea.setText("Moves:");
        selectGameType();
        game = new Game(this, gameType);
        game.startWithGUI();
    }


    public static void main(String[] args) {
        MyGui myGui = new MyGui();
        myGui.start();
    }
}
