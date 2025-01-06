package keyhub.filekit.core.name;

import java.util.UUID;

public class UuidV7NameGenerator implements NameGenerator {

	@Override
	public String generateName() {
		return getUuidV7().toString();
	}

	private UUID getUuidV7() {
		return UuidV7Generator.generate();
	}
}
