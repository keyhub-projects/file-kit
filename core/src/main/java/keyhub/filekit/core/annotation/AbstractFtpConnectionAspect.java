package keyhub.filekit.core.annotation;

import org.apache.commons.net.ftp.FTPClient;

public abstract class AbstractFtpConnectionAspect implements FtpConnectionAspect{
    protected final static ThreadLocal<FTPClient> ftpConnectionThreadLocal = new ThreadLocal<>();

    protected static FTPClient ftpClient() {
        FTPClient ftpClient = ftpConnectionThreadLocal.get();
        if (ftpClient == null) {
            throw new IllegalStateException("No ftp connection");
        }
        return ftpClient;
    }
}
