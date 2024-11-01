package com.nsu.shift.service;

import com.nsu.shift.exception.ErrorCode;
import com.nsu.shift.exception.ServiceException;
import com.nsu.shift.model.Seller;
import com.nsu.shift.repository.SellerRepository;
import com.nsu.shift.requests.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SellerServiceTests {
    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerService sellerService;

    private Seller seller1;
    private Seller seller2;

    @BeforeEach
    public void setup() {
        seller1 = new Seller();
        seller1.setId(UUID.randomUUID());
        seller1.setName("name1");
        seller1.setContactInfo("123");
        seller1.setRegistrationDate(LocalDate.parse("2023-02-10").atStartOfDay());

        seller2 = new Seller();
        seller2.setId(UUID.randomUUID());
        seller2.setName("name2");
        seller2.setContactInfo("789");
        seller2.setRegistrationDate(LocalDate.parse("2023-02-10").atStartOfDay());
    }

    @Test
    public void getBestSeller() {
        var beginDate = LocalDate.parse("2024-02-10");
        var endDate = LocalDate.parse("2024-02-15");
        var request = new BestBetweenDaysRequest(beginDate, endDate);
        var expectedSeller = new Seller();

        given(sellerRepository.findFirstSellerWithBestSalesAmount(
                eq(beginDate.atStartOfDay()),
                eq(endDate.atStartOfDay()))).willReturn(Optional.of(expectedSeller));

        var actualSeller = sellerService.getBestSeller(request);

        assertEquals(expectedSeller, actualSeller);
    }

    @Test
    public void getWorst_BetweenDates() {
        var beginDate = LocalDate.parse("2024-02-10");
        var endDate = LocalDate.parse("2024-02-15");
        var amount = 100;
        var expectedSellers = List.of(new Seller());

        given(sellerRepository.getSellersWithSalesAmountLessThen(
                eq(beginDate.atStartOfDay()),
                eq(endDate.atStartOfDay()),
                eq(amount))).willReturn(expectedSellers);

        var actualSeller = sellerService.getSellersWithSalesLessThan(beginDate, endDate, amount);

        assertIterableEquals(expectedSellers, actualSeller);
    }

    @Test
    public void getWorst_ByDay() {
        var date = LocalDate.parse("2024-02-10");
        var amount = 100;
        var request = new WorstByDayRequest(date, amount);

        var expectedSellers = List.of(new Seller());

        given(sellerRepository.getSellersWithSalesAmountLessThen(
                eq(date.atStartOfDay()),
                eq(date.plusDays(1).atStartOfDay()),
                eq(amount))).willReturn(expectedSellers);

        var actualSeller = sellerService.getSellersWithSalesLessThan(request);

        assertIterableEquals(expectedSellers, actualSeller);
    }

    @Test
    public void getWorst_ByMonth() {
        var date = LocalDate.parse("2024-02-10");
        var amount = 100;

        var request = new WorstByMonthRequest(date, amount);

        var expectedSellers = List.of(new Seller());

        var repositoryExpectedStartDate = LocalDate.parse("2024-02-01").atStartOfDay();
        var repositoryExpectedEndDate = LocalDate.parse("2024-03-01").atStartOfDay();
        given(sellerRepository.getSellersWithSalesAmountLessThen(
                eq(repositoryExpectedStartDate),
                eq(repositoryExpectedEndDate),
                eq(amount))).willReturn(expectedSellers);

        var actualSeller = sellerService.getSellersWithSalesLessThan(request);

        assertIterableEquals(expectedSellers, actualSeller);
    }

    @Test
    public void getWorst_ByYear() {
        var date = LocalDate.parse("2024-02-10");
        var amount = 100;

        var request = new WorstByYearRequest(date, amount);

        var expectedSellers = List.of(new Seller());

        var repositoryExpectedStartDate = LocalDate.parse("2024-01-01").atStartOfDay();
        var repositoryExpectedEndDate = LocalDate.parse("2025-01-01").atStartOfDay();
        given(sellerRepository.getSellersWithSalesAmountLessThen(
                eq(repositoryExpectedStartDate),
                eq(repositoryExpectedEndDate),
                eq(amount))).willReturn(expectedSellers);

        var actualSeller = sellerService.getSellersWithSalesLessThan(request);

        assertIterableEquals(expectedSellers, actualSeller);
    }

    @Test
    public void getWorst_ByQuarter() {
        var date = LocalDate.parse("2024-02-10");
        var quarter = 2;
        var amount = 100;

        var request = new WorstByQuarterRequest(date, quarter, amount);

        var expectedSellers = List.of(new Seller());

        var repositoryExpectedStartDate = LocalDate.parse("2024-04-01").atStartOfDay();
        var repositoryExpectedEndDate = LocalDate.parse("2024-07-01").atStartOfDay();
        given(sellerRepository.getSellersWithSalesAmountLessThen(
                eq(repositoryExpectedStartDate),
                eq(repositoryExpectedEndDate),
                eq(amount))).willReturn(expectedSellers);

        var actualSeller = sellerService.getSellersWithSalesLessThan(request);

        assertIterableEquals(expectedSellers, actualSeller);
    }

    @Test
    public void createSeller_success() {
        var request = new CreateSellerRequest(seller1.getName(), seller1.getContactInfo());
        given(sellerRepository.existsByContactInfo(seller1.getContactInfo())).willReturn(false);
        given(sellerRepository.save(any())).willReturn(seller1);

        var actualSeller = sellerService.createSeller(request);

        assertNotNull(seller1.getId());
        assertNotNull(seller1.getRegistrationDate());
        assertEquals(seller1.getName(), actualSeller.getName());
        assertEquals(seller1.getContactInfo(), actualSeller.getContactInfo());
    }

    @Test
    public void createSeller_fail() {
        var request = new CreateSellerRequest(seller1.getName(), seller1.getContactInfo());
        given(sellerRepository.existsByContactInfo(seller1.getContactInfo())).willReturn(true);

        var res = assertThrows(ServiceException.class, () -> sellerService.createSeller(request));
        assertEquals(res.getErrorCode(), ErrorCode.CONTACT_INFO_IS_TAKEN);
    }

    @Test
    public void getAllSellers() {
        var sellerList = List.of(seller1, seller2);
        given(sellerRepository.findAll()).willReturn(sellerList);

        var actualList = sellerService.getAllSellers();

        assertIterableEquals(sellerList, actualList);
    }

    @Test
    public void editUser_success() {
        var newName = "newName";
        var newContactInfo = "new123";
        var request = new EditSellerRequest(seller1.getId(), newName, newContactInfo);
        var updatedSeller = new Seller(seller1.getId(), newName, newContactInfo, seller1.getRegistrationDate());

        given(sellerRepository.existsByContactInfo(updatedSeller.getContactInfo())).willReturn(false);
        given(sellerRepository.save(any())).willReturn(updatedSeller);
        given(sellerRepository.findById(seller1.getId())).willReturn(Optional.of(seller1));

        var actualSeller = sellerService.editSeller(request);

        assertEquals(updatedSeller, actualSeller);
    }

    @Test
    public void editUser_fail() {
        var newName = "newName";
        var newContactInfo = "new123";
        var request = new EditSellerRequest(seller1.getId(), newName, newContactInfo);
        var updatedSeller = new Seller(seller1.getId(), newName, newContactInfo, seller1.getRegistrationDate());

        given(sellerRepository.existsByContactInfo(updatedSeller.getContactInfo())).willReturn(true);
        given(sellerRepository.findById(seller1.getId())).willReturn(Optional.of(seller1));

        var res = assertThrows(ServiceException.class, () -> sellerService.editSeller(request));
        assertEquals(res.getErrorCode(), ErrorCode.CONTACT_INFO_IS_TAKEN);
    }

    @Test
    public void getSeller_success() {
        given(sellerRepository.findById(seller1.getId())).willReturn(Optional.of(seller1));

        var actualSeller = sellerService.getSeller(seller1.getId());

        assertEquals(seller1, actualSeller);
    }
}
