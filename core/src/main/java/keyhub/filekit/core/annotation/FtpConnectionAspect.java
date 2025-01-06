package keyhub.filekit.core.annotation;

import org.apache.commons.net.ftp.FTPClient;
import org.aspectj.lang.ProceedingJoinPoint;

public interface FtpConnectionAspect {
    Object manageFtpResource(ProceedingJoinPoint joinPoint, FtpConnection ftpConnection);

    static FTPClient ftpClient() {
        return AbstractFtpConnectionAspect.ftpClient();
    }
}
