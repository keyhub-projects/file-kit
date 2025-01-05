package keyhub.filekit.core.mover;

import keyhub.filekit.core.FileStorage;

import java.io.IOException;

public interface FileMover<FROM_ID, TO_ID> {
	static <FROM_ID, TO_ID> FileMover<FROM_ID, TO_ID> of(FileStorage<FROM_ID> from, FileStorage<TO_ID> to){
		return SimpleFileMover.of(from, to);
	}

	/**
	 * 단건 처리
	 * @param fromSourcePath
	 * @param toSourcePath
	 * @return 처리결과
	 * @throws IOException
	 */
	TO_ID move(String fromSourcePath, String toSourcePath) throws IOException;

	/**
	 * 다건 처리를 위한 경로 추가
	 * @param fromSourcePath
	 * @param toSourcePath
	 * @return 체이닝
	 * @throws IOException
	 */
	FileMover<FROM_ID, TO_ID> add(String fromSourcePath, String toSourcePath) throws IOException;

	/**
	 * 다건 처리
	 * @return 처리결과총합
	 * @throws IOException
	 */
	int moveAll() throws IOException;
}
