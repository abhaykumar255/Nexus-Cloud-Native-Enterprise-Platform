package com.nexus.file.service;

import com.nexus.file.config.FileStorageProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * SFTP Server using Apache MINA SSHD
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SftpServerService {

    private final FileStorageProperties storageProperties;
    private SshServer sshd;

    @PostConstruct
    public void startSftpServer() throws IOException {
        if (!storageProperties.getSftp().getEnabled()) {
            log.info("SFTP server is disabled");
            return;
        }

        log.info("Starting SFTP server on port {}", storageProperties.getSftp().getPort());

        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(storageProperties.getSftp().getPort());

        // Set host key
        Path hostKeyPath = Paths.get(storageProperties.getSftp().getHostKeyPath());
        if (!Files.exists(hostKeyPath.getParent())) {
            Files.createDirectories(hostKeyPath.getParent());
        }
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(hostKeyPath));

        // Set up file system
        Path uploadPath = Paths.get(storageProperties.getSftp().getUploadPath());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        VirtualFileSystemFactory fileSystemFactory = new VirtualFileSystemFactory();
        fileSystemFactory.setDefaultHomeDir(uploadPath);
        sshd.setFileSystemFactory(fileSystemFactory);

        // Set up SFTP subsystem
        sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));

        // Simple password authenticator (in production, use proper authentication)
        sshd.setPasswordAuthenticator((username, password, session) -> {
            // TODO: Integrate with auth service
            log.info("SFTP login attempt: {}", username);
            return "nexus".equals(username) && "nexus123".equals(password);
        });

        sshd.start();
        log.info("SFTP server started successfully on port {}", storageProperties.getSftp().getPort());
    }

    @PreDestroy
    public void stopSftpServer() throws IOException {
        if (sshd != null && sshd.isStarted()) {
            log.info("Stopping SFTP server");
            sshd.stop();
            log.info("SFTP server stopped");
        }
    }
}

