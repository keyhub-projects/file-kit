package keyhub.filekit.core.uploader;

import keyhub.filekit.core.name.NameGenerator;

public interface FileUploaderConfigMap {
	String directoryPath();
	FileUploaderType type();
	NameGenerator nameGenerator();
}
