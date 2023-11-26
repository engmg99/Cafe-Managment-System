package com.inn.cafe.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
}
