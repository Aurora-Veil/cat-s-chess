package view;


import controller.ChessRecorder;
import model.*;
import controller.ClickController;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.WHITE;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;

    protected CurrentPlayerLabel currentPlayerLabel;

    private List<ChessRecorder> recorder = new ArrayList<>();

    public List<ChessRecorder> getRecorder() {
        return recorder;
    }

    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);
        //bishop
        initBishopOnBoard(0,2,ChessColor.BLACK);
        initBishopOnBoard(0,CHESSBOARD_SIZE - 3,ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);
        //Pawn
        for (int i = 0; i < CHESSBOARD_SIZE; i ++){
            initPawnOnBoard(1,i,ChessColor.BLACK);
            initPawnOnBoard(CHESSBOARD_SIZE - 2,i,ChessColor.WHITE);
        }
        //knight
        initKnightOnBoard(0,1,ChessColor.BLACK);
        initKnightOnBoard(0,CHESSBOARD_SIZE - 2,ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);
        //queen
        initQueenOnBoard(0,3,ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);
        //king
        initKingOnBoard(0,4,ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();
        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        ChessComponent IsKing = chess2;
        boolean passPawn = false;
        if (chess1 instanceof PawnChessComponent && recorder.size() != 0){
            passPawn = ((PawnChessComponent) chess1).passPawn(chessComponents,chess2.getChessboardPoint());
        }
        ChessRecorder step = new ChessRecorder(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getName().charAt(0),
                chess2.getChessboardPoint().getX(),chess2.getChessboardPoint().getY(),chess2.getName().charAt(0),currentColor);
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
        if (IsKing instanceof KingChessComponent){
            String str;
            str = IsKing.getChessColor() == ChessColor.BLACK ? "白方获胜！" : "黑方获胜";
            int select = JOptionPane.showOptionDialog(null, str, "胜负判定", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon("./images/label-icon.png"), new String[]{"再来一局","关闭游戏"}, "再来一局");
            if(select == 0){
                RemoveChessComponents();
                newGame();
                swapColor();
                return;
            } else if(select==1){
                // TODO Auto-generated method stub
                System.exit(0);
            }
            step.setSpecial("end");
        }
        if ((chess1 instanceof PawnChessComponent) && (chess1.getChessboardPoint().getX() == 0 || chess1.getChessboardPoint().getX() == 7)){
            PawnChange(chess1);
            step.setSpecial("change pawn");
        }
        if (chess1 instanceof PawnChessComponent && recorder.size() != 0){
            if (passPawn){
                ChessComponent chessComponent = new EmptySlotComponent(new ChessboardPoint(row2,col1), calculatePoint(row2,col1), clickController, CHESS_SIZE);
                chessComponent.setVisible(true);
                putChessOnBoard(chessComponent);
                chessComponents[row2][col1].repaint();
                step.setSpecial("en passant");
            }
        }
        recorder.add(step);
        chessComponents[row1][col1].repaint();
        chessComponents[row2][col2].repaint();
        MusicPlay bing = new MusicPlay("./src/轻轻按下开关_1.wav");
        bing.musicMain(1);
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                chessComponents[i][j].setPath(false);
                chessComponents[i][j].repaint();
            }
        }
        //此处切换label文字
        currentPlayerLabel.CurrentPlayerColor(currentColor);
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        initiateEmptyChessboard();
        for (int i = 0; i < 8 ; i ++) {
            for (int j = 0; j < 8; j++) {
                char chess = chessData.get(i).charAt(j);
                switch (chess) {
                    case 'K' -> initKingOnBoard(i,j,ChessColor.BLACK);
                    case 'k' -> initKingOnBoard(i,j,ChessColor.WHITE);
                    case 'Q' -> initQueenOnBoard(i,j,ChessColor.BLACK);
                    case 'q' -> initQueenOnBoard(i,j,ChessColor.WHITE);
                    case 'B' -> initBishopOnBoard(i,j,ChessColor.BLACK);
                    case 'b' -> initBishopOnBoard(i,j,ChessColor.WHITE);
                    case 'N' -> initKnightOnBoard(i,j,ChessColor.BLACK);
                    case 'n' -> initKnightOnBoard(i,j,ChessColor.WHITE);
                    case 'R' -> initRookOnBoard(i,j,ChessColor.BLACK);
                    case 'r' -> initRookOnBoard(i,j,ChessColor.WHITE);
                    case 'P' -> initPawnOnBoard(i,j,ChessColor.BLACK);
                    case 'p' -> initPawnOnBoard(i,j,ChessColor.WHITE);
                }
            }
        }
        currentColor = chessData.get(8).equals("Black") ? ChessColor.BLACK : ChessColor.WHITE;
        //此处切换label文字
        currentPlayerLabel.CurrentPlayerColor(currentColor);
        if (chessData.size() == 9){
            JOptionPane.showMessageDialog(null,"没有行棋步骤！","提示",JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 9; i < chessData.size(); i ++){
            String step = chessData.get(i);
            System.out.println(step);
            ChessColor color = step.charAt(12) == 'b' ? ChessColor.BLACK : ChessColor.WHITE;
            recorder.add(new ChessRecorder(step.charAt(1) - 48, step.charAt(3) - 48,step.charAt(5), step.charAt(7)- 48, step.charAt(9)- 48,step.charAt(11),color));
            System.out.println(recorder.get(recorder.size() - 1).getSourceX());
            System.out.println(recorder.get(recorder.size() - 1).getSourceY());
            if (step.length() >= 14){
                recorder.get(recorder.size() - 1).setSpecial(step.substring(13));
                System.out.println(recorder.get(recorder.size() - 1).getSpecial());
            }
        }
    }

    public void RemoveChessComponents(){
        for (int i = 0; i < 8; i ++){
            for (int j = 0; j < 8; j ++){
                if (chessComponents[i][j].isSelected()){
                    JOptionPane.showMessageDialog(null,"还有棋子处于选中状态！","提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        recorder.clear();
        for (int i = 0; i < 8 ; i ++){
            for (int j = 0; j < 8 ; j ++){
                chessComponents[i][j].setVisible(false);
            }
        }
    }

    public void newGame() {
        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);
        //bishop
        initBishopOnBoard(0,2,ChessColor.BLACK);
        initBishopOnBoard(0,CHESSBOARD_SIZE - 3,ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);
        //Pawn
        for (int i = 0; i < CHESSBOARD_SIZE; i ++){
            initPawnOnBoard(1,i,ChessColor.BLACK);
            initPawnOnBoard(CHESSBOARD_SIZE - 2,i,ChessColor.WHITE);
        }
        //knight
        initKnightOnBoard(0,1,ChessColor.BLACK);
        initKnightOnBoard(0,CHESSBOARD_SIZE - 2,ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);
        //queen
        initQueenOnBoard(0,3,ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);
        //king
        initKingOnBoard(0,4,ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);

        currentColor = ChessColor.WHITE;
        //此处切换label文字
        currentPlayerLabel.CurrentPlayerColor(currentColor);
    }

    public void PawnChange(ChessComponent chess1){
        int select = JOptionPane.showOptionDialog(null, "请你选择想变成的棋子", "兵的升变",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/item000.png"), new String[]{"车","马","象","后"}, "车");
        switch (select) {
            case 0 -> {
                remove(chess1);
                add(chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()] = new RookChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), chess1.getChessColor(), clickController, CHESS_SIZE));
                chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()].repaint();
            }
            case 1 -> {
                remove(chess1);
                add(chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()] = new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), chess1.getChessColor(), clickController, CHESS_SIZE));
                chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()].repaint();
            }
            case 2 -> {
                remove(chess1);
                add(chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()] = new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), chess1.getChessColor(), clickController, CHESS_SIZE));
                chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()].repaint();
            }
            case 3 -> {
                remove(chess1);
                add(chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()] = new QueenChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), chess1.getChessColor(), clickController, CHESS_SIZE));
                chessComponents[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()].repaint();
            }
            case JOptionPane.CLOSED_OPTION -> {
                JOptionPane.showMessageDialog(null,"兵必须升变","提示",JOptionPane.ERROR_MESSAGE);
                PawnChange(chess1);
            }
        }
    }

    public void RetractChess(){
        if (recorder.size() == 0){
            JOptionPane.showMessageDialog(null,"没有步骤可以悔棋啦！","提示",JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < 8; i ++){
            for (int j = 0; j < 8; j ++){
                if (chessComponents[i][j].isSelected()){
                    JOptionPane.showMessageDialog(null,"还有棋子处于选中状态！","提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        int souX = recorder.get(recorder.size() - 1).getSourceX();
        int desX = recorder.get(recorder.size() - 1).getDesX();
        int souY = recorder.get(recorder.size() - 1).getSourceY();
        int desY = recorder.get(recorder.size() - 1).getDesY();
        chessComponents[souX][souY].setVisible(false);
        remove(chessComponents[souX][souY]);
        switch (recorder.get(recorder.size() - 1).getSourceName()) {
            case 'K' -> initKingOnBoard(souX,souY,ChessColor.BLACK);
            case 'k' -> initKingOnBoard(souX,souY,ChessColor.WHITE);
            case 'Q' -> initQueenOnBoard(souX,souY,ChessColor.BLACK);
            case 'q' -> initQueenOnBoard(souX,souY,ChessColor.WHITE);
            case 'B' -> initBishopOnBoard(souX,souY,ChessColor.BLACK);
            case 'b' -> initBishopOnBoard(souX,souY,ChessColor.WHITE);
            case 'N' -> initKnightOnBoard(souX,souY,ChessColor.BLACK);
            case 'n' -> initKnightOnBoard(souX,souY,ChessColor.WHITE);
            case 'R' -> initRookOnBoard(souX,souY,ChessColor.BLACK);
            case 'r' -> initRookOnBoard(souX,souY,ChessColor.WHITE);
            case 'P' -> initPawnOnBoard(souX,souY,ChessColor.BLACK);
            case 'p' -> initPawnOnBoard(souX,souY,ChessColor.WHITE);
        }
        chessComponents[desX][desY].setVisible(false);
        remove(chessComponents[desX][desY]);
        switch (recorder.get(recorder.size() - 1).getDesName()) {
            case 'K' -> initKingOnBoard(desX,desY,ChessColor.BLACK);
            case 'k' -> initKingOnBoard(desX,desY,ChessColor.WHITE);
            case 'Q' -> initQueenOnBoard(desX,desY,ChessColor.BLACK);
            case 'q' -> initQueenOnBoard(desX,desY,ChessColor.WHITE);
            case 'B' -> initBishopOnBoard(desX,desY,ChessColor.BLACK);
            case 'b' -> initBishopOnBoard(desX,desY,ChessColor.WHITE);
            case 'N' -> initKnightOnBoard(desX,desY,ChessColor.BLACK);
            case 'n' -> initKnightOnBoard(desX,desY,ChessColor.WHITE);
            case 'R' -> initRookOnBoard(desX,desY,ChessColor.BLACK);
            case 'r' -> initRookOnBoard(desX,desY,ChessColor.WHITE);
            case 'P' -> initPawnOnBoard(desX,desY,ChessColor.BLACK);
            case 'p' -> initPawnOnBoard(desX,desY,ChessColor.WHITE);
            case '_' ->{
                ChessComponent chessComponent = new EmptySlotComponent(new ChessboardPoint(desX,desY), calculatePoint(desX,desY), clickController, CHESS_SIZE);
                chessComponent.setVisible(true);
                putChessOnBoard(chessComponent);
            }
        }
        if (recorder.get(recorder.size() - 1).getSpecial().equals("en passant")){
            if (recorder.get(recorder.size() - 1).getSourceChessColor() == ChessColor.BLACK){
                initPawnOnBoard(souX,desY,ChessColor.WHITE);
            }else if (recorder.get(recorder.size() - 1).getSourceChessColor() == ChessColor.WHITE){
                initPawnOnBoard(souX,desY,ChessColor.BLACK);
            }
        }
        recorder.remove(recorder.size() - 1);
        swapColor();
    }
    public static class MusicPlay {

        private AudioClip aau;
        private String url;


        public MusicPlay(String url){
            this.url=url;
            playMusic();

        }
        public  void playMusic(){
            try {
                URL cb;
                //可以在项目里创建一个Source folder包，将音乐文件放到这个包里,再把路径给它
                File f = new File(url); //url这里放音乐路径。
                cb = f.toURL();
                aau = Applet.newAudioClip(cb);

            } catch (MalformedURLException e) {

                e.printStackTrace();
            }
        }
        //控制音乐的方法,调用这个方法要给一个int参数
        public void musicMain(int n) {
            //用switch循环
            switch (n) {
                //如果是1就开始播放
                case 1 -> aau.play();

                //如果是2,就停止播放
                case 2 -> aau.stop();
                case 3 ->
                    //循环播放
                        aau.loop();
                default -> {
                }
            }
        }

    }
}
