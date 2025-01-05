package keyhub.filekit.core;

import keyhub.filekit.core.config.FileConfigMap;
import keyhub.filekit.core.service.FilePath;
import keyhub.filekit.core.service.FilePathService;
import keyhub.filekit.core.uploader.FileUploader;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class SimpleFileStorage<ID> implements FileStorage<ID> {
	private final FileConfigMap configMap;
	private final FileUploader uploader;
	private final FilePathService<ID> pathService;

	public SimpleFileStorage(FileConfigMap configMap, FileUploader uploader, FilePathService<ID> pathService) {
		this.configMap = configMap;
		this.uploader = uploader;
		this.pathService = pathService;
	}

	static <ID> SimpleFileStorage<ID> of(FileConfigMap configMap, FilePathService<ID> pathService) {
		return new SimpleFileStorage<>(
			configMap,
			FileUploader.of(configMap),
			pathService
		);
	}

	@Override
	public FilePath<ID> upload(MultipartFile file) throws IOException {
		String path = uploader.preparePath(file);
		FilePath<ID> filePath = pathService.save(file, path);
		int result = uploader.upload(file, path);
		if (result < 1) {
			throw new FileUploadException("File upload failed");
		}
		return filePath;
	}

	@Override
	public Optional<? extends FilePath<ID>> find(ID id) {
		return pathService.findById(id);
	}

	@Override
	public FileConfigMap configMap() {
		return this.configMap;
	}

	@Override
	public Path read(String sourcePath) throws IOException {
		return this.uploader.read(sourcePath);
	}

	@Override
	public Optional<? extends FilePath<ID>> findByPath(String fromSourcePath) {
		return pathService.findByPath(fromSourcePath);
	}

	@Override
	public FilePath<ID> write(Path source, String targetPath) throws IOException {
		String path = uploader.preparePath(targetPath);
		FilePath<ID> filePath = pathService.save(source, path);
		this.uploader.write(source, targetPath);
		return filePath;
	}
}