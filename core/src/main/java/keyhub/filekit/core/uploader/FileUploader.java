package keyhub.filekit.core.uploader;

import keyhub.filekit.core.config.FileConfigMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileUploader {
	static FileUploader of(FileConfigMap configMap) {
		return switch (configMap.getType()) {
			case LOCAL -> LocalFileUploader.of(configMap);
			default -> LocalFileUploader.of(configMap);
		};
	}

	int upload(MultipartFile uploadFile, String path) throws IOException;

	String preparePath(MultipartFile file);

	String preparePath(String targetPath);

	Path read(String sourcePath) throws IOException;

	void write(Path source, String targetPath) throws IOException;

	void remove(String sourcePath) throws IOException;
}
