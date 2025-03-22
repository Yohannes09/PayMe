//package com.payme.app.services.main;
//
//import com.sendgrid.Method;
//import com.sendgrid.Request;
//import com.sendgrid.Response;
//import com.sendgrid.SendGrid;
//import com.sendgrid.helpers.mail.Mail;
//import com.sendgrid.helpers.mail.objects.Content;
//import com.sendgrid.helpers.mail.objects.Email;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//    private static final String API_KEY = "your-sendgrid-api-key";
//
//    public void sendEmail(String to, String subject, String body) {
//        Email from = new Email("yohannesm8814@gmail.com");
//        Email recipient = new Email(to);
//        Content content = new Content("text/plain", body);
//        Mail mail = new Mail(from, subject, recipient, content);
//
//        SendGrid sg = new SendGrid(API_KEY);
//        Request request = new Request();
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sg.api(request);
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
//        } catch (Exception ex) {
//            System.err.println(ex.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        EmailService emailService = new EmailService();
//        emailService.sendEmail("yohannesm8815@gmail.com", "test", "Hello this is testing the functionallity of this API");
//    }
//}
//
