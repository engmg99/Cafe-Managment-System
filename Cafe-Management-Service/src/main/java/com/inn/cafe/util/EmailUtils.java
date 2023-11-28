package com.inn.cafe.util;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtils {
	@Autowired
	private JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("goyalmunish9906@gmail.com");
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);
		if (list != null && list.size() > 0) {
			msg.setCc(getCCMembers(list));
		}
		emailSender.send(msg);
	}

	private String[] getCCMembers(List<String> ccList) {
		String[] cc = new String[ccList.size()];
		for (int i = 0; i < ccList.size(); i++) {
			cc[i] = ccList.get(i);
		}
		return cc;
	}

	public void forgotPwdMail(String to, String subject, String pwd) throws MessagingException {
		MimeMessage msg = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setFrom("goyalmunish9906@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to
				+ " <br><b>Password: </b> " + pwd
				+ "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
		msg.setContent(htmlMsg, "text/html");
		emailSender.send(msg);

	}
}
