/*
 * Directory Content Maven Plugin
 * http://directory-content-maven-plugin.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabrys
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 * - a copy of the License at project page
 * - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.maven.plugins.directory.content.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Responsible for creating {@link FileMetadata files metadata}.
 * @since 1.0
 */
public class FileMetadataFactory {

    private final FileMetadataFactoryConfiguration configuration;
    private final FileNameResolver fileNameResolver;
    private final DirectoryResolver directoryResolver;

    /**
     * Constructs a new instance and sets configuration.
     * @param configuration the factory configuration.
     * @since 1.0
     */
    public FileMetadataFactory(final FileMetadataFactoryConfiguration configuration) {
        this(configuration, new FileNameResolver(), new DirectoryResolver());
    }

    FileMetadataFactory(final FileMetadataFactoryConfiguration configuration, final FileNameResolver fileNameResolver,
            final DirectoryResolver directoryResolver) {
        this.configuration = configuration;
        this.fileNameResolver = fileNameResolver;
        this.directoryResolver = directoryResolver;
    }

    /**
     * Creates metadata for files.
     * @param files the collection with files.
     * @return files metadata.
     * @since 1.0
     */
    public List<FileMetadata> create(final Collection<File> files) {
        final List<FileMetadata> metadata = new ArrayList<FileMetadata>(files.size());
        for (final File file : files) {
            metadata.add(create(file));
        }
        return metadata;
    }

    /**
     * Creates metadata for file.
     * @param file the file.
     * @return the file metadata.
     * @since 1.0
     */
    public FileMetadata create(final File file) {
        final String directory = directoryResolver.resolve(file, configuration.getRootDirectory(), configuration.getSeparator());
        final String path = "".equals(directory) ? "" : directory + configuration.getSeparator();
        final String name = file.getName();

        final FileMetadata metadata = new FileMetadata();
        metadata.setFullPath(path + name);
        metadata.setFullName(name);
        metadata.setName(fileNameResolver.resolveName(file));
        metadata.setExtension(fileNameResolver.resolveExtension(file));
        metadata.setDirectory(directory);
        metadata.setSize(file.length());
        return metadata;
    }
}
