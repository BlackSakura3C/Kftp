package testFile_useless;

import java.io.*;
import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

public class FileInput2 implements ActionListener{
    JFrame frame=new JFrame();
    Container con=new Container();
    JLabel label1=new JLabel("选择文件目录");
    JLabel label4=new JLabel();
    JLabel label5=new JLabel();

    JTextField text1=new JTextField("");
    JTextArea text_result=new JTextArea();

    JButton button1=new JButton("选择");
    JButton button5=new JButton("扫描");
    JFileChooser jfc=new JFileChooser();

    FileInput2() throws Exception{
        text_result.setVisible(false);
        jfc.setCurrentDirectory(new File("d:\\"));
        double lx=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int)(lx / 2) - 230,(int)(ly / 2) - 120));// 设定窗口出现位置
        frame.setSize(600,240);// 设定窗口大小
        // 下面设定标签等的出现位置和宽
        label1.setBounds(10,30,100,20);
        text1.setBounds(110,30,250,20);
        button1.setBounds(400,30,100,20);
        text1.setEditable(false);

        label4.setBounds(10,70,100,20);
        label5.setBounds(100,70,1000,20);

        button5.setBounds(160,160,100,20);

        text_result.setBounds(10,100,970,300);
        text_result.setAutoscrolls(true);

        button1.addActionListener(this);
        button5.addActionListener(this);

        con.add(label1);
        con.add(text1);
        con.add(button1);

        con.add(label4);
        con.add(label5);

        con.add(button5);
        con.add(jfc);

        con.add(text_result);

        frame.add(con);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    boolean inWork;
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(button1)){
            label4.setText("");
            jfc.setFileSelectionMode(1);
            int state=jfc.showOpenDialog(null);
            if(state == 1){
                return;
            }
            else{
                File f=jfc.getSelectedFile();
                text1.setText(f.getAbsolutePath());
            }
        }

        if(e.getSource().equals(button5)){ // 导入
            final File file=new File(text1.getText());
            if(text1.getText().trim().equals("")){
                label4.setText("请选择文件路径！");
            }
            else{
                if(inWork){
                    return;
                }
                inWork=true;
                new Thread(){
                    public void run(){
                        try{
                            showFiles(file);
                        }
                        catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        finally{
                            label4.setText("文件扫描完成!");
                            label5.setText("");
                            inWork=false;
                        }
                    }
                }.start();
            }
        }

    }

    private void showFiles(final File path_from) throws InterruptedException{
        File[] files=path_from.listFiles();
        File file_temp=null;
        for(int i=0;i < files.length;i++){
            file_temp=files[i];
            if(file_temp.isDirectory()){
                showFiles(file_temp);
            }
            else if(file_temp.isFile()){
                System.out.println(file_temp.toString());
                label4.setText("正在扫描文件：");
                label5.setText(file_temp.toString());
                Thread.sleep(200L);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        new FileInput2();
    }

}