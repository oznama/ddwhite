package mx.com.ddwhite.ws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.com.ddwhite.ws.dto.ProductDto;

@SpringBootTest
@AutoConfigureMockMvc
public class MsProductApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	private final String CONTEXT = "/product";

	@Test
	void bulkSave() throws JsonProcessingException, Exception {
		String uri = "/saveBulk";
		List<ProductDto> productsDto = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			ProductDto productDto = new ProductDto();
			String num = i < 10 ? "00" + i : (i < 100 ? "0" + i : "" + i);
			productDto.setNameLarge("Producto con nombre " + num);
			productDto.setNameShort("PRODUCT" + num);
			productDto.setSku("0000000" + num);
			productDto.setDescription("Descripcion del producto " + productDto.getNameLarge());
			productDto.setPercentage(BigDecimal.valueOf(1 + (i * 2 / 100)));
			productDto.setGroup(Long.valueOf(i % 5 == 0 ? 6 : (i % 3 == 0 ? 5 : 4)));
			productDto.setUserId(1L);
			productsDto.add(productDto);
		}
		MvcResult mvcResult = mockMvc.perform(
				post(CONTEXT + uri).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productsDto)))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

}
