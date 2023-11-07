import java.io.*;
import java.net.*;
import java.util.*;


// 郵件信封類別
public class Envelope {
   // 宣告寄件人地址為public型態
    public String Sender;

    // 宣告收件人地址為public型態
    public String Recipient;

    // 宣告目標郵件伺服器主機名稱為public型態
    public String DestHost;
    public InetAddress DestAddr;

    // 宣告郵件訊息主機名稱為private型態
    public Message  Message;

    // 建構函式，建立郵件信封
    public Envelope(Message message, String localServer) throws UnknownHostException {
	
	Sender = message.getFrom();
	Recipient = message.getTo();

	// 轉譯郵件訊息，處理以點（.）開頭的行
	Message = escapeMessage(message);

	
	DestHost = localServer;
	try {
	    DestAddr = InetAddress.getByName(DestHost);
	} catch (UnknownHostException e) {
	    System.out.println("Unknown host: " + DestHost);
	    System.out.println(e);
	    throw e;
	}
	return;
    }

    // 轉譯郵件訊息
    private Message escapeMessage(Message message) {
	String escapedBody = "";
	String token;
	StringTokenizer parser = new StringTokenizer(message.Body, "\n", true);

	while(parser.hasMoreTokens()) {
	    token = parser.nextToken();
	    if(token.startsWith(".")) {
		token = "." + token;
	    }
	    escapedBody += token;
	}
	message.Body = escapedBody;
	return message;
    }

    // 轉換為字串形式，用於顯示資訊
    public String toString() {
	String res = "Sender: " + Sender + '\n';
	res += "Recipient: " + Recipient + '\n';
	res += "MX-host: " + DestHost + ", address: " + DestAddr + '\n';
	res += "Message:" + '\n';
	res += Message.toString();
	
	return res;
    }
	// 獲取郵件伺服器主機名稱
	public String getServer()
	{
		return this.DestHost;
	}
	// 獲取寄件人地址
	public String getSender()
	{
		return this.Sender;
	}
	// 獲取收件人地址
	public String getRecipient()
	{
		return this.Recipient;
	}
	// 獲取郵件訊息正文
	public String getBody()
	{
		return this.Message.toString();
	}
}