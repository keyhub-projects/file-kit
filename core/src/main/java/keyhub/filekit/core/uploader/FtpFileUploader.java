package keyhub.filekit.core.uploader;

import keyhub.filekit.core.name.NameGenerator;
import keyhub.filekit.core.name.UuidV7NameGenerator;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static keyhub.filekit.core.annotation.FtpConnectionAspect.ftpClient;

public class FtpFileUploader implements FileUploader {
	private final String directoryPath;
	private final NameGenerator nameGenerator;

	private FtpFileUploader(String directoryPath, NameGenerator nameGenerator) {
        this.directoryPath = directoryPath;
        this.nameGenerator = nameGenerator;
	}

	public static FtpFileUploader of(FtpFileUploaderConfigMap configMap) {
		if (configMap.nameGenerator() == null) {
			return new FtpFileUploader(configMap.directoryPath(), new UuidV7NameGenerator());
		}
		return new FtpFileUploader(configMap.directoryPath(), configMap.nameGenerator());
	}

	@Override
	public int upload(MultipartFile uploadFile, String path) throws IOException {
		return upload(ftpClient(), uploadFile, path);
	}
	public int upload(FTPClient ftpClient, MultipartFile uploadFile, String path) throws IOException {
		if (uploadFile.isEmpty()) {
			return 0;
		}
		ftpClient.storeFile(path, uploadFile.getInputStream());
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
		return read(ftpClient(), sourcePath);
	}
	public Path read(FTPClient ftpClient, String sourcePath) throws IOException {
		Path tempFilePath = Files.createTempFile("ftp-temp-", ".dat");
		try (OutputStream outputStream = Files.newOutputStream(tempFilePath)) {
			boolean success = ftpClient.retrieveFile(sourcePath, outputStream);
			if (!success) {
				throw new IOException("Failed to retrieve file: " + sourcePath);
			}
		}
		return tempFilePath;
	}

	@Override
	public void write(Path source, String targetPath) throws IOException {
		write(ftpClient(), source, targetPath);
	}
	public void write(FTPClient ftpClient, Path source, String targetPath) throws IOException {
	 	InputStream inputStream = Files.newInputStream(source);
		boolean success = ftpClient.storeFile(targetPath, inputStream);
		if (!success) {
			throw new IOException("Failed to store file: " + targetPath);
		}
	}

	@Override
	public void remove(String sourcePath) throws IOException {
		remove(ftpClient(), sourcePath);
	}
	public void remove(FTPClient ftpClient, String sourcePath) throws IOException {
		boolean deleted = ftpClient.deleteFile(sourcePath);
		if (!deleted) {
			throw new IOException("Failed to delete file: " + sourcePath);
		}
	}
}
