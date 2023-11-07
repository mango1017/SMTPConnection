import java.net.*;
import java.io.*;
import java.util.*;

import javax.net.ssl.SSLSocket; 
import javax.net.ssl.SSLSocketFactory;
// java套件區 //

public class SMTPConnection {
    
    private Socket connection; // for連線用的socket物件
    private BufferedReader fromServer; // 宣告BufferReader用來讀取伺服器資料
    private DataOutputStream toServer;  // DataOutputStream用來傳送資料到Server

    private static final int SMTP_PORT = 465; // SMTP連接埠465
    private static final String CRLF = "\r\n"; // SMTP協定使用的分隔符


    private boolean isConnected = false; // 標記SMTP是否連接到伺服器

    public SMTPConnection(Envelope envelope) throws IOException {
        // 建立SMTP連線，利用SSL加密
        connection = (SSLSocket) (SSLSocketFactory.getDefault()).createSocket(envelope.DestAddr, SMTP_PORT);
        fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        toServer = new DataOutputStream(connection.getOutputStream());



        String reply = fromServer.readLine();  // 從伺服器讀取並解析伺服器回應
        if (parseReply(reply) != 220) {   // 如果回應代碼不是220，代表伺服器未就緒，連線失敗
            throw new IOException("Connection Failed!");
        } else {
         // 伺服器已就緒，向伺服器打招呼,回傳代碼250
        String localhost = InetAddress.getLocalHost().getHostName();
        sendCommand("HELO " + localhost + CRLF, 250);

            isConnected = true;
        }
    }


    public void send(Envelope envelope) throws IOException {
        // SMTP認證
         
        String username = "azir891017@gmail.com";  
        String password = "hhat pbak dril aopl";
        sendCommand("AUTH LOGIN", 334); // 通知伺服器要進行驗證
        
        sendCommand(Base64.getEncoder().encodeToString(username.getBytes()), 334);  // 傳送base64編碼的使用者名稱
        sendCommand(Base64.getEncoder().encodeToString(password.getBytes()), 334);  // 傳送base64編碼的使用者密碼
        sendCommand("MAIL FROM: <" + envelope.getSender() + ">", 250); // 指定寄件人郵件地址
        sendCommand("RCPT TO: <" + envelope.getRecipient() + ">", 250); // 指定收件者的郵件地址
        sendCommand("DATA", 354); /// 準備傳送郵件內容
     
        sendCommand(envelope.getBody()+CRLF+".", 250);  // 傳送郵件內容並以.結束
        //sendCommand("." , 250); // End the email data
        
        
    }


   
    public void close() {
	isConnected = false;
	try {
	    sendCommand( "QUIT", 221); // 通知伺服器關閉連線
	    connection.close(); // 關閉連線
	} catch (IOException e) {
	    System.out.println("Unable to close connection: " + e);
	    isConnected = true;
	}
    }
    

    private void sendCommand(String command, int rc) throws IOException {
	// 傳送指令到伺服器
    toServer.writeBytes(command + CRLF);
    System.out.println(command);

    // 讀取伺服器回應
    String serverResponse = fromServer.readLine();
    System.out.println(serverResponse);

    // 解析回應碼
    int replyCode = parseReply(serverResponse);

}

    private int parseReply(String reply) {
        try {
            // 解析伺服器回應中的回應碼
            String[] parts = reply.split(" ");
            if (parts.length > 0) {
                return Integer.parseInt(parts[0]);
            }
         } catch (NumberFormatException e) {

        }
         // 如果解析出錯，回傳-1代表無效的回應碼
     
        return -1;
    }

    protected void finalize() throws Throwable {
	if(isConnected) {
	    close();
	}
	super.finalize();
    }
}
