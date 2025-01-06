package keyhub.filekit.core.annotation;

import keyhub.filekit.core.uploader.FtpFileUploaderConfigMap;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;

@Aspect
public class SimpleFtpConnectionAspect extends AbstractFtpConnectionAspect {
    @Override
    @Around("@annotation(ftpConnection)")
    public Object manageFtpResource(ProceedingJoinPoint joinPoint, FtpConnection ftpConnection) {
        try{
            FtpFileUploaderConfigMap configMap = getFtpFileUploaderConfigMap(joinPoint, ftpConnection);
            FTPClient ftpClient = new FTPClient();
            ftpClient.setControlEncoding("UTF-8");
            try {
                ftpClient.connect(configMap.url(), configMap.port());
                int reply = ftpClient.getReplyCode();
                if(!FTPReply.isPositiveCompletion(reply)){
                    throw new FileUploadException("FTP server refused connection.");
                }
                if(!ftpClient.login(configMap.username(), configMap.password())){
                    throw new FileUploadException("FTP server refused login.");
                }
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setControlEncoding("UTF-8");
                ftpConnectionThreadLocal.set(ftpClient);
                return joinPoint.proceed();
            } finally {
                ftpClient.logout();
                ftpClient.disconnect();
                ftpConnectionThreadLocal.remove();
            }
        }  catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    private FtpFileUploaderConfigMap getFtpFileUploaderConfigMap(ProceedingJoinPoint joinPoint, FtpConnection ftpConnection) throws NoSuchFieldException, IllegalAccessException {
        Object obj = null;
        String name = ftpConnection.ftpFileUploaderConfig();
        FtpConnection.FtpFileUploaderConfigFrom ftpFileUploaderConfigFrom = ftpConnection.repoPathFrom();
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("No ftpFileUploaderConfig path");
        }
        Object target = joinPoint.getTarget();
        switch (ftpFileUploaderConfigFrom) {
            case FIELD -> {
                Field field = target.getClass().getField(name);
                field.setAccessible(true);
                obj = field.get(target);
            }
            case PARAMETER -> {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                var parameterNames = signature.getParameterNames();
                var args = joinPoint.getArgs();
                for (int i = 0; i < parameterNames.length; i++) {
                    if (parameterNames[i].equals(name)) {
                        obj = args[i];
                        break;
                    }
                }
            }
        }
        return (FtpFileUploaderConfigMap) obj;
    }

}