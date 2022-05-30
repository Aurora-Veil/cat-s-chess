package model;

import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnightChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image KNIGHT_WHITE;
    private static Image KNIGHT_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image knightImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("./images/白马.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("./images/黑马.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ChessboardPoint> canMoveToList() {
        List<ChessboardPoint> canMoveToList = new ArrayList<>();
        if (this.Move(2,1)){canMoveToList.add(this.getChessboardPoint().offset(2, 1));}
        if (this.Move(2,-1)){canMoveToList.add(this.getChessboardPoint().offset(2, -1));}
        if (this.Move(-2,1)){canMoveToList.add(this.getChessboardPoint().offset(-2, 1));}
        if (this.Move(-2,-1)){canMoveToList.add(this.getChessboardPoint().offset(-2, -1));}
        if (this.Move(1,2)){canMoveToList.add(this.getChessboardPoint().offset(1, 2));}
        if (this.Move(-1,2)){canMoveToList.add(this.getChessboardPoint().offset(-1, 2));}
        if (this.Move(1,-2)){canMoveToList.add(this.getChessboardPoint().offset(1, -2));}
        if (this.Move(-1,-2)){canMoveToList.add(this.getChessboardPoint().offset(-1, -2));}
        return canMoveToList;
    }

    public boolean Move(int dx, int dy){
        if (this.getChessboardPoint().offset(dx, dy) != null){
            return clickController.getChessboard().getChessComponents()[this.getChessboardPoint().getX() + dx][this.getChessboardPoint().getY() + dy].getChessColor() != this.getChessColor();
        }
        return false;
    }

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKnightImage(color);
        name = color == ChessColor.BLACK ? "N" : "n";
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
        if ((Math.abs(source.getX() - destination.getX()) == 2 && Math.abs(source.getY() - destination.getY()) == 1)
            || (Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 2)){
            return true;
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
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
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
            Chessboard.MusicPlay yin= new Chessboard.MusicPlay("./src/灵动的按下按钮音效_1_1.WAV");
            yin.musicMain(1);
        }
    }
}
