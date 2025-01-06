package keyhub.filekit.core.uploader;

import keyhub.filekit.core.name.NameGenerator;
import keyhub.filekit.core.name.UuidV7NameGenerator;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileUploader implements FileUploader {
	private final String directoryPath;
	private final NameGenerator nameGenerator;

	private LocalFileUploader(String directoryPath, NameGenerator nameGenerator) {
		this.directoryPath = directoryPath;
		this.nameGenerator = nameGenerator;
	}

	public static LocalFileUploader of(LocalFileUploaderConfigMap configMap) {
		if (configMap.nameGenerator() == null) {
			return new LocalFileUploader(configMap.directoryPath(), new UuidV7NameGenerator());
		}
		return new LocalFileUploader(configMap.directoryPath(), configMap.nameGenerator());
	}

	@Override
	public int upload(MultipartFile uploadFile, String path) throws IOException {
		if (uploadFile.isEmpty()) {
			return 0;
		}
		if (!path.contains(directoryPath)) {
			throw new FileUploadException("Not allowed path");
		}
		File resultFile = new File(path);
		File parentDir = resultFile.getParentFile();
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		uploadFile.transferTo(resultFile);
		return 1;
	}

	@Override
	public String preparePath(MultipartFile file) {
		return directoryPath + "/" + nameGenerator.generateName();
	}

	@Override
	public String preparePath(String targetPath) {
		return directoryPath + "/" + targetPath;
	}

	@Override
	public Path read(String sourcePath) throws IOException {
		Path path = Paths.get(sourcePath);
		if (!Files.exists(path)) {
			throw new IOException("File not found: " + sourcePath);
		}
		return path;
	}

	@Override
	public void write(Path source, String targetPath) throws IOException {
		Path target = Paths.get(targetPath);
		if (!Files.exists(target.getParent())) {
			Files.createDirectories(target.getParent());
		}
		Files.copy(source, target);
	}

	@Override
	public void remove(String sourcePath) throws IOException {
		Path path = Paths.get(sourcePath);
		Files.deleteIfExists(path);
	}
}
