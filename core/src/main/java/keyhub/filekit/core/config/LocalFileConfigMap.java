package keyhub.filekit.core.config;

import keyhub.filekit.core.name.NameFactory;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocalFileConfigMap implements FileConfigMap {
	private FileStorageType type = FileStorageType.LOCAL;
	private String directoryPath;
	private NameFactory nameFactory;

	public LocalFileConfigMap(String directoryPath, NameFactory nameFactory) {
		this.directoryPath = directoryPath;
		this.nameFactory = nameFactory;
	}
}
