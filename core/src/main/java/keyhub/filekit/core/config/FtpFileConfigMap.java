package keyhub.filekit.core.config;

import keyhub.filekit.core.name.NameFactory;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FtpFileConfigMap implements FileConfigMap {
	private FileStorageType type = FileStorageType.FTP;

	private String directoryPath;
	private NameFactory nameFactory;

	public FtpFileConfigMap(String directoryPath, NameFactory nameFactory) {
		this.directoryPath = directoryPath;
		this.nameFactory = nameFactory;
	}
}
