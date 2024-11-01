package com.nsu.shift.requests;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CreateSellerRequest {

    private String name;

    private String contactInfo;
}
