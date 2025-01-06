package keyhub.filekit.core;

import keyhub.filekit.core.uploader.FileUploaderConfigMap;
import keyhub.filekit.core.service.FilePath;
import keyhub.filekit.core.service.FilePathService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface FileStorage<ID> {
	static <ID> FileStorage<ID> of(
		FileUploaderConfigMap configMap,
		FilePathService<ID> pathService
	) {
		return SimpleFileStorage.of(configMap, pathService);
	}

	FilePath<ID> upload(MultipartFile file) throws IOException;

	Optional<? extends FilePath<ID>> find(ID id);

	FileUploaderConfigMap configMap();

	Path read(String sourcePath) throws IOException;

	FilePath<ID> write(Path source, String targetPath) throws IOException;

	Optional<? extends FilePath<ID>> findByPath(String sourcePath);

	FilePath<ID> remove(String fromSourcePath) throws IOException;
}
