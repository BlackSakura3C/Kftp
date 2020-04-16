//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.Socket;
//
//public class ftpDownload extends Thread{
//    private String filename;
//    private BufferedWriter writer;
//    private InputStream input;
//    public ftpDownload(String filename, BufferedWriter writer, InputStream input){
//        this.filename=filename;
//        this.writer=writer;
//        this.input=input;
//    }
//    @Override
//    public void run(){
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
//        usePasv(writer,input);
//        writer.write("RETR "+filename);writer.newLine();writer.flush();
//        Socket downloadSock=new Socket(hostname,this.pasvDataPort);
//        InputStream pasvInput=downloadSock.getInputStream();
//        read(input);
//
//        String temp;
//        while(true){
//            if(pasvInput.available()!=0){
//                byte[] bytes = new byte[pasvInput.available()];
//                pasvInput.read(bytes);
//                temp = new String(bytes);
//                break;
//            }
//        }
//        try {
//            FileWriter fileWriter=new FileWriter(path);
//            fileWriter.write(temp);
//            fileWriter.flush();
//            fileWriter.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        //readData(pasvInput);
//        read(input);
//    }
//    public void usePasv(BufferedWriter writer,InputStream input) throws IOException {
//        writer.write("PASV");writer.newLine();writer.flush();
//        this.pasvDataPort=read(input);
//        System.out.println("Passive Port Number:"+this.pasvDataPort);
//    }
//}
