package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.dto.PurchaseReasignDto;
import mx.com.ddwhite.ws.model.PurchaseReasign;
import mx.com.ddwhite.ws.repository.PurchaseReasignRepository;

@Service
public class PurchaseReasingService {
	
	@Autowired
	private PurchaseReasignRepository purchaseReasignRep;
	
	public PurchaseReasignDto create(PurchaseReasignDto purchaseReasignDto) {
		PurchaseReasign purchaseReasign = new PurchaseReasign();
		BeanUtils.copyProperties(purchaseReasignDto, purchaseReasign);
		purchaseReasignRep.saveAndFlush(purchaseReasign);
		purchaseReasign.setId(purchaseReasign.getId());
		return purchaseReasignDto;
	}
	
	public List<PurchaseReasignDto> findByOrigin(Long purchaseOrigin){
		final List<PurchaseReasignDto> purchasesReasingDto = new ArrayList<>();
		List<PurchaseReasign> purchasesReasing = purchaseReasignRep.findByOrigin(purchaseOrigin);
		purchasesReasing.forEach( pr -> purchasesReasingDto.add(getPurchaseReasignDto(pr)));
		return purchasesReasingDto;
	}
	
	public List<PurchaseReasignDto> findByDestiny(Long purchaseDestiny){
		final List<PurchaseReasignDto> purchasesReasingDto = new ArrayList<>();
		List<PurchaseReasign> purchasesReasing = purchaseReasignRep.findByDestiny(purchaseDestiny);
		purchasesReasing.forEach( pr -> purchasesReasingDto.add(getPurchaseReasignDto(pr)));
		return purchasesReasingDto;
	}
	
	private PurchaseReasignDto getPurchaseReasignDto(PurchaseReasign purchaseReasign) {
		PurchaseReasignDto purchaseReasignDto = new PurchaseReasignDto();
		BeanUtils.copyProperties(purchaseReasign, purchaseReasignDto);
		return purchaseReasignDto;
	}

}
