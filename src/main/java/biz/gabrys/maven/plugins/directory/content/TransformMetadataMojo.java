/*
 * Directory Content Maven Plugin
 * http://directory-content-maven-plugin.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabryś
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 * - a copy of the License at project page
 * - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.maven.plugins.directory.content;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import biz.gabrys.maven.plugin.util.io.DestinationFileCreator;
import biz.gabrys.maven.plugin.util.timer.SystemTimer;
import biz.gabrys.maven.plugin.util.timer.Timer;
import biz.gabrys.maven.plugins.directory.content.file.FileMetadata;
import biz.gabrys.maven.plugins.directory.content.file.FileMetadataFactory;
import biz.gabrys.maven.plugins.directory.content.file.FileMetadataFactoryConfiguration;
import biz.gabrys.maven.plugins.directory.content.transform.TransformException;
import biz.gabrys.maven.plugins.directory.content.transform.XmlFileMetadataTransformer;
import biz.gabrys.maven.plugins.directory.content.transform.XsltTransformer;

/**
 * Transforms files metadata to documents using <a href="http://www.w3.org/TR/xslt">XSLT</a> technology. Each file
 * metadata used to transformation process has specified structure:
 * 
 * <pre>
 * &lt;file&gt;
 *    &lt;fullPath&gt;full file path&lt;/fullPath&gt;
 *    &lt;fullName&gt;name with extension&lt;/fullName&gt;
 *    &lt;name&gt;name without extension&lt;/name&gt;
 *    &lt;!-- extension node is optionally --&gt;
 *    &lt;extension&gt;extension&lt;/extension&gt;
 *    &lt;directory&gt;parent directory relative to ${directory.content.sourceDirectory}&lt;/directory&gt;
 *    &lt;size&gt;size in bytes&lt;/size&gt;
 * &lt;/file&gt;
 * </pre>
 * <p>
 * Examples:
 * </p>
 * 
 * <pre>
 * &lt;file&gt;
 *    &lt;fullPath&gt;static/js/script.js&lt;/fullPath&gt;
 *    &lt;fullName&gt;script.js&lt;/fullName&gt;
 *    &lt;name&gt;script&lt;/name&gt;
 *    &lt;extension&gt;js&lt;/extension&gt;
 *    &lt;directory&gt;static/js&lt;/directory&gt;
 *    &lt;size&gt;131072&lt;/size&gt;
 * &lt;/file&gt;
 * </pre>
 * 
 * <pre>
 * &lt;file&gt;
 *    &lt;fullPath&gt;static/js/file-without-extension&lt;/fullPath&gt;
 *    &lt;fullName&gt;file-without-extension&lt;/fullName&gt;
 *    &lt;name&gt;file-without-extension&lt;/name&gt;
 *    &lt;directory&gt;static/js&lt;/directory&gt;
 *    &lt;size&gt;28672&lt;/size&gt;
 * &lt;/file&gt;
 * </pre>
 * 
 * @since 1.0
 */
@Mojo(name = "transformMetadata", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class TransformMetadataMojo extends AbstractTransformMojo {

    /**
     * The directory for transformed files.
     * @since 1.0
     */
    @Parameter(property = "directory.content.outputDirectory", defaultValue = "${project.build.directory}")
    protected File outputDirectory;

    /**
     * Destination files naming format. {fileName} is equal to source file name without extension.
     * @since 1.0
     */
    @Parameter(property = "directory.content.outputFileFormat", required = true)
    protected String outputFileFormat;

    @Override
    protected void fillParametersForLogger(final Collection<String> parameters) {
        super.fillParametersForLogger(parameters);
        parameters.add("outputDirectory = " + outputDirectory);
        parameters.add("outputFileFormat = " + outputFileFormat);
    }

    @Override
    protected void execute2(final Collection<File> files) throws MojoFailureException {
        getLog().info(String.format("Transforming %s file%s...", files.size(), files.size() != 1 ? "s" : ""));

        for (final File file : files) {
            transformFile(file);
        }
    }

    private void transformFile(final File file) throws MojoFailureException {
        final File destination = new DestinationFileCreator(sourceDirectory, outputDirectory, outputFileFormat).create(file);
        if (!force && destination.exists() && file.lastModified() < destination.lastModified()) {
            if (verbose) {
                getLog().info("Skips transforming metadata, because source is older than output file: " + destination.getAbsolutePath());
            }
            return;
        }

        Timer timer = null;
        if (verbose) {
            getLog().info("Transforming metadata of the file: " + file.getAbsolutePath());
            timer = SystemTimer.getStartedTimer();
        }

        final FileMetadata metadata = createMetadata(file);
        final String document = transformMetadata(metadata);
        saveDocument(document, destination);
        if (timer != null) {
            getLog().info("Finished in " + timer.stop());
        }
    }

    private FileMetadata createMetadata(final File file) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("Creating metadata...");
        }
        final FileMetadataFactoryConfiguration configuration = new FileMetadataFactoryConfiguration();
        configuration.setRootDirectory(sourceDirectory);
        configuration.setSeparator(separator);
        return new FileMetadataFactory(configuration).create(file);
    }

    private String transformMetadata(final FileMetadata metadata) throws MojoFailureException {
        if (getLog().isDebugEnabled()) {
            getLog().debug("Transforming metadata to document...");
        }
        final String xml = new XmlFileMetadataTransformer().transform(metadata);
        try {
            return new XsltTransformer().transform(xml, xsltFile);
        } catch (final TransformException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    private void saveDocument(final String document, final File destination) throws MojoFailureException {
        if (verbose) {
            getLog().info("Saving transformed document to " + destination.getAbsolutePath());
        }
        try {
            FileUtils.write(destination, document, encoding);
        } catch (final IOException e) {
            throw new MojoFailureException("Cannot save transformed document to file: " + destination.getAbsolutePath(), e);
        }
    }
}
