package keyhub.filekit.core.uploader;

import keyhub.filekit.core.name.NameGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
@Getter
public class FtpFileUploaderConfigMap implements FileUploaderConfigMap {
	private FileUploaderType type = FileUploaderType.FTP;

	private String directoryPath;
	private NameGenerator nameGenerator;
	private String url;
	private Integer port;
	private String username;
	private String password;

	public FtpFileUploaderConfigMap(String directoryPath, NameGenerator nameGenerator) {
		this.directoryPath = directoryPath;
		this.nameGenerator = nameGenerator;
	}
}
