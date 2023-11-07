import java.util.*;
import java.text.*;


public class Message {
    // 宣告郵件的標頭為public型態
    public String Headers;
	// 宣告郵件正文為public型態
    public String Body;

    // 宣告字串From為private型態
    private String From;
	// 宣告字串To為private型態
    private String To;

    // 宣告換行符
    private static final String CRLF = "\r\n";

    // 構造函數，創建郵件消息
    public Message(String from, String to, String subject, String text) {
	// 清除發件人和收件人地址的多餘空格
	From = from.trim();
	To = to.trim();
	// 構件郵件頭部
	Headers = "From: " + From + CRLF;
	Headers += "To: " + To + CRLF;
	Headers += "Subject: " + subject.trim() + CRLF;

	// 獲取當前時間，並格式化為符合郵件標準的日期格式
	SimpleDateFormat format = 
	    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
	String dateString = format.format(new Date());
	Headers += "Date: " + dateString + CRLF;
	Body = text;
    }

    // 獲取getFrom字串
    public String getFrom() {
	return From;
    }
    // 獲取getTo字串
    public String getTo() {
	return To;
    }

    // 驗證郵件地址的有效性
    public boolean isValid() {
	int fromat = From.indexOf('@');
	int toat = To.indexOf('@');

	if(fromat < 1 || (From.length() - fromat) <= 1) {
	    System.out.println("Sender address is invalid");
	    return false;
	}
	if(toat < 1 || (To.length() - toat) <= 1) {
	    System.out.println("Recipient address is invalid");
	    return false;
	}
	if(fromat != From.lastIndexOf('@')) {
	    System.out.println("Sender address is invalid");
	    return false;
	}
	if(toat != To.lastIndexOf('@')) {
	    System.out.println("Recipient address is invalid");
	    return false;
	}	
	return true;
    }
    
    // 轉換為字符串形式，用於顯示郵件內容
    public String toString() {
	String res;

	res = Headers + CRLF;
	res += Body;
	return res;
    }
}