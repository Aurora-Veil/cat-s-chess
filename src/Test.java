
import java.awt.Container;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Test extends JFrame {

        public Test() {
                setBak(); //调用背景方法
                Container c = getContentPane(); //获取JFrame面板
                JPanel jp = new JPanel(); //创建个JPanel
                jp.setOpaque(false); //把JPanel设置为透明 这样就不会遮住后面的背景 这样你就能在JPanel随意加组件了

                c.add(jp);
                setSize(540, 450);
                setVisible(true);
        }

        public void setBak(){
                ((JPanel)this.getContentPane()).setOpaque(false);
                ImageIcon img = new ImageIcon("D:\\桌面\\半成品\\ChessDemo\\src/what.png"); //添加图片
                JLabel background = new  JLabel(img);
                this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
                background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        }

        public static void main(String[] args) {
                Test s = new Test();
                s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

}