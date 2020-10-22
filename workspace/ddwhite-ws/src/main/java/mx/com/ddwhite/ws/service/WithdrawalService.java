package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.WithdrawalDto;
import mx.com.ddwhite.ws.model.Withdrawal;
import mx.com.ddwhite.ws.model.WithdrawalDetail;
import mx.com.ddwhite.ws.repository.WithdrawalDetailRepository;
import mx.com.ddwhite.ws.repository.WithdrawalRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class WithdrawalService {
	
	@Autowired
	private WithdrawalRepository withdrawalRepository;
	
	@Autowired
	private WithdrawalDetailRepository withdrawalDetailRepository;
	
	public Long save(Long sessionId, List<WithdrawalDto> denominations) {
		BigDecimal total = denominations.stream().map(d -> d.getDenominationValue().multiply(BigDecimal.valueOf(d.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
		final Withdrawal withdrawal = new Withdrawal();
		withdrawal.setAmmount(total);
		withdrawal.setSessionId(sessionId);
		withdrawalRepository.saveAndFlush(withdrawal);
		denominations.forEach( d -> {
			WithdrawalDetail wd = new WithdrawalDetail();
			wd.setWithdrawalId(withdrawal.getId());
			wd.setDenominationId(d.getDenominationId());
			wd.setQuantity(d.getQuantity());
			withdrawalDetailRepository.saveAndFlush(wd);
		} );
		return withdrawal.getId();
	}
	
	public String getLastDateWithdrawalBySession(Long sessionId, String sessionInData) {
		try {
			return withdrawalRepository.findTop1BySessionId(sessionId, Sort.by(Sort.Direction.DESC, "dateCreated")).getDateCreated();
		} catch (NullPointerException e) {
			return sessionInData;
		}
	}
	
	public List<Withdrawal> findWithdrawallsBySessionAndRange(Long sessionId, String startDate, String endDate) {
		return withdrawalRepository.findBySessionAndDates(sessionId, startDate, endDate);
	}
	
	public List<Withdrawal> findWithdrawallsByRange(String startDate, String endDate) {
		return withdrawalRepository.findByDates(startDate, endDate);
	}
	
	public List<Withdrawal> findWithdrawalCurrentSession(String sessionInDate) {
		return findWithdrawallsByRange(sessionInDate, GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME)
		);
	}

}
