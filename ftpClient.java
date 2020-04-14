import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class ftpClient {
    private int pasvDataPort;
    private static String hostname;
    private static String user;
    private static String passwd;

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
            writer.write("PASV");writer.newLine();writer.flush();
            this.pasvDataPort=read(input);
            System.out.println("Passive Port Number:"+this.pasvDataPort);
            /*被动模式下数据端口是服务器分配的
            * 选择PASV模式后服务器会传回(a,b,c,d,e,f)
            * 其中a.b.c.d是IP地址 ex256+f是对应的服务器数据端口
            * */
            writer.write("LIST");writer.newLine();writer.flush();
            Socket pasvSock=new Socket(hostname,this.pasvDataPort);
            InputStream pasvInput=pasvSock.getInputStream();
            OutputStream pasvOutput=pasvSock.getOutputStream();
            read(input);
            readData(pasvInput);


        }catch (Exception e){
            e.printStackTrace();
            /**/
        }
        return 1;
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
