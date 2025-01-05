package keyhub.filekit.core.name;

import java.util.UUID;

public class UuidV7NameFactory implements NameFactory {

	@Override
	public String generateName() {
		return getUuidV7().toString();
	}

	private UUID getUuidV7() {
		return UuidV7Generator.generate();
	}
}
