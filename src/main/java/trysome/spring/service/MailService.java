package trysome.spring.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MailService {
    private ZoneId zoneId = ZoneId.systemDefault();
    
    public void setZoneId(ZoneId zoneId){
        this.zoneId = zoneId;
    }

    public String getTime(){
        return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public void sendLonginMail(User user){
        System.err.println(String.format("Hi, %s! Your are logged in at %s",user.getName(), getTime()));
    }
    
    public void sendRegistrationMail(User user){
        System.err.println(String.format("Welcome, %s ",user.getName()));
    }
}
