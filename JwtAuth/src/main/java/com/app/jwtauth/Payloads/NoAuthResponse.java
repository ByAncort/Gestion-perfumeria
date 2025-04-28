package com.app.jwtauth.Payloads;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoAuthResponse {
    private int errorCode;
    private String Mensaje;

}
