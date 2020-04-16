import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/*
* 111.230.233.136
* admin
* admin
* */


public class ftpClient {
    private int pasvDataPort;
    private static String hostname;
    private static String user;
    private static String passwd;
    private List<String> fileDirectory=new ArrayList<>();
    private String basePath="C:\\Users\\20173\\Desktop\\ftptest\\";
    public static void main(String[] args) throws IOException {
        ftpClient test=new ftpClient();
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入将要连接的主机IP:");
        hostname=scanner.nextLine();
        System.out.println("用户名:");
        user=scanner.nextLine();
        System.out.println("密码:");
        passwd=scanner.nextLine();
//        hostname="47.97.221.221";
//        user="kkc";
//        passwd="kkc";
        //"47.97.221.221"
        Socket sock=new Socket(hostname,21);
        System.out.println("LocalAddr"+sock.getLocalSocketAddress());
        System.out.println("RemoteAddr"+sock.getRemoteSocketAddress());
        try (InputStream input = sock.getInputStream()) {
            try (OutputStream output = sock.getOutputStream()) {
                if(test.login(input,output)==1){
                    System.out.println("HHH");
                }
                test.terminal(input, output);
            }
        }
        sock.close();
        System.out.print("Disconnect");
    }
    public int login(InputStream input, OutputStream output) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        Scanner scanner=new Scanner(System.in);
        try {
            read(input);
//            String welcome;
//            while ((welcome=reader.readLine())!=null){
//                System.out.println(welcome);
//            }
//            if(!(welcome.substring(0,3).equals("220"))){
//                return -220;
//                /*服务器连接失败
//                * 可能服务器没有开启服务等原因*/
//            }

            writer.write("USER "+user);writer.newLine();writer.flush();
            read(input);
            writer.write("PASS "+passwd);writer.newLine();writer.flush();
            read(input);
            writer.write("PWD");writer.newLine();writer.flush();
            read(input);
            writer.write("CWD /"+passwd);writer.newLine();writer.flush();
            read(input);
            writer.write("PWD");writer.newLine();writer.flush();
            read(input);
            writer.write("TYPE A");writer.newLine();writer.flush();
            read(input);
            /*
             * 这个地方后期可以考虑手动增加主动模式
             * */

            listFile(writer,input);
            System.out.print("Which file do you want to download:");
            String filename=scanner.nextLine();
            downLoad(filename,writer,input);
            readDirectoryFile(basePath);
            System.out.print("Which file do you want to upload:");
            filename=scanner.nextLine();
            upload(filename,writer,input);


        }catch (Exception e){
            e.printStackTrace();
            /**/
        }
        return 1;
    }

    /*被动模式下数据端口是服务器分配的
     * 选择PASV模式后服务器会传回(a,b,c,d,e,f)
     * 其中a.b.c.d是IP地址 ex256+f是对应的服务器数据端口
     * */
    public void usePasv(BufferedWriter writer,InputStream input) throws IOException{
        writer.write("PASV");writer.newLine();writer.flush();
        this.pasvDataPort=read(input);
        System.out.println("Passive Port Number:"+this.pasvDataPort);
    }
    public void listFile(BufferedWriter writer,InputStream input) throws IOException{
        usePasv(writer,input);
        writer.write("LIST");writer.newLine();writer.flush();
        Socket pasvSock=new Socket(hostname,this.pasvDataPort);
        InputStream pasvInput=pasvSock.getInputStream();
        //OutputStream pasvOutput=pasvSock.getOutputStream();
        /*读数据前和读数据后都会有server的返回命令 所以使用了两边read*/
        read(input);
        readData(pasvInput);
        read(input);

    }
    public void downLoad(String filename,BufferedWriter writer,InputStream input) throws IOException{
        /*
         *
         * 后面需要调整成为Windows下的当前路径
         *
         * */
        if(this.fileDirectory.indexOf(filename)==-1){
            System.out.println("<<< Searching File...");
            System.out.println("<<< Failure: Server File is not exsited.");
            return;
        }
        String path=basePath+filename;
        if(fileExist(path)){
            System.out.println("<<< Downloading...");
            System.out.println("<<< Failure: File exsited.");
            return;
        }
        else {
            System.out.println("Downloading...");
        }
        writer.write("TYPE I");writer.newLine();writer.flush();
        read(input);
        usePasv(writer,input);
        writer.write("RETR "+filename);writer.newLine();writer.flush();
        Socket downloadSock=new Socket(hostname,this.pasvDataPort);
        InputStream pasvInput=downloadSock.getInputStream();
        read(input);

        try {
            BufferedInputStream in=null;
            BufferedOutputStream out=null;
            in=new BufferedInputStream(pasvInput);
            out=new BufferedOutputStream(new FileOutputStream(path));
            byte[] bytes = new byte[1024];
            int len=-1;
            int tranferSize=0;
            while((len=in.read(bytes))!=-1){
                tranferSize+=len;
                out.write(bytes,0,len);
            }
            in.close();
            out.close();
//            while(true){
//                if(pasvInput.available()!=0){
//                    byte[] bytes = new byte[pasvInput.available()];
//                    pasvInput.read(bytes);
//                    temp = new String(bytes);
//                    break;
//                }
//            }

//            FileWriter fileWriter=new FileWriter(path);
//            fileWriter.write(temp);
//            fileWriter.flush();
//            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //readData(pasvInput);
        read(input);
    }

    public void upload(String filename,BufferedWriter writer,InputStream input) throws IOException{
        String path=basePath+filename;
        if(this.fileDirectory.indexOf(filename)!=-1){
            System.out.println("<<< Uploading...");
            System.out.println("<<< Failure: File is already exsited.");
            return;
        }
        usePasv(writer,input);
        writer.write("STOR "+filename);writer.newLine();writer.flush();
        Socket uploadSock=new Socket(hostname,this.pasvDataPort);
        //InputStream pasvInput=downloadSock.getInputStream();
        OutputStream pasvOutput=uploadSock.getOutputStream();
        read(input);
        try{
            BufferedOutputStream out=null;
            BufferedInputStream in=null;
            in=new BufferedInputStream(new FileInputStream(path));
            out=new BufferedOutputStream(pasvOutput);
            byte[] bytes = new byte[1024];
            int len=-1;
            int tranferSize=0;
            while((len=in.read(bytes))!=-1){
                tranferSize+=len;
                out.write(bytes,0,len);
            }
            in.close();
            out.close();


        }catch (Exception e){
            e.printStackTrace();
        }
        read(input);
    }

    public void readDirectoryFile(String filepath){
        try{
            File file=new File(filepath);
            System.out.println("Now File Dictionary:");
            /*指定位置不是目录而是一个文件时*/
            if(!file.isDirectory()){
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());
            }
            else if(file.isDirectory()){
                System.out.println("文件夹");
                String[] filelist=file.list();
                for(String temp:filelist){
                    File fileItem=new File(filepath+"\\"+temp);
                    if (!fileItem.isDirectory()) {
                        System.out.println("path=" + fileItem.getPath());
                        System.out.println("absolutepath="
                                + fileItem.getAbsolutePath());
                        System.out.println("           "+"name=" + fileItem.getName());

                    } else if (fileItem.isDirectory()) {
                        /*递归搜索文件*/
                        readDirectoryFile(filepath + "\\" + temp);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean fileExist(String path){
        File file=new File(path);
        if(file.exists()) return true;
        else return false;
    }
    //    public String commendCode(String message){
//
//    }
//    public int read(InputStream in) throws IOException{
//        while(true){
//            if(in.available()!=0){
//                byte[] bytes=new byte[in.available()];
//                in.read(bytes);
//                System.out.println(bytes);
//            }
//        }
//    }
    public void readData(InputStream in) throws IOException{
        while(true){
            if(in.available()!=0){
                byte[] bytes = new byte[in.available()];
                in.read(bytes);
                String s = new String(bytes);
                System.out.println("<<< File Directory:");
                System.out.print(s);
                /*
                 * 保存文件列表用于判断文件存在等
                 * */
                String[] dirctionary=s.split("\r\n");
                for(String temp:dirctionary){
                    String[] file=temp.split("\\s+");
                    this.fileDirectory.add(new String(file[8]));
                }

                return;
            }
        }
    }
    public int read(InputStream in) throws IOException{
        while(true){
            if(in.available()!=0){
                byte[] bytes = new byte[in.available()];
                in.read(bytes);
                String s = new String(bytes);
                System.out.print("<<< "+s);
                String[] ss= s.split("\r\n");
                int status=-1;
                for(String t : ss){
                    status = Integer.parseInt(t.substring(0,3));
                    if(status!=220){
                        /*返回被动模式数据端口号*/
                        if(status==227) {
                            String[] portCalFront=t.split(",");
                            /*
                             * Attention )是特殊符号要转义 分隔符中也要转义
                             * */
                            String[] portCalBack=portCalFront[5].split("\\)");
                            return (Integer.valueOf(portCalFront[4])*256)+Integer.valueOf(portCalBack[0]);
                        }
                        else
                            return  status;
                    }
                    //System.out.println("a"+status);
                }
                if(status==-1) throw new RuntimeException("wrong format");
                else if(status==220)return 530;
            }
        }
    }


    private void terminal(InputStream input, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        //System.out.println("[server] " + reader.readLine());
        for (;;) {
            System.out.print(">>>(quit退出) "); // 打印提示
            String s = scanner.nextLine(); // 读取一行输入
            if (s.equals("quit")) {
                break;
            }
            writer.write(s);
            writer.newLine();
            writer.flush();
            read(input);

        }
    }
}