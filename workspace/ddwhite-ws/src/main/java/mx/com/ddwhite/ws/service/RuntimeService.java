package mx.com.ddwhite.ws.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;

@Service
public class RuntimeService {

	@Autowired
	private CatalogService catalogService;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	private final String MYSQLDUMP = "\\bin\\mysqldump -u %s -p%s ddwhite";
	private final String MYSQL_RESTORE = "\\bin\\mysql -u %s -p%s ddwhite";

	public void mysql(InputStream input) throws Exception {
		if (!mysqldumpRestore(input, dbUsername, dbPassword))
			throw new Exception("Error restoring database");
	}

	public String mysqlDump() {
		try {
			return mysqldumpBackUp(dbUsername, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String mysqldumpBackUp(String user, String pswd) throws IOException, InterruptedException {
		try {
			String command = catalogService.findByName(GeneralConstants.CATALOG_MYSQL_PATH).getDescription().trim();
			command = String.format(command + MYSQLDUMP, user, pswd);
			Process p = Runtime.getRuntime().exec(command);
			InputStream is = p.getInputStream();
			return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean mysqldumpRestore(InputStream in, String user, String pswd)
			throws IOException, InterruptedException {
		try {
			String command = catalogService.findByName(GeneralConstants.CATALOG_MYSQL_PATH).getDescription().trim();
			command = String.format(command + MYSQL_RESTORE, user, pswd);
			Process p = Runtime.getRuntime().exec(command);
			OutputStream os = p.getOutputStream();
			byte[] buffer = new byte[1000];
			int leido = in.read(buffer);
			while (leido > 0) {
				os.write(buffer, 0, leido);
				leido = in.read(buffer);
			}
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
