//import com.hukaidi.util.NumUtil;
//
//import java.io.*;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Scanner;
//import java.util.regex.Pattern;
//
////55 53 45 52 20 61 64 6d 69 6e 0d 0a USER admin
////50 41 53 53 20 61 64 6d 69 6e 0d 0a PASS admin
//public class FTP {
//    private Socket socket;
//    //目前假设所有被动模式返回的ip都是一样的,仅仅端口不同
//    private int passivePort;
//    private String ftpHost;
//    private int ftpPort;
//    private  String nowPath="/";
//    private Runnable callback;
//    public static void main1(String[] args) throws Exception {
//        Socket socket = new Socket("111.230.233.136", 21);
//        Scanner scanner = new Scanner(System.in);
//        var in = socket.getInputStream();
//        var out = socket.getOutputStream();
//        while (true) {
//            if (in.available() > 0) {
//                //in.transferTo(System.out);
//                var bytes = new byte[in.available()];
//                in.read(bytes);
//                System.out.println(new String(bytes));
//            }
//            if (System.in.available() > 0) {
//                var s = scanner.nextLine().trim();
//                var c = s.indexOf(0);
//                if (c >= '0' && c <= '9' || c >= 'a' && c <= 'f') {
//                    var ss = s.split(" ");
//                    var bytes = new byte[ss.length];
//                    for (int i = 0; i < ss.length; i++) {
//                        bytes[i] = (byte) Integer.parseInt(ss[i], 16);
//                    }
//                    System.out.println("get:" + new String(bytes));
//                    out.write(bytes);
//                } else {
//                    out.write(s.getBytes());
//                    out.write(new byte[]{0x0d, 0x0a});
//                    System.out.println("get:" + s);
//                }
//                out.flush();
//            }
//        }
//    }
//
//    public static void main2(String[] args) throws Exception {
//        var f = new FTP();
//        f.init("111.230.233.136",21);
//        f.sendCmd("USER", 331, "admin");
//        f.sendCmd("PASS", 230, "admin");
//
//        Thread.sleep(1000);
//        f.sendCmd("PWD", 257, "");
//        f.sendCmd("CWD", 250, "/");
//        f.sendCmd("PWD", 257, "");
//        Thread.sleep(1000);
//        f.sendCmd("TYPE", 200, "A");
//        f.sendCmd("PASV", 227, "");
//        f.sendCmd("LIST", 150, "");
//        Thread.sleep(1000);
//        f.sendCmd("XMKD", 257, "ks");
//        //Thread.sleep(10*1000);
//        f.sendCmd("XMKD", 257, "a");
//
//        f.sendCmd("CWD", 250, "/c");
//        f.sendCmd("PWD", 257, "");
//        Thread.sleep(1000);
//        f.sendCmd("TYPE", 200, "A");
//        f.sendCmd("PASV", 227, "");
//        f.sendCmd("LIST", 150, "");
//        Thread.sleep(1000);
//        f.sendCmd("TYPE", 200, "I");
//        f.sendCmd("PASV", 227, "");
//        f.sendCmd("STOR", 150, "coin2.mp4");
//        Thread.sleep(1000);
//
//    }
//
//    public static void main(String[] args) throws Exception{
//        new FTP().main();
//    }
//    public void main(){
//        Scanner scanner=new Scanner(System.in);
//        //System.out.println("请输入将要连接的主机IP:");
//        //String hostname=scanner.nextLine();
//        init("111.230.233.136",21);
//        System.out.println("LocalAddr" + socket.getLocalSocketAddress());
//        System.out.println("RemoteAddr"+ socket.getRemoteSocketAddress());
//        boolean isLogin=false;
//        while(!isLogin){
//            System.out.println("用户名:");
//            String user=scanner.nextLine();
//            System.out.println("密码:");
//            String passwd=scanner.nextLine();
//            isLogin = login(user,passwd);
//        }
//        for (;;) {
//            System.out.print("ftp>"); // 打印提示
//            String s = scanner.nextLine(); // 读取一行输入
//            if (s.equals("quit")) {
//                break;
//            }
//            String[] ss = s.split(" ");
//            switch (ss[0]){
//                case "ls":
//                    System.out.println(nowPath);
//                    list(nowPath);
//                    break;
//                case "mkdir":
//                    System.out.println(nowPath);
//                    mkdir(nowPath,ss[1]);
//                    break;
//                case "cd":
//                    if(ss[1].startsWith("/")){
//                        nowPath=ss[1];
//                        break;
//                    }
//                    else if(!ss[1].endsWith("/"))ss[1]+='/';
//                    nowPath+=ss[1];
//                    break;
//                case "download":
//                    readDirectoryFile(".");
//                    System.out.println("本地文件路径:");
//                    String localp=scanner.nextLine();
//                    System.out.println("服务端文件名:");
//                    String remoteName=scanner.nextLine();
//                    download(localp,remoteName);
//                    break;
//                case "upload":
//                    readDirectoryFile(".");
//                    System.out.println("本地文件路径:");
//                    String localp1=scanner.nextLine();
//                    System.out.println("服务端文件名:");
//                    String remoteName1=scanner.nextLine();
//                    upload(localp1,remoteName1);
//                    break;
//            }
//        }
//        System.out.print("Disconnect");
//    }
//    public boolean login(String user,String password){
//        return sendCmd("USER", 331, user) && sendCmd("PASS", 230, password);
//    }
//
//    public boolean list(String path){
//        sendCmd("CWD", 250, path);
//        sendCmd("PWD", 257, "");
//        sendCmd("TYPE", 200, "A");
//        sendCmd("PASV", 227, "");
//        int _port = passivePort;
//        callback=() -> {
//            try {
//
//                System.out.println("port:" + _port);
//                Socket _socket = new Socket(ftpHost,_port);
//                byte[] bytes = readBytesFromStream(_socket.getInputStream());
//                var s2 = new String(bytes);
//                System.out.println(s2.trim());
//                _socket.close();
//                var storeCode = read(socket.getInputStream());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//        sendCmd("LIST", 150, "");
//        return true;
//    }
//
//    public boolean mkdir(String path,String name){
//        sendCmd("CWD", 250, path);
//        sendCmd("PWD", 257, "");
//        sendCmd("XMKD", 257, name);
//        return true;
//    }
//
//    public boolean upload(String localPath,String fileName){
//        sendCmd("PWD", 257, "");
//        sendCmd("TYPE", 200, "I");
//        sendCmd("PASV", 227, "");
//        int _port = passivePort;
//        callback= () -> {
//            try {
//                System.out.println("port:" + _port);
//                Socket _socket = new Socket(ftpHost,_port);
//                var file = new File(localPath);
//                System.out.println(localPath+":"+file.length());
//                var fin = new BufferedInputStream(new FileInputStream(file));
//                var fileBytes = new byte[1400];
//                var fcount= 1;
//                long total = file.length();
//                long count =0;
//                int con = 0;
//                while(fcount>0){
//                    fcount= fin.read(fileBytes);
//                    if(++con ==300){
//                        System.out.println("upload"+count+"/"+total+":"+fcount);
//                        con=0;
//                    }
//                    if(fcount>0){
//                        _socket.getOutputStream().write(fileBytes,0,fcount);
//                        count+=fcount;
//                    }
//                }
//                System.out.println("upload"+count+"/"+total+":"+fcount);
//                System.out.println("end--upload");
//                fin.close();
//                _socket.close();
//                var storeCode = read(socket.getInputStream());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//        sendCmd("STOR", 150, fileName);
//        return true;
//    }
//
//    public boolean download(String localPath,String fileName){
//        sendCmd("PWD", 257, "");
//        sendCmd("TYPE", 200, "I");
//        sendCmd("PASV", 227, "");
//        int _port = passivePort;
//        callback=() -> {
//            try {
//                System.out.println("port:" + _port);
//                Socket _socket = new Socket(ftpHost,_port);
//                var fout = new BufferedOutputStream(new FileOutputStream(localPath));
//                var in = _socket.getInputStream();
//                var fileBytes = new byte[1400];
//                var fcount=1;
//                long total = in.available();
//                long count =0;
//                int con = 0;
//                while(fcount>0){
//                    fcount= in.read(fileBytes);
//                    if(++con ==300){
//                        System.out.println("download"+count+"/"+total+":"+fcount);
//                        con=0;
//                    }
//                    if(fcount>0){
//                        fout.write(fileBytes,0,fcount);
//                        count+=fcount;
//                    }
//                }
//                System.out.println("download"+count+"/"+total+":"+fcount);
//                System.out.println("end--download");
//                fout.close();
//                _socket.close();
//                var storeCode = read(socket.getInputStream());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//        sendCmd("RETR", 150, fileName);
//        return true;
//    }
//
//    public void init(String host, int port){
//        try{
//            ftpHost =host;
//            ftpPort =port;
//            socket = new Socket(ftpHost, ftpPort);
//            var status = read(socket.getInputStream());
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    //发送命令,若该命令有回调函数(通过callback属性传递),则在发送完命令后启用回调
//    public boolean sendCmd(String type, int expCode, String content) {
//        StringBuilder sb = new StringBuilder(type);
//        sb.append(" ");
//        sb.append(content);
//        sb.append("\r\n");
//
//        try {
//            var bytes = sb.toString().getBytes();
//            System.out.println("send:---------");
//            //System.out.println(NumUtil.bytesToHex(bytes));
//            System.out.println(new String(bytes));
//            var out = socket.getOutputStream();
//            out.write(bytes);
//            out.flush();
//            if(callback!=null){
//                new Thread(callback).start();
//                callback=null;
//            }
//            var resCode = read(socket.getInputStream());
//            return resCode == expCode;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 从指定网络流读取数据,解析并返回响应码
//     * 被动模式下,此函数将会改变 passivePort 属性的值
//     * @param in
//     * @return
//     * @throws IOException
//     */
//    public synchronized int read(InputStream in) throws IOException {
//        byte[] bytes = readBytesFromStream(in);
//        String s = new String(bytes);
//        System.out.println(s);
//        String[] ss= s.split("\r\n");
//        int status=-1;
//        for(String t : ss){
//            status = Integer.parseInt(t.substring(0,3));
//            if (status == 227) {
//                var p = Pattern.compile(".*\\(\\d+,\\d+,\\d+,\\d+,(\\d+),(\\d+)\\).*");
//                var m = p.matcher(s);
//                if (m.find()) {
//                    passivePort = Integer.parseInt(m.group(1)) * 256 + Integer.parseInt(m.group(2));
//                    System.err.println("端口被设置为:" + passivePort);
//                } else {
//                    System.err.println("未找到");
//                    throw new RuntimeException("wrong format");
//                }
//            }
//        }
//        if(status==-1) throw new RuntimeException("wrong format");
//        return status;
//    }
//
//
//    /**
//     * 从指定网络流里面读取数据,数据必须以0x0d 0x0a结尾
//     * @param in
//     * @return
//     * @throws IOException
//     */
//    public byte[] readBytesFromStream(InputStream in) throws  IOException{
//        byte[] bytes = new byte[0];
//        int c;
//        while (true) {
//            if (( c = in.available()) != 0) {
//                byte[] bytes2 = new byte[bytes.length + c];
//                System.arraycopy(bytes,0,bytes2,0,bytes.length);
//                in.read(bytes2,bytes.length,c);
//                bytes=bytes2;
////                System.out.println("---------------");
////                System.out.println("- "+new String(bytes));
////                System.out.println("- "+Arrays.toString(bytes));
////                System.out.println("---------------");
//                if(bytes.length>=2 && bytes[bytes.length-2]==13 && bytes[bytes.length-1]==10)break;
//            }
//        }
//        return bytes;
//    }
//
//
//
//
//    @Deprecated
//    public int read2(InputStream in,int timeOut) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String s;
//        int status = -1;
//        int count = 0;
//        socket.setSoTimeout(timeOut);
//        try{
//            while ((s=br.readLine())!=null) {
//                System.out.println("read:---------------");
//                System.out.println(s);
//                System.out.println("--------------------");
//                status = Integer.parseInt(s.substring(0, 3));
//                if (status == 227) {
//                    var p = Pattern.compile(".*\\(\\d+,\\d+,\\d+,\\d+,(\\d+),(\\d+)\\).*");
//                    var m = p.matcher(s);
//                    if (m.find()) {
//                        passivePort = Integer.parseInt(m.group(1)) * 256 + Integer.parseInt(m.group(2));
//                        System.err.println("端口被设置为:" + passivePort);
//                        return status;
//                    }
//                    System.err.println("未找到");
//                    throw new RuntimeException("wrong format");
//                }
//                socket.setSoTimeout(timeOut);
//            }
//        }catch (SocketTimeoutException ste){
//            return status;
//        }
//        return status;
//    }
//
//    public void readDirectoryFile(String filepath){
//        try{
//            File file=new File(filepath);
//            System.out.println("Now File Dictionary:");
//            /*指定位置不是目录而是一个文件时*/
//            if(!file.isDirectory()){
//                System.out.println("文件");
//                System.out.println("path=" + file.getPath());
//                System.out.println("absolutepath=" + file.getAbsolutePath());
//                System.out.println("name=" + file.getName());
//            }
//            else if(file.isDirectory()){
//                System.out.println("文件夹");
//                String[] filelist=file.list();
//                for(String temp:filelist){
//                    File fileItem=new File(filepath+"\\"+temp);
//                    if (!fileItem.isDirectory()) {
//                        System.out.println("path=" + fileItem.getPath());
//                        System.out.println("absolutepath="
//                                + fileItem.getAbsolutePath());
//                        System.out.println("           "+"name=" + fileItem.getName());
//
//                    } else if (fileItem.isDirectory()) {
//                        /*递归搜索文件*/
//                        readDirectoryFile(filepath + "\\" + temp);
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
