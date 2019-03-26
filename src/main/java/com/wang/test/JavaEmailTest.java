package com.wang.test;

import com.sun.mail.util.MimeUtil;
import com.wang.util.SessionFactory;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author 王念
 * @create 2019-03-26 19:41
 */
public class JavaEmailTest {
    private static final String ENCODING="UTF-8";//字符编码
    private static final String CONTENTTYPE="text/html;charset=utf-8";//网页内容编码
    private static final String SENDER="1062572204@qq.com";//发件人
    private static final String RECIPIENT="1161205193@qq.com";//收件人
    public static final String AUTHORZATIONCODE="icnccuoqukqcbdbb";//授权码
    public static final String IMGPATH="G:\\\\手机备份\\\\毕业啦/QQ图片20150530132933.jpg";
    public static final String FILEPATH="F:\\\\IdeaProject\\\\JavaEmailDemo.zip";
    public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
        //创建会话对象
        Session session= Session.getInstance(SessionFactory.getInstance().getPro());
        //开启日志
        session.setDebug(true);
        MimeMessage mimeMessage=createMime(session);
        try {
            //建立连接
            Transport transport=session.getTransport();
            transport.connect(SENDER,AUTHORZATIONCODE);
            //发送邮件
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            //关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
    public static MimeMessage createMime(Session session) throws UnsupportedEncodingException, MessagingException {
        //1.创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //2.设置发件人地址
        msg.setFrom(new InternetAddress(SENDER,"王念",ENCODING));
        /**
         * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(RECIPIENT,ENCODING));
        //4.设置邮件主题
        msg.setSubject("用Java优雅的发送邮件(包含图片和附件)",ENCODING);

        //下面是设置邮件正文

        // 5. 创建图片"节点"
        MimeBodyPart image = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh = new DataHandler(new FileDataSource(IMGPATH));
        // 将图片数据添加到"节点"
        image.setDataHandler(dh);
        // 为"节点"设置一个唯一编号（在文本"节点"将引用该ID）
        image.setContentID("mailTestPic");

        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        text.setContent("这是一张图片<br/><img src='cid:mailTestPic'/>",CONTENTTYPE);

        // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
        MimeMultipart mm_text_image = new MimeMultipart();
        mm_text_image.addBodyPart(text);
        mm_text_image.addBodyPart(image);
        mm_text_image.setSubType("related");    // 关联关系

        // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        // 上面的 mailTestPic 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
        MimeBodyPart text_image = new MimeBodyPart();
        text_image.setContent(mm_text_image);

        // 9. 创建附件"节点"
        MimeBodyPart attachment = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh2 = new DataHandler(new FileDataSource(FILEPATH));
        // 将附件数据添加到"节点"
        attachment.setDataHandler(dh2);
        // 设置附件的文件名（需要编码）
        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text_image);
        mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
        mm.setSubType("mixed");         // 混合关系

        // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        msg.setContent(mm);
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        return msg;
    }

}
