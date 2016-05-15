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

import biz.gabrys.maven.plugins.directory.content.file.FileMetadata;
import biz.gabrys.maven.plugins.directory.content.file.FileMetadataFactory;
import biz.gabrys.maven.plugins.directory.content.file.FileMetadataFactoryConfiguration;
import biz.gabrys.maven.plugins.directory.content.transform.TransformException;
import biz.gabrys.maven.plugins.directory.content.transform.XmlFileMetadataTransformer;
import biz.gabrys.maven.plugins.directory.content.transform.XsltTransformer;

/**
 * Transforms files list to a document using <a href="http://www.w3.org/TR/xslt">XSLT</a> technology. The XML document
 * used to transformation process has specified structure:
 * 
 * <pre>
 * &lt;files&gt;
 *    &lt;file&gt;
 *       &lt;fullPath&gt;full file path&lt;/fullPath&gt;
 *       &lt;fullName&gt;name with extension&lt;/fullName&gt;
 *       &lt;name&gt;name without extension&lt;/name&gt;
 *       &lt;!-- extension node is optionally --&gt;
 *       &lt;extension&gt;extension&lt;/extension&gt;
 *       &lt;directory&gt;parent directory relative to ${directory.content.sourceDirectory}&lt;/directory&gt;
 *       &lt;size&gt;size in bytes&lt;/size&gt;
 *    &lt;/file&gt;
 *    &lt;!-- more file nodes --&gt;
 * &lt;/files&gt;
 * </pre>
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * &lt;files&gt;
 *    &lt;file&gt;
 *       &lt;fullPath&gt;static/js/script.js&lt;/fullPath&gt;
 *       &lt;fullName&gt;script.js&lt;/fullName&gt;
 *       &lt;name&gt;script&lt;/name&gt;
 *       &lt;extension&gt;js&lt;/extension&gt;
 *       &lt;directory&gt;static/js&lt;/directory&gt;
 *       &lt;size&gt;131072&lt;/size&gt;
 *    &lt;/file&gt;
 *    &lt;file&gt;
 *       &lt;fullPath&gt;static/js/file-without-extension&lt;/fullPath&gt;
 *       &lt;fullName&gt;file-without-extension&lt;/fullName&gt;
 *       &lt;name&gt;file-without-extension&lt;/name&gt;
 *       &lt;directory&gt;static/js&lt;/directory&gt;
 *       &lt;size&gt;28672&lt;/size&gt;
 *    &lt;/file&gt;
 * &lt;/files&gt;
 * </pre>
 * 
 * @since 1.0
 */
@Mojo(name = "transformList", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class TransformListMojo extends AbstractTransformMojo {

    /**
     * Generated output document file.
     * @since 1.0
     */
    @Parameter(property = "directory.content.outputFile", required = true)
    protected File outputFile;

    @Override
    protected void fillParametersForLogger(final Collection<String> parameters) {
        super.fillParametersForLogger(parameters);
        parameters.add("outputFile = " + outputFile);
    }

    @Override
    protected void execute2(final Collection<File> files) throws MojoFailureException {
        if (!isTransformationRequired(files)) {
            getLog().info("Skips transforming files list, because all sources are older than output file: " + outputFile.getAbsolutePath());
            return;
        }
        getLog().info(String.format("Transforming list with %s file%s to document...", files.size(), files.size() != 1 ? "s" : ""));

        final String xml = transformToXml(files);
        final String document = transformToDocument(xml);
        saveDocument(document);
    }

    private boolean isTransformationRequired(final Collection<File> files) {
        for (final File file : files) {
            if (force || !outputFile.exists() || file.lastModified() >= outputFile.lastModified()) {
                return true;
            }
        }
        return false;
    }

    private String transformToXml(final Collection<File> files) throws MojoFailureException {
        if (getLog().isDebugEnabled()) {
            getLog().debug("Creating metadata...");
        }
        final FileMetadataFactoryConfiguration configuration = new FileMetadataFactoryConfiguration();
        configuration.setRootDirectory(sourceDirectory);
        configuration.setSeparator(separator);
        final Collection<FileMetadata> metadata = new FileMetadataFactory(configuration).create(files);
        return new XmlFileMetadataTransformer().transform(metadata);
    }

    private String transformToDocument(final String xml) throws MojoFailureException {
        if (getLog().isDebugEnabled()) {
            getLog().debug("Transforming metadata to document...");
        }
        try {
            return new XsltTransformer().transform(xml, xsltFile);
        } catch (final TransformException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    private void saveDocument(final String document) throws MojoFailureException {
        if (verbose) {
            getLog().info("Saving document to " + outputFile.getAbsolutePath());
        }
        try {
            FileUtils.write(outputFile, document, encoding);
        } catch (final IOException e) {
            throw new MojoFailureException("Cannot save document to file: " + outputFile.getAbsolutePath(), e);
        }
    }
}
