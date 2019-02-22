package miniproject.certificate;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class GmailSend {
	public void GmailSet(String user, String text, String content){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");    
        properties.put("mail.smtp.host", "smtp.gmail.com");      // smtp 서버 호스트
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port", "587");                 // gmail 포트
        
        //구글 계정 인증(발송자)
        Authenticator auth = new MyAuthentication();
          
        //session 생성 및  MimeMessage생성
        Session session = Session.getDefaultInstance(properties, auth);
        MimeMessage msg = new MimeMessage(session);
        String fromName = "This_is_CatchMind";
        String charSet = "UTF-8";
         
        try{
            // 편지보낸시간 설정
            msg.setSentDate(new Date());
              
            // 송신자 설정
            InternetAddress from = new InternetAddress() ;
            from = new InternetAddress(new String(fromName.getBytes(charSet), "8859_1") + "<songming05@gmail.com>");
            msg.setFrom(from);
              
            // 수신자 설정
            InternetAddress to = new InternetAddress(user);
            msg.setRecipient(Message.RecipientType.TO, to);
             
            // 제목 설정
            msg.setSubject(text, "UTF-8");
             
            msg.setText(content, "UTF-8");  //내용 설정
             
            // 메일 송신
            Transport.send(msg);   
             
            System.out.println("메일 발송을 완료하였습니다.");
        }catch (AddressException addr_e) {  //예외처리 주소를 입력하지 않을 경우
            JOptionPane.showMessageDialog(null, "메일을 입력해주세요", "메일주소입력", JOptionPane.ERROR_MESSAGE);
            addr_e.printStackTrace();
        }catch (MessagingException msg_e) { //메시지에 이상이 있을 경우
            JOptionPane.showMessageDialog(null, "메일을 제대로 입력해주세요.", "오류발생", JOptionPane.ERROR_MESSAGE);
            msg_e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
