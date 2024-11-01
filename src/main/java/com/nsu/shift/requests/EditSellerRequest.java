package com.nsu.shift.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EditSellerRequest {

    private UUID id;

    private String name;

    private String contactInfo;
}
