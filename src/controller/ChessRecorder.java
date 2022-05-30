package controller;

import model.ChessColor;

public class ChessRecorder {
    private int sourceX;
    private int sourceY;
    private ChessColor sourceChessColor;
    private char sourceName;
    private int desX;
    private int desY;
    private ChessColor desChessColor;
    private char desName;

    private ChessColor currentPlayer;

    private String special;

    public ChessRecorder(int sourceX, int sourceY, char sourceName, int desX, int desY, char desName, ChessColor currentPlayer) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceName = sourceName;
        if ('a' <= sourceName && sourceName <= 'z') {
            sourceChessColor = ChessColor.WHITE;
        } else if ('A' <= sourceName && sourceName <= 'Z') {
            sourceChessColor = ChessColor.BLACK;
        }
        this.desX = desX;
        this.desY = desY;
        this.desName = desName;
        if ('a' <= desName && desName <= 'z') {
            desChessColor = ChessColor.WHITE;
        } else if ('A' <= desName && desName <= 'Z') {
            desChessColor = ChessColor.BLACK;
        }
        this.currentPlayer = currentPlayer;
        special = "";
    }

    public int getSourceX() {
        return sourceX;
    }

    public void setSourceX(int sourceX) {
        this.sourceX = sourceX;
    }

    public int getSourceY() {
        return sourceY;
    }

    public void setSourceY(int sourceY) {
        this.sourceY = sourceY;
    }

    public ChessColor getSourceChessColor() {
        return sourceChessColor;
    }

    public void setSourceChessColor(ChessColor sourceChessColor) {
        this.sourceChessColor = sourceChessColor;
    }

    public char getSourceName() {
        return sourceName;
    }

    public void setSourceName(char sourceName) {
        this.sourceName = sourceName;
    }

    public int getDesX() {
        return desX;
    }

    public void setDesX(int desX) {
        this.desX = desX;
    }

    public int getDesY() {
        return desY;
    }

    public void setDesY(int desY) {
        this.desY = desY;
    }

    public ChessColor getDesChessColor() {
        return desChessColor;
    }

    public void setDesChessColor(ChessColor desChessColor) {
        this.desChessColor = desChessColor;
    }

    public char getDesName() {
        return desName;
    }

    public void setDesName(char desName) {
        this.desName = desName;
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean IsLegal(){
        //颜色是否正确
        if (sourceChessColor != currentPlayer || desChessColor == currentPlayer || sourceChessColor == desChessColor){
            return false;
        }
//        if (sourceName != 'k' && sourceName != 'K' && sourceName != 'Q' && sourceName != 'q' && sourceName != 'B' && sourceName != 'b' &&
//                sourceName != 'R' && sourceName != 'r' && sourceName != 'P' && sourceName != 'p' && sourceName != 'N' && sourceName != 'n' && sourceName != '_'){
//
//        }
        //棋子是否越界
        if (Bounded(sourceX) || Bounded(sourceY) || Bounded(desX) || Bounded(desY)){
            return false;
        }
        //走棋合法？？感觉略麻烦
        return true;
    }

    private boolean Bounded(int coordination){
        return coordination < 0 || coordination > 7;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("(").append(sourceX).append(",").append(sourceY).append(")").append(sourceName);
        result.append("(").append(desX).append(",").append(desY).append(")").append(desName);
        if (currentPlayer == ChessColor.WHITE){
            result.append("w");
        }else if (currentPlayer == ChessColor.BLACK){
            result.append("b");
        }
        result.append(special);
        return result.toString();
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getSpecial() {
        return special;
    }
}
