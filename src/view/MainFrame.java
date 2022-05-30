package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener{
    private final int WIDTH;
    private final int HEIGTH;
    public MainFrame(int width, int height){
        setTitle("国际象棋初始界面"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
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
//        setVisible(true);

        addExitButton();
        addStartButton();
        addLabel();
        setVisible(true);  //可视化
    }
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);  //窗体不可见
        new ChessGameFrame(1000, 760);  //创建新的窗体，以达到切换窗体的效果
    }
//    public static void main(String[] args){
//        new MainFrame(1000,760);
//    }
    private void addStartButton() {
        JButton button = new JButton("START");
        button.setLocation(WIDTH/10+140, HEIGTH / 10 + 400 );
        button.setSize(180,60);
        button.setFont(new Font("Times New Roman", Font.BOLD, 30));
        button.addActionListener(this); //添加监视器
        add(button);
    }
    private void addExitButton() {
        JButton button = new JButton("EXIT");
        button.setLocation(WIDTH/10+460, HEIGTH / 10 + 400);
        button.setSize(180,60);
        button.setFont(new Font("Times New Roman", Font.BOLD, 30));
        add(button);
        button.addActionListener(e -> {
            // TODO Auto-generated method stub
            System.exit(0);
        });
    }
    private void addLabel() {
        JLabel statusLabel = new JLabel("Chess Game",SwingConstants.CENTER);
        statusLabel.setLocation(HEIGTH/10+150, HEIGTH / 10+100);
        statusLabel.setSize(550, 200);
        statusLabel.setFont(new Font("Ink Free", Font.ITALIC, 90));
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel);
    }
    public void setBak(){
        ((JPanel)this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon("images/chessboard.jpeg"); //添加图片
        JLabel background = new  JLabel(img);
        this.getLayeredPane().add(background, Integer.valueOf(Integer.MIN_VALUE));
        background.setBounds(0, 0, WIDTH, HEIGTH);
    }
}