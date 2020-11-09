package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.SessionDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Session;
import mx.com.ddwhite.ws.repository.SessionRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class SessionService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);
	
	private final String MODULE = Session.class.getSimpleName();
	
	@Autowired
	private SessionRepository repository;
	
	@Autowired
	private UserService userService;
	
	public List<SessionDto> findByRange(Date startDate, Date endDate) {
		final List<SessionDto> sessionsDto = new ArrayList<>();
		String strStartDate = GenericUtils.dateToString(startDate, GeneralConstants.FORMAT_DATE_TIME);
		endDate = GenericUtils.plusDay(endDate, 1);
		String strEndDate = GenericUtils.dateToString(endDate, GeneralConstants.FORMAT_DATE_TIME);
		List<Session> sessions = repository.findByRange(strStartDate, strEndDate);
		sessions.forEach( session -> sessionsDto.add(getSessionDto(session)));
		return sessionsDto;
	}
	
	public SessionDto findById(Long id) throws ResourceNotFoundException {
		return getSessionDto(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id)));
	}
	
	public SessionDto findSessionByUser(Long userId) {
		SessionDto sessionDto = new SessionDto();
		BeanUtils.copyProperties(repository.findSessionByUserInDate(userId, GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME)), sessionDto);
		return sessionDto;
	}
	
	public SessionDto findCurrentSession(Long userId) {
		SessionDto sessionDto = new SessionDto();
		Session session = repository.findCurrentSession(userId, GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
		if(session != null) {
			BeanUtils.copyProperties(session, sessionDto);
		}
		return sessionDto;
	}
	
	public SessionDto findByUserIdAndRange(Long userId, String startDate, String endDate) {
		SessionDto sessionDto = new SessionDto();
		List<Session> sessions = repository.findByUserIdAndRange(userId, startDate, endDate);
		if(!sessions.isEmpty()) {
			BeanUtils.copyProperties(sessions.get(0), sessionDto);
		}
		return sessionDto;
	}
	
	public SessionDto create(SessionDto sessionDto) {
		Session session = new Session();
		BeanUtils.copyProperties(sessionDto, session);
		repository.saveAndFlush(session);
		sessionDto.setId(session.getId());
		return sessionDto;
	}
	
	public String updateCloseSession(Long id, BigDecimal finalAmount) {
		String outDate = GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME);
		repository.updateCloseSession(id, outDate, finalAmount);
		return outDate;
	}
	
	private SessionDto getSessionDto(Session session) {
		SessionDto sessionDto = new SessionDto();
		BeanUtils.copyProperties(session, sessionDto);
		sessionDto.setUserFullname(userService.findById(sessionDto.getUserId()).getFullName());
		return sessionDto;
	}
	
	public void updateAmounts(SessionDto sessionDto) {
		LOGGER.debug("Updating session amounts, id: %d, initial: %s, final: %s\n", sessionDto.getId(), sessionDto.getInitialAmount(), sessionDto.getFinalAmount());
		repository.updateAmounts(sessionDto.getId(), sessionDto.getInitialAmount(), sessionDto.getFinalAmount());
	}

}
