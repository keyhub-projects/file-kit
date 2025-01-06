package keyhub.filekit.core.annotation;


public @interface FtpConnection {
    String ftpFileUploaderConfig();
    FtpFileUploaderConfigFrom repoPathFrom() default FtpFileUploaderConfigFrom.PARAMETER;
    enum FtpFileUploaderConfigFrom {
        FIELD, PARAMETER
    }
}
