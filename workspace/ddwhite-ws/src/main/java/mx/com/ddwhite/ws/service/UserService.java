package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.UserDto;
import mx.com.ddwhite.ws.dto.UserGrantDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Privilege;
import mx.com.ddwhite.ws.model.Role;
import mx.com.ddwhite.ws.model.RolePrivileges;
import mx.com.ddwhite.ws.model.User;
import mx.com.ddwhite.ws.repository.PrivilegeRepository;
import mx.com.ddwhite.ws.repository.RolePrivilegesRepository;
import mx.com.ddwhite.ws.repository.RoleRepository;
import mx.com.ddwhite.ws.repository.UserRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class UserService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private final String MODULE = User.class.getSimpleName();

	@Autowired
	private UserRepository repository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PrivilegeRepository privilegeRepository;
	@Autowired
	private RolePrivilegesRepository rolePrivilegeRepository;

	public Page<UserDto> findAll(Pageable pageable) {
		List<User> users = repository.findAll(pageable).getContent();
		users = users.stream().filter(user -> !user.getUsername().equals(GeneralConstants.USERNAME_ADMIN))
				.collect(Collectors.toList());
		final List<UserDto> usersDto = new ArrayList<>();
		users.forEach(user -> {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			userDto.setRoleName( findRole(user.getRoleId()).getName() );
			usersDto.add(userDto);
		});
		return new PageImpl<>(usersDto, pageable, repository.count());
	}

	public UserDto findById(Long id) {
		User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		userDto.setRoleName( findRole(user.getRoleId()).getName() );
		return userDto;
	}

	public UserDto create(UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		repository.saveAndFlush(user);
		userDto.setId(user.getId());
		return userDto;
	}

	public void update(UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		repository.findById(userDto.getId()).map(t -> {
			BeanUtils.copyProperties(userDto, t);
			t.setDateCreated(GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
			return repository.saveAndFlush(t);
		}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", userDto.getId()));
	}

	public void delete(Long id) {
		User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(user);
		repository.flush();
	}

	public UserGrantDto login(UserDto userDto) throws Exception {
		LOGGER.debug("Logig service for user: {} with pswd: ****{}", userDto.getUsername(), userDto.getPassword().length()>6 ? userDto.getPassword().substring(6) : "");
		User userFinded = repository.findByUsername(userDto.getUsername());
		if (userFinded != null) {
			if (userDto.getPassword().equals(userFinded.getPassword()))
				return getUserGranted(userFinded);
			else
				throw new Exception("La contraseña es incorrecta");
		}
		throw new Exception("El usuario no se encuentra registrado");
	}
	
	private UserGrantDto getUserGranted(User user) {
		UserGrantDto userGrantedDto = new UserGrantDto();
		BeanUtils.copyProperties(user, userGrantedDto);
		Role role = findRole(user.getRoleId());
		userGrantedDto.setRole( role.getName() );
		userGrantedDto.setPrivileges(getPrivileges(rolePrivilegeRepository.findByRole(role.getId())));
		return userGrantedDto;
	}
	
	private Role findRole(Long roleId) {
		return roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class.getSimpleName(), "id", roleId));
	}
	
	private Privilege findPrivilege(Long privilegeId) {
		return privilegeRepository.findById(privilegeId).orElseThrow(() -> new ResourceNotFoundException(Privilege.class.getSimpleName(), "id", privilegeId));
	}
	
	private List<String> getPrivileges(List<RolePrivileges> rolePrivileges){
		final List<String> privileges = new ArrayList<>();
		rolePrivileges.forEach( rp -> {
			Privilege privilege = findPrivilege(rp.getPrivilegeId());
			privileges.add(privilege.getKey());
		});
		return privileges;
	}

}
