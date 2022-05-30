//import java.awt.*;
//
//import java.awt.event.*;
//
//import javax.swing.*;
//
//public class Manager{
//
//    JFrame mainFrame; //主框架
//
//    public StudentManager(){
//
//        mainFrame=new JFrame("管理系统");
//
////创建菜单栏JMenuBar
//
//        JMenuBar menuBar=new JMenuBar();
//
////创建菜单JMenu
//
//        JMenu menu=new JMenu("菜单选项");
//
////创建菜单项目JMenuItem
//
//        JMenuItem input=new JMenuItem("录入对象信息");
//
//        input.addActionListener(new MenuHandler());
//
//        JMenuItem modify=new JMenuItem("修改对象信息");
//
//        modify.addActionListener(new MenuHandler());
//
////把菜单项目JMenuItem添加到菜单JMenu当中
//
//        menu.add(input);
//
//        menu.add(modify);
//
////把菜单JMenu添加到菜单栏JMenuBar当中
//
//        menuBar.add(menu);
//
////把菜单栏JMenuBar添加到框架上
//
//        mainFrame.setJMenuBar(menuBar);
//
////创建标签
//
//        JLabel myLabel=new JLabel("管理系统");
//
//        myLabel.setForeground(Color.red);
//
//        myLabel.setSize(50,5);
//
//        JPanel pan=new JPanel();
//
//        pan.add(myLabel);
//
////把标签所在的容器设置为框架的内容面板
//
//        mainFrame.setContentPane(pan);
//
//        mainFrame.setSize(500,500);
//
//        mainFrame.setVisible(true);
//
////关闭框架的处理
//
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//    }
//
//    class MenuHandler implements ActionListener{
//
//        public void actionPerformed(ActionEvent e){
//
//            if(e.getActionCommand().equals("录入对象信息"))
//
//            {
//
//                new Input(); //如果是录入信息，则产生录入窗口
//
//            }
//
//            if(e.getActionCommand().equals("查询对象信息"))
//
//            {
//
//                new Inquest(); //如果是查询信息，则产生查询窗口
//
//            }
//
//        }
//
//    }
//
//    public static void main(String args[]){
//
//        new StudentManager();
//
//    }
//
//}