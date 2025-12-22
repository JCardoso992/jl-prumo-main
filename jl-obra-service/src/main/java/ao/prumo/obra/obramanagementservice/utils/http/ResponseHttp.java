package ao.prumo.obra.obramanagementservice.utils.http;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Setter
@Getter
@ToString
public class ResponseHttp {
    public  String mensagen;
    public HttpStatus status;
    public Integer code;
    public Object data;
    public Date timeStamp = new Date();
}
