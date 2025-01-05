package keyhub.filekit.core.config;

import keyhub.filekit.core.name.NameFactory;

public interface FileConfigMap {
	String getDirectoryPath();

	FileStorageType getType();

	NameFactory getNameFactory();
}
