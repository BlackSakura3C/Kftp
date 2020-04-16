//import java.io.*;
//import java.net.Socket;
//import java.util.List;
//
//public class ftpDownload extends Thread{
//    private String filename;
//    private BufferedWriter writer;
//    private InputStream input;
//    private int pasvDataPort;
//    private String hostname;
//    private List<String> fileDirectory;
//    public ftpDownload(String hostname, String filename, BufferedWriter writer, InputStream input, List<String> fileDirectory){
//        this.hostname=hostname;
//        this.filename=filename;
//        this.writer=writer;
//        this.input=input;
//        this.fileDirectory=fileDirectory;
//    }
//    @Override
//    public void run() {
//        /*
//         *
//         * 后面需要调整成为Windows下的当前路径
//         *
//         * */
//        if(this.fileDirectory.indexOf(filename)==-1){
//            System.out.println("<<< Searching File...");
//            System.out.println("<<< Failure: File is not exsited.");
//            return;
//        }
//        String path="C:\\Users\\20173\\Desktop\\ftptest\\"+filename;
//        if(fileExist(path)){
//            System.out.println("<<< Downloading...");
//            System.out.println("<<< Failure: File exsited.");
//            return;
//        }
//        try{
//            usePasv(writer,input);
//            writer.write("RETR "+filename);writer.newLine();writer.flush();
//            Socket downloadSock=new Socket(hostname,this.pasvDataPort);
//            InputStream pasvInput=downloadSock.getInputStream();
//            String temp;
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
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        //readData(pasvInput);
////        read(input);
//    }
//    public void usePasv(BufferedWriter writer,InputStream input) throws IOException {
//        writer.write("PASV");writer.newLine();writer.flush();
//        this.pasvDataPort=read(input);
//        System.out.println("Passive Port Number:"+this.pasvDataPort);
//    }
//
//    public boolean fileExist(String path){
//        File file=new File(path);
//        if(file.exists()) return true;
//        else return false;
//    }
//
//    public int read(InputStream in) throws IOException{
//        while(true){
//            if(in.available()!=0){
//                byte[] bytes = new byte[in.available()];
//                in.read(bytes);
//                String s = new String(bytes);
//                System.out.print("<<< "+s);
//                String[] ss= s.split("\r\n");
//                int status=-1;
//                for(String t : ss){
//                    status = Integer.parseInt(t.substring(0,3));
//                    if(status!=220){
//                        /*返回被动模式数据端口号*/
//                        if(status==227) {
//                            String[] portCalFront=t.split(",");
//                            /*
//                             * Attention )是特殊符号要转义 分隔符中也要转义
//                             * */
//                            String[] portCalBack=portCalFront[5].split("\\)");
//                            return (Integer.valueOf(portCalFront[4])*256)+Integer.valueOf(portCalBack[0]);
//                        }
//                        else
//                            return  status;
//                    }
//                    //System.out.println("a"+status);
//                }
//                if(status==-1) throw new RuntimeException("wrong format");
//                else if(status==220)return 530;
//            }
//        }
//    }
//}
