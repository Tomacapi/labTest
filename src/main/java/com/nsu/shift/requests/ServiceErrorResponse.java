package com.nsu.shift.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceErrorResponse {
    private int errorCode;
    private String errorMessage;}
