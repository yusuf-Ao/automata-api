package io.aycodes.automataapi.common.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CustomResponse {

    private LocalDateTime       timeStamp;
    private String              message;
    private HttpStatus          status;
    private int                 statusCode;
    private Object              data;
    private boolean             success;

    public CustomResponse() {
        timeStamp   =   LocalDateTime.now();
        data        =   new Object();
    }

}
