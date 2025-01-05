package keyhub.filekit.core.service;

public interface FilePath<ID> {
	ID id();

	String name();

	String path();
}
