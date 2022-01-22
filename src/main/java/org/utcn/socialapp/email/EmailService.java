package org.utcn.socialapp.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email.");
            helper.setFrom("raluca.jucan@gmail.com");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Fail to send email. ", e);
            throw new IllegalStateException("Failed to send email!");
        }
    }

    public String buildEmail(String value1, String value2, String value3, String link){
        return "<!DOCTYPE html>\n" +
                "<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" " +
                "xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<meta charset=\"utf-8\"/>\n" +
                "<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\" " +
                "type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Nunito\" rel=\"stylesheet\" " +
                "type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Droid+Serif\" rel=\"stylesheet\" " +
                "type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Cabin\" rel=\"stylesheet\" " +
                "type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Ubuntu\" rel=\"stylesheet\" " +
                "type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Oxygen\" rel=\"stylesheet\" " +
                "type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Roboto+Slab\" rel=\"stylesheet\" " +
                "type=\"text/css\"/>\n" +
                "<style>* {box-sizing: border-box;} body {margin: 0;\tpadding: 0;\t}a[x-apple-data-detectors] {\n" +
                "color: inherit !important;text-decoration: inherit !important;}\t#MessageViewBody a {\n" +
                "color: inherit;text-decoration: none;} p {line-height: inherit} @media (max-width:670px) {\n" +
                ".icons-inner {text-align: center;}\n" +
                ".icons-inner td {margin: 0 auto;}\n" +
                ".row-content {width: 100% !important;}\n" +
                ".stack .column {width: 100%;display: block;}}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body style=\"background-color: #fbfbfb; margin: 0; padding: 0; -webkit-text-size-adjust: none; " +
                "text-size-adjust: none;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\"" +
                " style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #fbfbfb;\" " +
                "width=\"100%\">\n" +
                "</td></tr></tbody></table></td></tr></tbody></table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-2\" " +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: " +
                "#175df1;\" width=\"100%\">\n" +
                "<tbody><tr><td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\"" +
                " role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: " +
                "650px;\" width=\"650\">\n" +
                "<tbody><tr>\n" +
                "<td class=\"column\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; " +
                "text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 15px; border-top: 0px; " +
                "border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\" role=\"presentation\" " +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
                "<tr><td style=\"padding-left:10px;padding-right:10px;padding-top:25px;\"><div style=\"font-family: " +
                "sans-serif\"><div style=\"font-size: 14px; font-family: Cabin, Arial, Helvetica Neue, Helvetica, " +
                "sans-serif; mso-line-height-alt: 16.8px; color: #ffffff; line-height: 1.2;\">\n" +
                "<p style=\"margin: 0; font-size: 30px; text-align: center;\"><strong><span style=\"font-size:38px;" +
                "\">"+value1+"</span></strong></p>\n" +
                "</div></div></td></tr></table>\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\" role=\"presentation\" " +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
                "<tr><td style=\"padding-left:10px;padding-right:10px;padding-top:10px;\"><div style=\"font-family: " +
                "sans-serif\">\n" +
                "<div style=\"font-size: 14px; mso-line-height-alt: 21px; color: #ffffff; line-height: 1.5; " +
                "font-family: Cabin, Arial, Helvetica Neue, Helvetica, sans-serif;\">\n" +
                "<p style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 33px;\"><span " +
                "style=\"font-size:22px;color:#f9c253;\">"+value2+"</span></p>\n" +
                "<a href=\"" + link + "\" style=\"margin: 0; font-size: 14px; color:white; text-decoration:none; text-align: center; mso-line-height-alt: 51px;\"><span " +
                "style=\"font-size:34px;\">"+value3+"</span></a>\n" +
                "</div></div></td></tr></table>\n" +
                "</body>\n" +
                "</html>";
    }
//    public String buildRegisterEmail(String name, String link) {
//        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
//                "\n" +
//                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
//                "\n" +
//                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;" +
//                "width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
//                "        \n" +
//                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;" +
//                "max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
//                "          <tbody><tr>\n" +
//                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
//                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" " +
//                "style=\"border-collapse:collapse\">\n" +
//                "                  <tbody><tr>\n" +
//                "                    <td style=\"padding-left:10px\">\n" +
//                "                  \n" +
//                "                    </td>\n" +
//                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;" +
//                "padding-left:10px\">\n" +
//                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;" +
//                "color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your " +
//                "email</span>\n" +
//                "                    </td>\n" +
//                "                  </tr>\n" +
//                "                </tbody></table>\n" +
//                "              </a>\n" +
//                "            </td>\n" +
//                "          </tr>\n" +
//                "        </tbody></table>\n" +
//                "        \n" +
//                "      </td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table>\n" +
//                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" " +
//                "cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;" +
//                "width:100%!important\" width=\"100%\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
//                "      <td>\n" +
//                "        \n" +
//                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" " +
//                "border=\"0\" style=\"border-collapse:collapse\">\n" +
//                "                  <tbody><tr>\n" +
//                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
//                "                  </tr>\n" +
//                "                </tbody></table>\n" +
//                "        \n" +
//                "      </td>\n" +
//                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table>\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" " +
//                "cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;" +
//                "width:100%!important\" width=\"100%\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td height=\"30\"><br></td>\n" +
//                "    </tr>\n" +
//                "    <tr>\n" +
//                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;" +
//                "max-width:560px\">\n" +
//                "        \n" +
//                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
//                "        \n" +
//                "      </td>\n" +
//                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//                "    </tr>\n" +
//                "    <tr>\n" +
//                "      <td height=\"30\"><br></td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
//                "\n" +
//                "</div></div>";
//    }
}
