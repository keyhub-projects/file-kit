package keyhub.filekit.core.uploader;

import keyhub.filekit.core.name.NameGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
@Getter
public class LocalFileUploaderConfigMap implements FileUploaderConfigMap {
	private FileUploaderType type = FileUploaderType.LOCAL;
	private String directoryPath;
	private NameGenerator nameGenerator;

	public LocalFileUploaderConfigMap(String directoryPath, NameGenerator nameGenerator) {
		this.directoryPath = directoryPath;
		this.nameGenerator = nameGenerator;
	}
}
