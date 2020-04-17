import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ftpUI {
    public static JScrollPane leftBottomCenter;
    public static JScrollPane rightBottomCenter;
    private static JTextField hostname_in;
    private static JTextField username_in;
    private static JPasswordField passwd_in;
    private static JTextField port_in;
    private static JTextField clientDirSearch;
    private static JTextField serverDirSearch;
    private static JButton clientDirUpdate;

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame jFrame=new JFrame("FTPClient");

        jFrame.setLayout(new BorderLayout(10,5));
        jFrame.setSize(900,600);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setJMenuBar(new JtopMenu());
        JPanel topPanel=new JPanel();
        jFrame.add(topPanel,BorderLayout.NORTH);
        JPanel midPanel=new JPanel();
        jFrame.add(midPanel,BorderLayout.CENTER);

        JTextArea Status=new JTextArea();
        JScrollPane bottomPanel=new JScrollPane(Status);
        jFrame.add(bottomPanel,BorderLayout.SOUTH);
        placeTopComponents(topPanel);
        placeCenterComponents(midPanel);
        topPanel.setPreferredSize(new Dimension(0,50));
        midPanel.setPreferredSize(new Dimension(0,450));
        bottomPanel.setPreferredSize(new Dimension(0,100));
        bottomPanel.setBackground(Color.GRAY);



        jFrame.setResizable(false);
        jFrame.setVisible(true);


    }

    private static void placeTopComponents(JPanel panel){
        panel.setLayout(null);
        JLabel hostname=new JLabel("主机地址:");
        JLabel username=new JLabel("用户名:");
        JLabel passwd=new JLabel("密码:");
        JLabel port=new JLabel("端口号:");
        hostname_in=new JTextField();
        username_in=new JTextField();
        passwd_in=new JPasswordField();
        port_in=new JTextField("21");
        JButton login=new JButton("登录");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(hostname_in.getText());
                System.out.println(username_in.getText());
                System.out.println(passwd_in.getPassword());
            }
        });

        hostname.setBounds(30,16,80,25);
        hostname_in.setBounds(110,16,120,25);
        username.setBounds(260,16,80,25);
        username_in.setBounds(340,16,120,25);
        passwd.setBounds(490,16,60,25);
        passwd_in.setBounds(550,16,120,25);
        port.setBounds(700,16,50,25);
        port_in.setBounds(740,16,60,25);
        login.setBounds(820,14,60,30);
        panel.add(hostname_in);
        panel.add(username_in);
        panel.add(passwd_in);
        panel.add(hostname);
        panel.add(username);
        panel.add(passwd);
        panel.add(login);
        panel.add(port);
        panel.add(port_in);
    }
    private static void placeCenterComponents(JPanel panel){
        panel.setLayout(new BorderLayout());
        JPanel areaLeft=new JPanel();
        JPanel areaRight=new JPanel();
        areaLeft.setLayout(new BorderLayout(10,5));
        areaRight.setLayout(new BorderLayout(10,5));
        areaLeft.setPreferredSize(new Dimension(440,450));
        areaRight.setPreferredSize(new Dimension(440,450));
//        areaLeft.setBackground(Color.red);
//        areaRight.setBackground(Color.BLUE);
        panel.add(areaLeft,BorderLayout.WEST);
        panel.add(areaRight,BorderLayout.EAST);
        JButton clientDel=new JButton("删除");
        JButton clientRename=new JButton("重命名");
        JButton clientNew=new JButton("新建文件夹");
        JButton clientUpload=new JButton("上传文件");
        JButton clientUpdate=new JButton("刷新");
        JButton serverDel=new JButton("删除");
        JButton serverRename=new JButton("重命名");
        JButton serverNew=new JButton("新建文件夹");
        JButton serverDownload=new JButton("下载文件");
        JButton serverUpdate=new JButton("刷新");
        areaLeft.setLayout(new BorderLayout(10,5));
        areaRight.setLayout(new BorderLayout(10,5));


        JLabel leftLabelTag=new JLabel("客户端");
        JPanel leftTitle=new JPanel();
        JPanel leftTop=new JPanel();

        //JTextArea leftShowDir=new JTextArea();
        JPanel leftBottom=new JPanel();
        leftBottom.setLayout(new BorderLayout());
        JPanel leftBottomNorth=new JPanel();
        JLabel clientDirName=new JLabel("客户机当前文件夹:");
        clientDirSearch=new JTextField(40);
        clientDirUpdate=new JButton("查找");
        //clientDirUpdate.addActionListener(new showDirListener(clientDirSearch.getText()));
        clientDirUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDirContent(clientDirSearch.getText());
            }
            public void showDirContent(String path){
                try{
                    File file=new File(path);
                    System.out.println("Now File Dictionary:");
                    /*指定位置不是目录而是一个文件时*/
                    if(!file.isDirectory()){
                        //System.out.println("文件");
//                        System.out.println("path=" + file.getPath());
//                        System.out.println("absolutepath=" + file.getAbsolutePath());
                        System.out.println("name=" + file.getName()+"  "+fileSize(file)+"  "+file.lastModified());
                    }
                    else if(file.isDirectory()){
                        //System.out.println("文件夹");
                        String[] filelist=file.list();
                        for(String temp:filelist){
                            File fileItem=new File(path+"\\"+temp);
                            if (!fileItem.isDirectory()) {
//                                System.out.println("path=" + fileItem.getPath());
//                                System.out.println("absolutepath="
//                                        + fileItem.getAbsolutePath());
                                Calendar cal = Calendar.getInstance();
                                long time = fileItem.lastModified();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                cal.setTimeInMillis(time);
                                System.out.println("File: " + fileItem.getName()+"  "+fileSize(fileItem)+"  "+formatter.format(cal.getTime()));

                            } else if (fileItem.isDirectory()) {
                                /*递归搜索文件*/
                                //showDirContent(path + "\\" + temp);
                                Calendar cal = Calendar.getInstance();
                                long time = fileItem.lastModified();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                cal.setTimeInMillis(time);
                                System.out.println("Dir: " + fileItem.getName()+"  "+formatter.format(cal.getTime()));
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            public String fileSize(File file){
                if(file.length()<1024) return file.length()+"B";
                else if(file.length()<Math.pow(1024,2)) return file.length()/1024+"KB";
                else if(file.length()<Math.pow(1024,3)) return file.length()/Math.pow(1024,2)+"MB";
                else return file.length()/Math.pow(1024,3)+"GB";
            }
        });




        leftBottomNorth.add(clientDirName);
        leftBottomNorth.add(clientDirSearch);
        leftBottomNorth.add(clientDirUpdate);
        leftBottom.add(leftBottomNorth,BorderLayout.NORTH);
        leftBottomCenter=new JScrollPane();
        leftBottom.add(leftBottomCenter,BorderLayout.CENTER);

        //leftBottom.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JPanel rightTitle=new JPanel();
        JLabel rightLabelTag=new JLabel("服务器");
        JPanel rightTop=new JPanel();
        JPanel rightBottom=new JPanel();

        rightBottom.setLayout(new BorderLayout());
        JPanel rightBottomNorth=new JPanel();
        JLabel serverDirName=new JLabel("服务器当前文件夹:");
        serverDirSearch=new JTextField(":\\",40);
        JButton serverDirUpdate=new JButton("查找");
        rightBottomNorth.add(serverDirName);
        rightBottomNorth.add(serverDirSearch);
        rightBottomNorth.add(serverDirUpdate);
        rightBottom.add(rightBottomNorth,BorderLayout.NORTH);
        rightBottomCenter=new JScrollPane();
        rightBottom.add(rightBottomCenter,BorderLayout.CENTER);



        leftTop.setBackground(Color.LIGHT_GRAY);
        rightTop.setBackground(Color.LIGHT_GRAY);
        leftTop.setLayout(new FlowLayout());
        leftTop.add(clientDel);
        leftTop.add(clientRename);
        leftTop.add(clientNew);
        leftTop.add(clientUpload);
        leftTop.add(clientUpdate);
        rightTop.setLayout(new FlowLayout());
        rightTop.add(serverDel);
        rightTop.add(serverRename);
        rightTop.add(serverNew);
        rightTop.add(serverDownload);
        rightTop.add(serverUpdate);

        leftTitle.setLayout(new FlowLayout());
        leftTitle.add(leftLabelTag);
        rightTitle.setLayout(new FlowLayout());
        rightTitle.add(rightLabelTag);
        areaLeft.add(leftTitle,BorderLayout.NORTH);
        areaLeft.add(leftTop,BorderLayout.CENTER);
        areaLeft.add(leftBottom,BorderLayout.SOUTH);
        leftTitle.setPreferredSize(new Dimension(0,20));
        leftTop.setPreferredSize(new Dimension(0,20));
        leftBottom.setPreferredSize(new Dimension(0,330));
        areaRight.add(rightTitle,BorderLayout.NORTH);
        areaRight.add(rightTop,BorderLayout.CENTER);
        areaRight.add(rightBottom,BorderLayout.SOUTH);
        rightTitle.setPreferredSize(new Dimension(0,20));
        rightTop.setPreferredSize(new Dimension(0,20));
        rightBottom.setPreferredSize(new Dimension(0,330));

        rightBottom.setBackground(Color.gray);
        leftBottom.setBackground(Color.gray);
//        leftTitle.setBackground(Color.gray);
//        rightTitle.setBackground(Color.gray);

        //clientUpload.addActionListener(new showDirListener());
//        JLabel test1=new JLabel("ddddddddddddddddd");
//        JLabel test2=new JLabel("sdfffffffffffffff");
//        leftBottom.add(test1);
//        rightBottom.add(test2);
//        JScrollPane client=new JScrollPane();
//        JScrollPane server=new JScrollPane();
//        panel.add(client,BorderLayout.WEST);
//        panel.add(server,BorderLayout.EAST);

    }

//    private static void placeBottomComponents(JPanel panel){
//
//    }

}

class JtopMenu extends JMenuBar{
    public JtopMenu(){
        add(createFileMenu());
        add(createEditMenu());
        add(createMoreMenu());
        setVisible(true);
    }
    private JMenu createFileMenu(){
        JMenu menu=new JMenu("文件(F)");
        menu.setMnemonic(KeyEvent.VK_F);    //设置快速访问符
        JMenuItem item=new JMenuItem("新建(N)",KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
        menu.add(item);
        item=new JMenuItem("打开(O)",KeyEvent.VK_O);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
        menu.add(item);
        item=new JMenuItem("保存(S)",KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        menu.add(item);
        menu.addSeparator();
        item=new JMenuItem("退出(E)",KeyEvent.VK_E);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
        menu.add(item);
        return menu;
    }
    private JMenu createEditMenu(){
        JMenu menu=new JMenu("编辑(E)");
        menu.setMnemonic(KeyEvent.VK_E);
        JMenuItem item=new JMenuItem("撤销(U)", KeyEvent.VK_U);
        item.setEnabled(false);
        menu.add(item);
        menu.addSeparator();
        item=new JMenuItem("剪贴(T)",KeyEvent.VK_T);
        menu.add(item);
        item=new JMenuItem("复制(C)",KeyEvent.VK_C);
        menu.add(item);
        return menu;
    }
    private JMenu createMoreMenu(){
        JMenu menu=new JMenu("更多(M)");
        menu.setMnemonic(KeyEvent.VK_M);
        return menu;
    }
}

