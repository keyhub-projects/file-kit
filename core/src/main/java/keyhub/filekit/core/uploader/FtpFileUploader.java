package keyhub.filekit.core.uploader;

import keyhub.filekit.core.name.NameFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public class FtpFileUploader implements FileUploader {
	// todo

	private final String url;
	private final String port;
	private final String username;
	private final String password;
	private final NameFactory nameFactory;

	public FtpFileUploader(String url, String port, String username, String password, NameFactory nameFactory) {
		this.url = url;
		this.port = port;
		this.username = username;
		this.password = password;
		this.nameFactory = nameFactory;
	}

	@Override
	public int upload(MultipartFile uploadFile, String path) throws IOException {
		return 0;
	}

	@Override
	public String preparePath(MultipartFile file) {
		return null;
	}

	@Override
	public String preparePath(String targetPath) {
		return null;
	}

	@Override
	public Path read(String sourcePath) throws IOException {
		return null;
	}

	@Override
	public void write(Path source, String targetPath) throws IOException {

	}
}
