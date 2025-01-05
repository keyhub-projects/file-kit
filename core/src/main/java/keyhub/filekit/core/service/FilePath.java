package keyhub.filekit.core.service;

public interface FilePath<ID> {
	ID getId();

	String getName();

	String getPath();
}
