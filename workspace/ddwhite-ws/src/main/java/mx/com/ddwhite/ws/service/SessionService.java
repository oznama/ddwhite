package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	private final String MODULE = Session.class.getSimpleName();
	
	@Autowired
	private SessionRepository repository;
	
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
	
	public SessionDto findSessionById(Long userId) {
		SessionDto sessionDto = new SessionDto();
		BeanUtils.copyProperties(repository.findSessionByIdAndDate(userId, GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME)), sessionDto);
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
	
	/*
	 * TODO change to limit
	 */
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
	
	public String updateCloseSession(Long id) {
		String outDate = GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME);
		repository.updateCloseSession(id, outDate);
		return outDate;
	}
	
	private SessionDto getSessionDto(Session session) {
		SessionDto sessionDto = new SessionDto();
		BeanUtils.copyProperties(sessionDto, session);
		return sessionDto;
	}

}
