package com.zerobase.cms.user.service.Customer;


import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.repository.CustomerBalanceHistoryRepository;
import com.zerobase.cms.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {
    // 고객의 잔액 변경 기능
    // 변경될 금액을 입력하면 그 금액이 변경
    // ex)admin 입금 1000 -> 1000
    // ex admin 출금 -500 -> 500
    // default = 0

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
    private final CustomerRepository customerRepository;

    @Transactional(dontRollbackOn = {CustomException.class})
    public CustomerBalanceHistory changeBalance(Long customId, ChangeBalanceForm form) throws CustomException {
        CustomerBalanceHistory customerBalanceHistory =
                customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customId)
                        .orElse(CustomerBalanceHistory.builder() // 히스토리가 없다는 건 가격이 0원, 모두 0으로 저장
                                .changeMoney(0)
                                .currentMoney(0)
                                .customer(customerRepository.findById(customId)
                                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)))
                                .build());

        //잔액 예외처리
        if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0){
            throw new CustomException(ErrorCode.NOT_ENOUGH_BALANCE);
        }

        //변경된 잔액을 다시 기록해주고
        customerBalanceHistory = CustomerBalanceHistory.builder()
                .changeMoney(form.getMoney())
                .currentMoney(customerBalanceHistory.getCurrentMoney() + form.getMoney())
                .fromMessage(form.getFrom())
                .description(form.getMessage())
                .customer(customerBalanceHistory.getCustomer())
                .build();

        System.out.println("@@@");
        System.out.println(form.getMoney());
        System.out.println(customerBalanceHistory.getChangeMoney());
        System.out.println(customerBalanceHistory.getCurrentMoney());
        customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());

        return customerBalanceHistoryRepository.save(customerBalanceHistory);
    }


}
