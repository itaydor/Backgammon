import javax.swing.*;
import java.awt.*;

public class MyBtn extends JButton {

    int row;
    int col;
    boolean clicked = false;

    public MyBtn(int row, int col){
        super();
        this.row = row;
        this.col = col;

        setText(row + "" + col);
        setFont(new Font("MV Boli", Font.BOLD, 18));
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.TOP);
        setIconTextGap(-5);
        setDefaultColors();
    }

    public void setDefaultColors() {
        if(row % 2 == col % 2){
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
        else {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
        }
    }

    public void click(){
        clicked = !clicked;
        if(clicked)
            setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        else
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public String getPos(){
        return row + "" + col;
    }
}
