package view;

import model.ChessColor;

import javax.swing.*;
public class CurrentPlayerLabel extends JFrame {
    protected JLabel label;

    public CurrentPlayerLabel(){
        label = new JLabel("当前行棋方：白方", SwingConstants.CENTER);
    }

    public void CurrentPlayerColor(ChessColor chessColor){
        if (chessColor == ChessColor.BLACK){
            label.setText("当前行棋方：黑方");}
        else if (chessColor == ChessColor.WHITE){
            label.setText("当前行棋方：白方");
        }
    }

}
