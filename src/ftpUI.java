import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/*
*
* 由于没有做成多线程的
* 主线程肯定是UI线程
* 为了实现Socket的响应
* 所有的FTP操作都需要加上重新建立Socket的操作[相当于不断连接进行操作处理]
* 后续应该考虑多线程操作
*
*
* */
public class ftpUI {
    private JFrame jFrame;
    public JScrollPane leftBottomCenter;
    public JScrollPane rightBottomCenter;
    private JTextField hostname_in;
    private JTextField username_in;
    private JPasswordField passwd_in;
    private JTextField port_in;
    private JTextField clientDirSearch;
    private JTextField serverDirSearch;
    private JButton clientDirUpdate;
    private JList<String> dirShower;
    private JButton serverDirUpdate;
    private JList<String> serverdirShower;

    private JScrollPane bottomScollPane;
    public static JTextArea commendArea=new JTextArea();
    private JButton progressStopMisson;

    private static ftpClient ftp;
    private String processfilename;//左右两块Jlist中选中的文件名
    private String[] clientDirArray;
    private String[] serverDirArray;


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        ftpUI test=new ftpUI();


    }
    public ftpUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        jFrame=new JFrame("FTPClient");

        jFrame.setLayout(new BorderLayout(10,5));
        jFrame.setSize(900,600);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setJMenuBar(new JtopMenu());
        JPanel topPanel=new JPanel();
        jFrame.add(topPanel,BorderLayout.NORTH);
        JPanel midPanel=new JPanel();
        jFrame.add(midPanel,BorderLayout.CENTER);

        JPanel bottomPanel=new JPanel();
        jFrame.add(bottomPanel,BorderLayout.SOUTH);
        placeTopComponents(topPanel);
        placeCenterComponents(midPanel);
        placeBottomComponents(bottomPanel);
        topPanel.setPreferredSize(new Dimension(0,50));
        midPanel.setPreferredSize(new Dimension(0,450));
        bottomPanel.setPreferredSize(new Dimension(0,100));
        bottomPanel.setBackground(Color.GRAY);



        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }

    private void placeTopComponents(JPanel panel){
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
                String hostname=hostname_in.getText();
                String username=username_in.getText();
                String passwd=new String(passwd_in.getPassword());
                System.out.println(passwd);
                String port=port_in.getText();
                if(hostname!=null&&username!=null&&passwd!=null&&port!=null){
                    ftp=new ftpClient();
                    try {
                        ftp.init(hostname,username,passwd);
                        if(ftp.login()){
                            System.out.println("Success");
                            JOptionPane.showMessageDialog(jFrame,"登陆成功");
                        }
                        else{
                            System.out.println("Fail");
                            JOptionPane.showMessageDialog(jFrame,"登陆失败");
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    /*
                    * Login
                    * 成功事件也没写
                    * 弹个窗说明一下也行
                    * 或者把成功信息扔在下面命令栏也行
                    *

                    *
                    * */
                }
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
    private void placeCenterComponents(JPanel panel){
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
                if(clientDirSearch.getText().equals("")) return;
                showDirContent(clientDirSearch.getText());
            }
            public void showDirContent(String path){
                try{
                    File file=new File(path);
                    System.out.println("Now File Dictionary:");
                    List<String> dirShowContent=new ArrayList<>();
                    /*指定位置不是目录而是一个文件时*/
                    if(!file.isDirectory()){
                        //System.out.println("文件");
//                        System.out.println("path=" + file.getPath());
//                        System.out.println("absolutepath=" + file.getAbsolutePath());
                        /*
                        * 因为当前的List内部只有一个元素 所以大小设置为1
                        * */
                        //dirShowContent=new String[1];
                        Calendar cal = Calendar.getInstance();
                        long time = file.lastModified();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cal.setTimeInMillis(time);
                        dirShowContent.add(String.format("File: %-40s%-10s%s",file.getName(),fileSize(file),formatter.format(cal.getTime())));
                        //System.out.println("File: " + file.getName()+"  "+fileSize(file)+"  "+file.lastModified());
                    }
                    else if(file.isDirectory()){
                        //System.out.println("文件夹");
                        String[] filelist=file.list();
                        int itemNum=filelist.length;
                        int temp_num=0;
                        //dirShowContent=new String[itemNum];
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

                                dirShowContent.add(String.format("File: %-30s%-10s%s",fileItem.getName(),fileSize(fileItem),formatter.format(cal.getTime())));
                                //temp_num=temp_num+1;
                                //System.out.println("File: " + fileItem.getName()+"\t"+fileSize(fileItem)+"\t"+formatter.format(cal.getTime()));

                            } else if (fileItem.isDirectory()) {
                                /*递归搜索文件*/
                                //showDirContent(path + "\\" + temp);
                                Calendar cal = Calendar.getInstance();
                                long time = fileItem.lastModified();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                cal.setTimeInMillis(time);

                                dirShowContent.add(String.format("Dir:  %-30s%-10s%s",fileItem.getName(),fileSize(fileItem),formatter.format(cal.getTime())));
                                //temp_num=temp_num+1;
                                //System.out.println("Dir:  " + fileItem.getName()+"  "+formatter.format(cal.getTime()));
                            }
                        }
                    }

                    /*
                    * JList只能使用String[]初始化
                    * 一开始我们又不知道文件列表长度
                    * 只能先存入ArrayList之后导入定长数组做初始化
                    * */
                    int temp_num=0;
                    clientDirArray=new String[dirShowContent.size()];
                    for(String i:dirShowContent){
                        clientDirArray[temp_num]=i;
                        temp_num+=1;
                    }
                    dirShower=new JList<String>(clientDirArray);
                    dirShower.setFont(new Font(Font.MONOSPACED,Font.PLAIN,11));

                    dirShower.addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            String[] temp=clientDirArray[dirShower.getLeadSelectionIndex()].split("\\s+");
                            processfilename=temp[1];
                            System.out.println(processfilename);
                        }
                    });

                    leftBottomCenter.getViewport().add(dirShower);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            public String fileSize(File file){
                if(file.length()<1024) return String.format("%dB",file.length());
                else if(file.length()<Math.pow(1024,2)) return String.format("%dKB",file.length()/1024);
                else if(file.length()<Math.pow(1024,3)) return String.format("%.2fMB",file.length()/Math.pow(1024,2));
                else return String.format("%.2fGB",file.length()/Math.pow(1024,3));
            }
//            public String getType(Object o) { //获取变量类型方法
//                return o.getClass().toString(); //利用Java中的反射机制
//            }
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

        serverDirUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showDirContent(ftp.listFile(ftp.writer,ftp.fisrtSock_input));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                /*
                *
                * 此处展示服务器的文件序列
                * 由于服务器文件可能有访问限制不可能输入什么文件路径
                * 就展示什么路径
                *
                * 参数接受的是List<String>
                * 也就是向服务器发送list命令后传回的结果稍加处理即可
                *
                *
                *
                *
                *
                *
                *
                *
                *
                *
                *
                *
                *
                * */
            }
            private void showDirContent(String[] s){
                serverDirArray=s;
//                String[] serverdir=new String[s.size()];
//                int num=0;
//                for(String tmp:s){
//                    serverdir[num]=tmp;
//                    num+=1;
//                }
//                serverdirShower=new JList<String>(serverdir);
                serverdirShower=new JList<String>(serverDirArray);
                serverdirShower.setFont(new Font(Font.MONOSPACED,Font.PLAIN,11));
                serverdirShower.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        String[] temp=serverDirArray[serverdirShower.getLeadSelectionIndex()].split("\\s+");
                        processfilename=temp[8];
                    }
                });
                rightBottomCenter.getViewport().add(serverdirShower);
            }
        });

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


        clientUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ftp.upload(clientDirSearch.getText(),processfilename,ftp.writer,ftp.fisrtSock_input);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        serverDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ftp.downLoad(clientDirSearch.getText(),processfilename,ftp.writer,ftp.fisrtSock_input);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
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

    private void placeBottomComponents(JPanel panel){
        panel.setLayout(new BorderLayout());
        JPanel bottomTop=new JPanel();
        JProgressBar progressBar=new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        bottomTop.add(progressBar);
        progressStopMisson=new JButton("暂停");
        bottomTop.add(progressStopMisson);
        panel.add(bottomTop,BorderLayout.NORTH);

        bottomScollPane=new JScrollPane(commendArea);
        panel.add(bottomScollPane,BorderLayout.CENTER);
    }

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

