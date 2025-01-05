package keyhub.filekit.core.mover;

import keyhub.filekit.core.FileStorage;
import keyhub.filekit.core.config.FileConfigMap;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Accessors(chain = true)
public class SimpleFileMover<FROM_ID, TO_ID> implements FileMover<FROM_ID, TO_ID> {
	private final FileStorage<FROM_ID> from;
	private final FileStorage<TO_ID> to;
	private final Map<String, String> sourcePath;

	private SimpleFileMover(FileStorage<FROM_ID> from, FileStorage<TO_ID> toIdFileStorage) {
		this.from = from;
		this.to = toIdFileStorage;
		this.sourcePath = new HashMap<>();
	}

	public static <FROM_ID, TO_ID> SimpleFileMover<FROM_ID, TO_ID> of(
		FileStorage<FROM_ID> from,
		FileStorage<TO_ID> to
	) {
		return new SimpleFileMover<>(from, to);
	}

	@Override
	public TO_ID move(String fromSourcePath, String toSourcePath) throws IOException {
		return moveFile(fromSourcePath, toSourcePath);
	}

	@Override
	public SimpleFileMover<FROM_ID, TO_ID> add(String fromSourcePath, String toSourcePath) throws IOException {
		if(sourcePath.containsKey(toSourcePath)){
			throw new IOException("Same destination already exists");
		}
		sourcePath.put(toSourcePath, fromSourcePath);
		return this;
	}

	@Override
	public int moveAll() throws IOException {
		try {
			AtomicInteger result = new AtomicInteger(0);
			Map<String, Throwable> errors = sourcePath.entrySet()
				.parallelStream()
				.map(entry -> {
					try {
						String fromSourcePath =  entry.getValue();
						String toSourcePath = entry.getKey();
						moveFile(fromSourcePath, toSourcePath);
						result.incrementAndGet();
						return null; // No error
					} catch (Throwable ex) {
						return Map.entry(entry.getKey(), ex);
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			if (!errors.isEmpty()) {
				errors.forEach((path, ex) -> {
					System.err.println("Failed to move file: " + path);
					ex.printStackTrace();
				});
			}
			return result.get();
		} finally {
			this.sourcePath.clear();
		}
	}

	private TO_ID moveFile(String fromSourcePath, String toSourcePath) throws IOException {
		FileConfigMap fromConfig = this.from.configMap();
		FileConfigMap toConfig = this.to.configMap();
		if (fromConfig.equals(toConfig) && fromSourcePath.equals(toSourcePath)) {
			@SuppressWarnings("unchecked")
			var result = (TO_ID)this.from.findByPath(fromSourcePath)
				.orElseThrow(()->new IOException("It exists, but not exists in DB")).id();
			return result;
		}
		Path path = this.from.read(fromSourcePath);
		return this.to.write(path, toSourcePath).id();
	}
}
