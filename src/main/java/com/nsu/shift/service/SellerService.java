package com.nsu.shift.service;

import com.nsu.shift.exception.ErrorCode;
import com.nsu.shift.exception.ServiceException;
import com.nsu.shift.model.Seller;
import com.nsu.shift.repository.SellerRepository;
import com.nsu.shift.requests.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Validated
@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Seller createSeller(CreateSellerRequest createSellerRequest) {
        if (sellerRepository.existsByContactInfo(createSellerRequest.getContactInfo())) {
            throw new ServiceException(ErrorCode.CONTACT_INFO_IS_TAKEN);
        }
        var seller = new Seller(createSellerRequest.getName(), createSellerRequest.getContactInfo());

        return sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSeller(UUID id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND));
    }

    public Seller editSeller(EditSellerRequest editSellerRequest) {
        var seller = getSeller(editSellerRequest.getId());
        if (!seller.getContactInfo().equals(editSellerRequest.getContactInfo())
                && sellerRepository.existsByContactInfo(editSellerRequest.getContactInfo())) {
            throw new ServiceException(ErrorCode.CONTACT_INFO_IS_TAKEN);
        }
        seller.setName(editSellerRequest.getName());
        seller.setContactInfo(editSellerRequest.getContactInfo());

        return sellerRepository.save(seller);
    }

    public void deleteSeller(UUID id) {
        if (!sellerRepository.existsById(id)) {
            throw new ServiceException(ErrorCode.OBJECT_NOT_FOUND);
        }
        sellerRepository.delete(getSeller(id));
    }

    public List<Seller> getSellersWithSalesLessThan(WorstByDayRequest dayRequest) {
        var startDay = dayRequest.getDay();
        var endDay = startDay.plusDays(1);
        return getSellersWithSalesLessThan(startDay, endDay, dayRequest.getAmount());
    }

    public List<Seller> getSellersWithSalesLessThan(WorstByMonthRequest monthRequest) {
        var startMonth = monthRequest.getMonth().withDayOfMonth(1);
        var endMonth = startMonth.plusMonths(1);
        return getSellersWithSalesLessThan(startMonth, endMonth, monthRequest.getAmount());
    }

    public List<Seller> getSellersWithSalesLessThan(WorstByYearRequest yearRequest) {
        var startYear = yearRequest.getYear().withDayOfYear(1);
        var endYear = startYear.plusYears(1);
        return getSellersWithSalesLessThan(startYear, endYear, yearRequest.getAmount());
    }

    public List<Seller> getSellersWithSalesLessThan(@Valid WorstByQuarterRequest quarterRequest) {
        var year = quarterRequest.getYear().withDayOfYear(1);

        var quarter = quarterRequest.getQuarter() - 1;
        var startQuarter = year.plusMonths(quarter * 3L);
        var endQuarter = startQuarter.plusMonths(3L);

        return getSellersWithSalesLessThan(startQuarter, endQuarter, quarterRequest.getAmount());
    }

    public List<Seller> getSellersWithSalesLessThan(LocalDate firstDate, LocalDate secondDate, int amount) {
        if (!secondDate.isAfter(firstDate)) {
            throw new ServiceException(ErrorCode.INVALID_TIME_PERIOD);
        }
        return sellerRepository.getSellersWithSalesAmountLessThen(
                firstDate.atStartOfDay(), secondDate.atStartOfDay(), amount);
    }

    public Seller getBestSeller(BestBetweenDaysRequest request) {
        if (!request.getEndDay().isAfter(request.getBeginDay())) {
            throw new ServiceException(ErrorCode.INVALID_TIME_PERIOD);
        }
        return sellerRepository.findFirstSellerWithBestSalesAmount(
                request.getBeginDay().atStartOfDay(), request.getEndDay().atStartOfDay())
                .orElseThrow();
    }
}