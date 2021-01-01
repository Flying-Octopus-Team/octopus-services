package pl.flyingoctopus.trello;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class CoolTrello {

    public String coolify(String text) {
        return "Real cool " + text;
    }

}
