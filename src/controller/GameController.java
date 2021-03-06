package controller;

import model.ChessColor;
import view.Chessboard;
import view.CurrentPlayerLabel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class GameController {
    private Chessboard chessboard;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void loadGameFromFile() {
        for (int i = 0; i < 8; i ++){
            for (int j = 0; j < 8; j ++){
                if (chessboard.getChessComponents()[i][j].isSelected()){
                    JOptionPane.showMessageDialog(null,"还有棋子处于选中状态！","提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("加载游戏");
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("标签文件(*.txt)", "txt");
//        fileChooser.setFileFilter(filter);
        int select = fileChooser.showOpenDialog(fileChooser);
        if (select == JFileChooser.CANCEL_OPTION) return ;
        try {
            //TODO:HERE
            File file = fileChooser.getSelectedFile();
            String name = file.getName();
            String[] strArray = name.split("\\.");
            int suffixIndex = strArray.length -1;
            if(!Objects.equals(strArray[suffixIndex], "txt")){
                JOptionPane.showMessageDialog(null, "文件格式错误！","提示",JOptionPane.ERROR_MESSAGE);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileChooser.getSelectedFile())));
            List<String> chessData = new ArrayList<>(0);
            while (true){
                String content = br.readLine();
                if (content == null) break;
                chessData.add(content);
            }
            //非法检测！
            if (chessData.get(0).length() != 8 || chessData.get(1).length() != 8 || chessData.get(2).length() != 8 ||
                    chessData.get(3).length() != 8 || chessData.get(4).length() != 8 || chessData.get(5).length() != 8 || chessData.get(6).length() != 8 ||
                    chessData.get(7).length() != 8){
                JOptionPane.showMessageDialog(null, "棋盘并非8*8","提示",JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (chessData.size() < 9 || !(chessData.get(8).equals("Black") || chessData.get(8).equals("White"))){
                JOptionPane.showMessageDialog(null, "导入数据只有棋盘，没有下一步行棋的方的提示","提示",JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = 0; i < 8; i ++){
                for (int j = 0; j < 8; j ++){
                    char chess = chessData.get(i).charAt(j);
                    if (chess != 'k' && chess != 'K' && chess != 'Q' && chess != 'q' && chess != 'B' && chess != 'b' &&
                            chess != 'R' && chess != 'r' && chess != 'P' && chess != 'p' && chess != 'N' && chess != 'n' && chess != '_'){
                        JOptionPane.showMessageDialog(null, "棋子并非六种之一，棋子并非黑白棋子","提示",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            chessboard.RemoveChessComponents();
            chessboard.loadGame(chessData);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "please write a correct path and try again");
        }
    }

    public void writeGameToFile(){
        //存储棋盘，样例同as5
        for (int i = 0; i < 8; i ++){
            for (int j = 0; j < 8; j ++){
                if (chessboard.getChessComponents()[i][j].isSelected()){
                    JOptionPane.showMessageDialog(null,"还有棋子处于选中状态！","提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        StringBuilder proDatas = new StringBuilder();
        for (int i = 0; i < 8; i ++){
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < 8; j ++){
                line.append(chessboard.getChessComponents()[i][j].getName());
            }
            proDatas.append(line).append("\n");
        }
        if (chessboard.getCurrentColor() == ChessColor.BLACK){
            proDatas.append("Black").append("\n");
        }
        else {
            proDatas.append("White").append("\n");
        }
        //增加写出步骤过程
        for (int i = 0; i < chessboard.getRecorder().size(); i ++){
            proDatas.append(chessboard.getRecorder().get(i).toString()).append("\n");
        }
        //创建文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存游戏");
        //后缀名过滤器
        FileNameExtensionFilter filter = new FileNameExtensionFilter("标签文件(*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        // 在容器上打开文件选择器
        File file = null;
        String fName = null;
        FileOutputStream fos = null;//字节输出流
        int select = fileChooser.showSaveDialog(fileChooser);
        if (select == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
            fName = file.getName();//从文件名输入框中获取文件名
        }
        else if (select == JFileChooser.CANCEL_OPTION) return;
        if(fName == null || fName.trim().length() == 0){
            JOptionPane.showMessageDialog(null, "文件名为空！","提示",JOptionPane.ERROR_MESSAGE);
        }
        try {
            assert file != null;
            if(file.exists()) {//若选择已有文件----询问是否要覆盖
                ImageIcon icon = new ImageIcon("./images/item000.png");
                int i = JOptionPane.showConfirmDialog(null, "该文件已经存在，确定要覆盖吗？","提示", JOptionPane.YES_NO_OPTION,INFORMATION_MESSAGE,icon);
                if(i == JOptionPane.YES_OPTION) fName = fName.substring(0,fName.length() - 4);
                else return;
            }
            file = new File(fileChooser.getCurrentDirectory()+"/"+fName+".txt");
            fos = new FileOutputStream(file,false);
            //写入文件操作
            String Data = proDatas.toString();
            fos.write(Data.getBytes());
            fos.close();
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(null, "文件保存出错"+e1.getMessage());
        } catch (IOException e1) {
            System.err.println("IO异常");
            e1.printStackTrace();
        }
    }

    public void RestartGame(){
        chessboard.RemoveChessComponents();
        chessboard.newGame();
    }
}
