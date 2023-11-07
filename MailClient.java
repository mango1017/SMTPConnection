import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

// 郵件客戶端主類別，繼承自Frame類別
public class MailClient extends Frame {
     // 創建按鈕，用於發送、清除和退出
    private Button btSend = new Button("Send");
    private Button btClear = new Button("Clear");
    private Button btQuit = new Button("Quit");
	// 創建標籤和文本框，用於本地郵件伺服器、發見人、收件人、主题和消息
    private Label serverLabel = new Label("Local mailserver:");
    private TextField serverField = new TextField("", 40);
    private Label fromLabel = new Label("From:");
    private TextField fromField = new TextField("", 40);
    private Label toLabel = new Label("To:"); 
    private TextField toField = new TextField("", 40);
    private Label subjectLabel = new Label("Subject:");
    private TextField subjectField = new TextField("", 40);
    private Label messageLabel = new Label("Message:");
    private TextArea messageText = new TextArea(10, 40);

    // 構造函數，設置窗口介面
    public MailClient() {
	super("Java Mailclient");
	
	// 創建包含服務器訊息的面板
	Panel serverPanel = new Panel(new BorderLayout());
	// 創建包含發件人訊息的面板
	Panel fromPanel = new Panel(new BorderLayout());
	// 創建包含收件人信息的面板
	Panel toPanel = new Panel(new BorderLayout());
	// 創建包含主题信息的面板
	Panel subjectPanel = new Panel(new BorderLayout());
	// 創建包含消息文本的面板
	Panel messagePanel = new Panel(new BorderLayout());

	// 將標籤和文本框添加到各個面板中
	serverPanel.add(serverLabel, BorderLayout.WEST);
	serverPanel.add(serverField, BorderLayout.CENTER);
	fromPanel.add(fromLabel, BorderLayout.WEST);
	fromPanel.add(fromField, BorderLayout.CENTER);
	toPanel.add(toLabel, BorderLayout.WEST);
	toPanel.add(toField, BorderLayout.CENTER);
	subjectPanel.add(subjectLabel, BorderLayout.WEST);
	subjectPanel.add(subjectField, BorderLayout.CENTER);
	messagePanel.add(messageLabel, BorderLayout.NORTH);	
	messagePanel.add(messageText, BorderLayout.CENTER);

	// 創建一個垂直布局的面板，将所有字段面板添加到其中
	Panel fieldPanel = new Panel(new GridLayout(0, 1));
	fieldPanel.add(serverPanel);
	fieldPanel.add(fromPanel);
	fieldPanel.add(toPanel);
	fieldPanel.add(subjectPanel);

    // 創建一个按钮面板，添加發送、清除和退出按钮，並為按钮添加事件監聽器
	Panel buttonPanel = new Panel(new GridLayout(1, 0));
	btSend.addActionListener(new SendListener());
	btClear.addActionListener(new ClearListener());
	btQuit.addActionListener(new QuitListener());
	buttonPanel.add(btSend);
	buttonPanel.add(btClear);
	buttonPanel.add(btQuit);
	
	
	// 將字斷面板、消息面板和按钮面板添加到主窗口中
	add(fieldPanel, BorderLayout.NORTH);
	add(messagePanel, BorderLayout.CENTER);
	add(buttonPanel, BorderLayout.SOUTH);
	pack();
	show();
    }

    // 主方法，程序入口点
    static public void main(String argv[]) {
	new MailClient();
    }

    // 發送按钮事件監聽器
    class SendListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    System.out.println("Sending mail");
	    
	    // 檢查本地郵件服務器是否為空
	    if ((serverField.getText()).equals("")) {
		System.out.println("Need name of local mailserver!");
		return;
	    }
 
	    // 檢查發件人是否為空
	    if((fromField.getText()).equals("")) {
		System.out.println("Need sender!");
		return;
	    }

		// 检查收件人是否為空
	    if((toField.getText()).equals("")) {
		System.out.println("Need recipient!");
		return;
	    }
        /* 
		// 創建郵件消息對象
	    Message mailMessage = new Message(fromField.getText(), 
					      toField.getText(), 
					      subjectField.getText(), 
					      messageText.getText());

	    // 檢查郵件消息是否有效
	    if(!mailMessage.isValid()) {
		return;
	    }

	      // 創建邮件信封
		Envelope envelope;
	    try {
		 envelope = new Envelope(mailMessage, 
						 serverField.getText());
	    } catch (UnknownHostException e) {
		// 處理未知主機異常
		return;
	    }
	    try {
		// 創建SMTP連線並發送郵件
		SMTPConnection connection = new SMTPConnection(envelope);
		connection.send(envelope);
		connection.close();
	    } catch (IOException error) {
		System.out.println("Sending failed: " + error);
		return;
	    }
	    System.out.println("Mail sent succesfully!");
	  */

	  /* Multiple account */
	  String[] accounts = toField.getText().split(";");
	  // 检查是否有超過兩位的收信者
	  if (accounts.length > 2) {
		System.out.println("注意你的收信者已超過兩位");
		return;
	}
	  for (int i = 0; i < accounts.length; i++) {
		  System.out.println(accounts[i]);
          // 檢查收件人地址是否包含 @ 符号
		  if (!accounts[i].contains("@")) {
			System.out.println("收件人地址無效：" + accounts[i]);
			return;
		}
		  Message mailMessage = new Message(fromField.getText(),
				  accounts[i],
				  subjectField.getText(),
				  messageText.getText());

		  if (!mailMessage.isValid()) {
			  return;
		  }

		  try {
			  Envelope envelope = new Envelope(mailMessage,
					  serverField.getText());

			  SMTPConnection connection = new SMTPConnection(envelope);
			  connection.send(envelope);
			  connection.close();
		  } catch (IOException error) {
			  System.out.println("Sending failed: " + error);
			  return;
		  }
		  System.out.println("Mail sent succesfully!");
	  }
	}
}

    // 清除按钮事件監聽器
    class ClearListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    System.out.println("Clearing fields");
	    fromField.setText("");
	    toField.setText("");
	    subjectField.setText("");
	    messageText.setText("");
	}
    }

    // 退出按鈕事件監聽器
    class QuitListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    System.exit(0);
	}
    }
}