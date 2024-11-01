package com.nsu.shift.controller;

import com.nsu.shift.model.Seller;
import com.nsu.shift.repository.SellerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SellerRepositoryTests {
    @Autowired
    private SellerRepository sellerRepository;

    @AfterEach
    void tearDown() {
        sellerRepository.deleteAll();
    }

    @Test
    public void sellerExistByContactInfo() {
        var contactInfo = "123";
        var seller = new Seller("name", contactInfo);

        sellerRepository.save(seller);
        var res = sellerRepository.existsByContactInfo(contactInfo);

        assertTrue(res);
    }
}
