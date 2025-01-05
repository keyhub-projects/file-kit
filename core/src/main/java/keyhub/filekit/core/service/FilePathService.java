package keyhub.filekit.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

public interface FilePathService<ID> {

	FilePath<ID> save(MultipartFile file, String path);
	FilePath<ID> save(Path source, String targetPath);

	Optional<? extends FilePath<ID>> findById(ID hashId);

	Optional<? extends FilePath<ID>> findByPath(String fromSourcePath);
}
