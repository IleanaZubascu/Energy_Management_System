package com.example.chat.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String sender;
    private String receiver;
    private String message;
    private Boolean typing;
    private Boolean seen;

}