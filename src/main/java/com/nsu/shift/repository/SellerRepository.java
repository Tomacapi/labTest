package com.nsu.shift.repository;

import com.nsu.shift.model.Seller;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    boolean existsByContactInfo(String contactInfo);

    @Query("select s " +
            "from Transaction t join t.seller s " +
            "where t.transactionDate between :beginDate and :endDate " +
            "group by s.id " +
            "having sum(t.amount) <= :amount")
    List<Seller> getSellersWithSalesAmountLessThen(LocalDateTime beginDate, LocalDateTime endDate, int amount);

    @Query("select s " +
            "from Transaction t join t.seller s " +
            "where t.transactionDate between :beginDate and :endDate " +
            "group by s.id " +
            "order by sum(t.amount) desc")
    List<Seller> findAllSellersWithSalesHigherThan(LocalDateTime beginDate, LocalDateTime endDate, Pageable pageable);

    default Optional<Seller> findFirstSellerWithBestSalesAmount(LocalDateTime beginDate, LocalDateTime endDate) {
        return findAllSellersWithSalesHigherThan(beginDate, endDate, PageRequest.of(0,1))
                .stream().findFirst();
    }
}
