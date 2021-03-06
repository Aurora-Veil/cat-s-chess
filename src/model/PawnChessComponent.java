package model;

import controller.ChessRecorder;
import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PawnChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image pawnImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/白兵.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/黑兵.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ChessboardPoint> canMoveToList() {
        List<ChessboardPoint> canMoveToList = new ArrayList<>();
        ChessComponent[][] chessboard = clickController.getChessboard().getChessComponents();
        int x = this.getChessboardPoint().getX();
        int y = this.getChessboardPoint().getY();
        switch (this.getChessColor()) {
            case WHITE -> {
                if (x == 6) {
                    if (chessboard[x-1][y].getChessColor() == ChessColor.NONE && chessboard[x-2][y].getChessColor() == ChessColor.NONE) {
                        canMoveToList.add(this.getChessboardPoint().offset(-2, 0));
                    }
                }
                if (this.getChessboardPoint().offset(-1, 0) != null) {
                    if (chessboard[x-1][y].getChessColor() == ChessColor.NONE) {
                        canMoveToList.add(this.getChessboardPoint().offset(-1, 0));
                    }
                }
                if (this.getChessboardPoint().offset(-1, -1) != null){
                    if (chessboard[x-1][y-1].getChessColor() == ChessColor.BLACK) {
                        canMoveToList.add(this.getChessboardPoint().offset(-1, -1));
                    }
                }
                if (this.getChessboardPoint().offset(-1, 1) != null){
                    if (chessboard[x-1][y+1].getChessColor() == ChessColor.BLACK) {
                        canMoveToList.add(this.getChessboardPoint().offset(-1, 1));
                    }
                }
                if (x == 3 && clickController.getChessboard().getRecorder().size() != 0){
                    ChessRecorder lastStep = clickController.getChessboard().getRecorder().get(clickController.getChessboard().getRecorder().size() - 1);
                    if (y >= 1 && chessboard[x-1][y-1].getChessColor() == ChessColor.NONE && lastStep.getSourceName() == 'P'
                            && lastStep.getSourceX() - lastStep.getDesX() == -2 && lastStep.getSourceY() == y-1 ){
                        canMoveToList.add(this.getChessboardPoint().offset(-1, -1));
                    }
                    if (y <= 6 && chessboard[x-1][y+1].getChessColor() == ChessColor.NONE && lastStep.getSourceName() == 'P'
                            && lastStep.getSourceX() - lastStep.getDesX() == -2 && lastStep.getSourceY() == y+1 ){
                        canMoveToList.add(this.getChessboardPoint().offset(-1, 1));
                    }
                }
            }
            case BLACK -> {
                if (x == 1) {
                    if (chessboard[x+1][y].getChessColor() == ChessColor.NONE && chessboard[x+2][y].getChessColor() == ChessColor.NONE) {
                        canMoveToList.add(this.getChessboardPoint().offset(2, 0));
                    }
                }
                if (this.getChessboardPoint().offset(1, 0) != null) {
                    if (chessboard[x+1][y].getChessColor() == ChessColor.NONE) {
                        canMoveToList.add(this.getChessboardPoint().offset(1, 0));
                    }
                }
                if (this.getChessboardPoint().offset(1, -1) != null){
                    if (chessboard[x+1][y-1].getChessColor() == ChessColor.WHITE) {
                        canMoveToList.add(this.getChessboardPoint().offset(1, -1));
                    }
                }
                if (this.getChessboardPoint().offset(1, 1) != null){
                    if (chessboard[x+1][y+1].getChessColor() == ChessColor.WHITE) {
                        canMoveToList.add(this.getChessboardPoint().offset(1, 1));
                    }
                }
                if (x == 4 && clickController.getChessboard().getRecorder().size() != 0){
                    ChessRecorder lastStep = clickController.getChessboard().getRecorder().get(clickController.getChessboard().getRecorder().size() - 1);
                    if (y >= 1 && chessboard[x+1][y-1].getChessColor() == ChessColor.NONE && lastStep.getSourceName() == 'p'
                            && lastStep.getSourceX() - lastStep.getDesX() == 2 && lastStep.getSourceY() == y-1 ){
                        canMoveToList.add(this.getChessboardPoint().offset(1, -1));
                    }
                    if (y <= 6 && chessboard[x+1][y+1].getChessColor() == ChessColor.NONE && lastStep.getSourceName() == 'p'
                            && lastStep.getSourceX() - lastStep.getDesX() == 2 && lastStep.getSourceY() == y+1 ){
                        canMoveToList.add(this.getChessboardPoint().offset(1, 1));
                    }
                }
            }
        }
        return canMoveToList;
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiatePawnImage(color);
        name = color == ChessColor.BLACK ? "P" : "p";
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        //先写行走，直走
        if (source.getY() == destination.getY()){
            if (chessColor == ChessColor.BLACK && source.getX() == 1 && destination.getX() - source.getX() == 2){
                for (int col = source.getX() + 1; col <= destination.getX(); col++) {
                    if (!(chessComponents[col][source.getY()] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
                return true;
            }
            else if (chessColor == ChessColor.BLACK){
                if (destination.getX() - source.getX() == 1 && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent){
                    return true;
                }
            }
            if (chessColor == ChessColor.WHITE && source.getX() == 6 && source.getX() - destination.getX() == 2){
                for (int col = source.getX() - 1; col >= destination.getX(); col--) {
                    if (!(chessComponents[col][source.getY()] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
                return true;
            }
            else if (chessColor == ChessColor.WHITE){
                return destination.getX() - source.getX() == -1 && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
            }
        }
        //再写吃子
        else if (Math.abs(source.getY() - destination.getY()) == 1){
            if (chessColor == ChessColor.BLACK && destination.getX() - source.getX() == 1
                    && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)){
                return true;
            }
            else if (chessColor == ChessColor.WHITE && destination.getX() - source.getX() == -1
                    && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)){
                return true;
            }
            //过路兵
            else if (clickController.getChessboard().getRecorder().size() != 0) {
                return passPawn(chessComponents,destination);
            }
        }
        return false;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
            List<ChessboardPoint> canShow = canMoveToList();
            if (canShow.size() > 0) {
                for (ChessboardPoint chessboardPoint : canShow) {
                    clickController.getChessboard().getChessComponents()[chessboardPoint.getX()][chessboardPoint.getY()].path = true;
                    clickController.getChessboard().getChessComponents()[chessboardPoint.getX()][chessboardPoint.getY()].repaint();
                }
            }
            Chessboard.MusicPlay yin = new Chessboard.MusicPlay("./src/灵动的按下按钮音效_1_1.WAV");
            if (hasMouse){
                yin.musicMain(1);
                System.out.println("play music");
            }
        }
    }

    public boolean passPawn(ChessComponent[][] chessComponents, ChessboardPoint destination){
        ChessboardPoint source = getChessboardPoint();
        ChessRecorder lastStep = clickController.getChessboard().getRecorder().get(clickController.getChessboard().getRecorder().size() - 1);
        if (chessColor == ChessColor.BLACK && destination.getX() - source.getX() == 1 && source.getX() == 4
                && (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)
                && lastStep.getSourceName() == 'p' && lastStep.getSourceX() - lastStep.getDesX() == 2 && lastStep.getSourceY() == destination.getY()) {
            return true;
        } else if (chessColor == ChessColor.WHITE && destination.getX() - source.getX() == -1 && source.getX() == 3
                && (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)
                && lastStep.getSourceName() == 'P' && lastStep.getSourceX() - lastStep.getDesX() == -2 && lastStep.getSourceY() == destination.getY()) {
            return true;
        }
        return false;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
