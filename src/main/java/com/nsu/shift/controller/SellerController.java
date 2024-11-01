package com.nsu.shift.controller;

import com.nsu.shift.model.Seller;
import com.nsu.shift.requests.*;
import com.nsu.shift.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @PostMapping
    public Seller createSeller(@RequestBody CreateSellerRequest createSellerRequest) {
        return sellerService.createSeller(createSellerRequest);
    }

    @GetMapping("/all")
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    public Seller getSeller(@PathVariable UUID id) {
        return sellerService.getSeller(id);
    }

    @PatchMapping
    public Seller editSeller(@RequestBody EditSellerRequest sellerEditRequest) {
        return sellerService.editSeller(sellerEditRequest);
    }

    @DeleteMapping("{id}")
    public void deleteSeller(@PathVariable UUID id) {
        sellerService.deleteSeller(id);
    }

    @PostMapping("/worst/between_dates")
    public List<Seller> getSellersWithSalesLessThan(@RequestBody WorstBetweenDaysRequest request) {
        return sellerService.getSellersWithSalesLessThan(request.getBeginDay(), request.getEndDay(), request.getAmount());
    }

    @PostMapping("/worst/by_day")
    public List<Seller> getSellersWithSalesLessThan(@RequestBody WorstByDayRequest request) {
        return sellerService.getSellersWithSalesLessThan(request);
    }

    @PostMapping("/worst/by_month")
    public List<Seller> getSellersWithSalesLessThan(@RequestBody WorstByMonthRequest request) {
        return sellerService.getSellersWithSalesLessThan(request);
    }

    @PostMapping("/worst/by_quarter")
    public List<Seller> getSellersWithSalesLessThan(@Valid @RequestBody WorstByQuarterRequest request) {
        return sellerService.getSellersWithSalesLessThan(request);
    }

    @PostMapping("/worst/by_year")
    public List<Seller> getSellersWithSalesLessThan(@RequestBody WorstByYearRequest request) {
        return sellerService.getSellersWithSalesLessThan(request);
    }

    @PostMapping("/best/between_dates")
    public Seller getBestSeller(@RequestBody BestBetweenDaysRequest request) {
        return sellerService.getBestSeller(request);
    }
}
