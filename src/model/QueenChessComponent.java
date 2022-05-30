package model;

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

public class QueenChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image QUEEN_WHITE;
    private static Image QUEEN_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image queenImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("./images/白后.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("./images/黑后.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ChessboardPoint> canMoveToList() {
        List<ChessboardPoint> canMoveToList = new ArrayList<>();
        canMoveToList.addAll(this.Move(1,1));
        canMoveToList.addAll(this.Move(-1,1));
        canMoveToList.addAll(this.Move(1,-1));
        canMoveToList.addAll(this.Move(-1,-1));
        canMoveToList.addAll(this.Move(1,0));
        canMoveToList.addAll(this.Move(-1,0));
        canMoveToList.addAll(this.Move(0,-1));
        canMoveToList.addAll(this.Move(0,1));
        return canMoveToList;
    }

    public List<ChessboardPoint> Move(int dx,int dy){
        List<ChessboardPoint> canMoveTo = new ArrayList<>();
        int x = this.getChessboardPoint().getX();
        int y = this.getChessboardPoint().getY();
        int changeX = dx;
        int changeY = dy;
        while (this.getChessboardPoint().offset(dx, dy) != null){
            if (clickController.getChessboard().getChessComponents()[x+dx][y+dy].getChessColor() != this.getChessColor()){
                canMoveTo.add(new ChessboardPoint(x+dx,y+dy));
            }
            if (clickController.getChessboard().getChessComponents()[x+dx][y+dy].getChessColor() != ChessColor.NONE){
                break;
            }
            dx += changeX; dy += changeY;
        }
        return canMoveTo;
    }

    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateQueenImage(color);
        name = color == ChessColor.BLACK ? "Q" : "q";
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
        if (Math.abs(source.getY() - destination.getY()) != Math.abs(source.getX() - destination.getX())
                && Math.abs(source.getY() - destination.getY()) != 0 && Math.abs(source.getX() - destination.getX()) != 0){
            return false;
        }
        else if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        }
        else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        }
        else if (Math.abs(source.getY() - destination.getY()) == Math.abs(source.getX() - destination.getX())){
            if ((source.getY() > destination.getY() && source.getX() > destination.getX())||
                    (source.getY() < destination.getY() && source.getX() < destination.getX())){
                int row = Math.min(source.getX(), destination.getX());
                int col = Math.min(source.getY(), destination.getY());
                for (int i = 1; i < Math.abs(source.getY() - destination.getY()); i ++){
                    if (!(chessComponents[row + i][col + i] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            }
            else if (source.getY() > destination.getY() && source.getX() < destination.getX()){
                for (int i = 1; i < Math.abs(source.getY() - destination.getY()); i ++){
                    if (!(chessComponents[source.getX() + i][source.getY() - i] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            }
            else if (source.getY() < destination.getY() && source.getX() > destination.getX()){
                for (int i = 1; i < Math.abs(source.getY() - destination.getY()); i ++){
                    if (!(chessComponents[source.getX() - i][source.getY() + i] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            }
        }
        return true;
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
        g.drawImage(queenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected() && hasMouse) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
            List<ChessboardPoint> canShow = canMoveToList();
            if (canShow.size() > 0) {
                for (ChessboardPoint chessboardPoint : canShow) {
                    clickController.getChessboard().getChessComponents()[chessboardPoint.getX()][chessboardPoint.getY()].path = true;
                    clickController.getChessboard().getChessComponents()[chessboardPoint.getX()][chessboardPoint.getY()].repaint();
                }
            }
            Chessboard.MusicPlay yin= new Chessboard.MusicPlay("./src/灵动的按下按钮音效_1_1.WAV");
            yin.musicMain(1);
            System.out.println("play music");
        }
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
