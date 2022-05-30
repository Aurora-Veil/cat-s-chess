package view;

import controller.GameController;
import model.BishopChessComponent;
import model.ChessComponent;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    JPanel jp1 = new JPanel();
    JPanel jp2 = new JPanel();
    JPanel jp3 = new JPanel();
    JLabel background;
    int count = 1;


    public ChessGameFrame(int width, int height) {
        setTitle("国际象棋"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        setBak(); //调用背景方法
        Container c = getContentPane(); //获取JFrame面板
        JPanel jp = new JPanel(); //创建个JPanel
        jp.setOpaque(false); //把JPanel设置为透明 这样就不会遮住后面的背景 这样你就能在JPanel随意加组件了
        c.add(jp);
        setSize(getWidth(), getHeight());
        setVisible(true);

        addChessboard();
        addLabel();
        addChangeSkinButton();
        addSaveButton();
        addLoadButton();
        addRestartButton();
    }



    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        CurrentPlayerLabel statusLabel = new CurrentPlayerLabel();
        statusLabel.label.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.label.setSize(200, 60);
        statusLabel.label.setFont(new Font("宋体", Font.BOLD, 20));
        gameController.getChessboard().currentPlayerLabel = statusLabel;
        add(statusLabel.label);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addChangeSkinButton() {
        JButton button = new JButton("更换皮肤");
        button.addActionListener((e) -> {int select = JOptionPane.showOptionDialog(this, "请选择皮肤", "皮肤更改", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,   null, new String[]{"经典","清新可爱风","宝可梦"}, "经典");
        if(select==1){
            changeBackgroundToPicture(1);
            Color purple = new Color(192,173,211);
            Color[] PurpleAndWhite = {Color.WHITE,purple};
            ChessComponent.changeChessBoardColor(PurpleAndWhite);
        }else if(select==0){
            changeBackgroundToPicture(0);
            Color[] BlackAndWhite = {Color.WHITE, Color.BLACK};
            ChessComponent.changeChessBoardColor(BlackAndWhite);
        } else if (select == 2) {
            changeBackgroundToPicture(2);
            Color red = new Color(255,179,179);
            Color[] RedAndWhite = {Color.WHITE, red};
            ChessComponent.changeChessBoardColor(RedAndWhite);
        }
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);
    }

    private void addSaveButton() {
        JButton button = new JButton("保存游戏");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click save");
            gameController.writeGameToFile();
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("加载游戏");
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            gameController.loadGameFromFile();
        });
    }

    private void addRestartButton() {
        JButton button = new JButton("重新开始");
        button.setLocation(HEIGTH, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click restart");
            gameController.RestartGame();
        });
    }
    public void setBackground(int index){
        ((JPanel)this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon("images/white.jpg");
        //0是白色底经典皮肤，1是紫色的东西
        if(index == 0){
            img = new ImageIcon("images/white.jpg"); //添加图片
        } else if (index == 1) {
            img = new ImageIcon("images/好康的.jpg"); //添加图片
        }else if(index == 2){
            img = new ImageIcon("images/宝可梦.jpeg");
        }
        background = new  JLabel(img);
        this.getLayeredPane().add(background, Integer.valueOf(Integer.MIN_VALUE + count));
        background.setBounds(0, 0, getWidth(), getHeight());
        count++;
    }

    public void changeBackgroundToPicture(int index){
        if(index == 0){
            setBackground(0); //调用背景方法
            Container c = getContentPane(); //获取JFrame面板
            jp1 = new JPanel(); //创建个JPanel
            jp1.setOpaque(false); //把JPanel设置为透明 这样就不会遮住后面的背景 这样你就能在JPanel随意加组件了
            c.add(jp1);
            setSize(WIDTH, HEIGTH);
            setVisible(true);
            jp2.setVisible(false);
        }else if(index == 1){
            setBackground(1); //调用背景方法
            Container c = getContentPane(); //获取JFrame面板
            jp2 = new JPanel(); //创建个JPanel
            jp2.setOpaque(false); //把JPanel设置为透明 这样就不会遮住后面的背景 这样你就能在JPanel随意加组件了
            c.add(jp2);
            setSize(WIDTH, HEIGTH);
            setVisible(true);
            jp1.setVisible(false);
        }else if(index == 2){
            setBackground(2);
            Container c = getContentPane(); //获取JFrame面板
            jp3 = new JPanel(); //创建个JPanel
            jp3.setOpaque(false); //把JPanel设置为透明 这样就不会遮住后面的背景 这样你就能在JPanel随意加组件了
            c.add(jp3);
            setSize(WIDTH, HEIGTH);
            setVisible(true);
        }
    }
    public void setBak(){
        ((JPanel)this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon("images/white.jpg"); //添加图片
        JLabel background = new  JLabel(img);
        this.getLayeredPane().add(background, Integer.valueOf(Integer.MIN_VALUE));
        background.setBounds(0, 0, getWidth(), getHeight());
    }
}
