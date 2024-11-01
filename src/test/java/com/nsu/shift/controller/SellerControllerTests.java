package com.nsu.shift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsu.shift.model.Seller;
import com.nsu.shift.requests.CreateSellerRequest;
import com.nsu.shift.requests.EditSellerRequest;
import com.nsu.shift.requests.WorstByDayRequest;
import com.nsu.shift.service.SellerService;
import com.nsu.shift.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class SellerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SellerService sellerService;

    @MockBean
    private TransactionService transactionService;

    private Seller seller1;
    private Seller seller2;

    @BeforeEach
    public void setup() {
        seller1 = new Seller();
        seller1.setId(UUID.randomUUID());
        seller1.setName("name1");
        seller1.setContactInfo("123");
        seller1.setRegistrationDate(LocalDateTime.now());

        seller2 = new Seller();
        seller2.setId(UUID.randomUUID());
        seller2.setName("name2");
        seller2.setContactInfo("789");
        seller2.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    public void createSeller() throws Exception {
        var request = new CreateSellerRequest(seller1.getName(), seller1.getContactInfo());
        given(sellerService.createSeller(eq(request))).willReturn(seller1);

        var response = mockMvc.perform(post("/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(seller1.getName())))
                .andExpect(jsonPath("$.contactInfo", is(seller1.getContactInfo())));
    }

    @Test
    public void getAllSellers() throws Exception {
        var sellerList = List.of(seller1, seller2);
        given(sellerService.getAllSellers()).willReturn(sellerList);

        var response = mockMvc.perform(get("/seller/all"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(sellerList.size())));
    }

    @Test
    public void getSeller() throws Exception {
        given(sellerService.getSeller(any(UUID.class))).willReturn(seller1);

        var response = mockMvc.perform(get("/seller/{id}", seller1.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(seller1.getName())))
                .andExpect(jsonPath("$.contactInfo", is(seller1.getContactInfo())));
    }

    @Test
    public void editSeller() throws Exception {
        var request = new EditSellerRequest(seller1.getId(), seller1.getName(), seller1.getContactInfo());
        var updatedSeller = new Seller(seller1.getId(), "newName", "new123", seller1.getRegistrationDate());
        given(sellerService.editSeller(eq(request))).willReturn(updatedSeller);

        var response = mockMvc.perform(patch("/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(seller1.getId().toString())))
                .andExpect(jsonPath("$.name", is(updatedSeller.getName())))
                .andExpect(jsonPath("$.contactInfo", is(updatedSeller.getContactInfo())));
    }

    @Test
    public void getWorstByDay() throws Exception {
        var request = new WorstByDayRequest(LocalDate.parse("2024-10-10"), 100);
        var sellerList = List.of(seller1, seller2);
        given(sellerService.getSellersWithSalesLessThan(request)).willReturn(sellerList);

        var response = mockMvc.perform(post("/seller/worst/by_day")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(sellerList.size())));
    }

}
