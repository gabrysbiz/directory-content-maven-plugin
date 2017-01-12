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

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import biz.gabrys.maven.plugin.util.parameter.ParametersLogBuilder;
import biz.gabrys.maven.plugin.util.parameter.sanitizer.SimpleSanitizer;

/**
 * Copies file from source to output directory (allow to change name).
 * @since 1.1.0
 */
@Mojo(name = "copyFile", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class CopyFileMojo extends AbstractMojo {

    /**
     * Defines whether to skip the plugin execution.
     * @since 1.1.0
     */
    @Parameter(property = "directory.content.skip", defaultValue = "false")
    protected boolean skip;

    /**
     * Defines whether to skip the plugin execution if source file does not exist.
     * @since 1.2.0
     */
    @Parameter(property = "directory.content.skipIfSourceFileDoesNotExist", defaultValue = "false")
    protected boolean skipIfSourceFileDoesNotExist;

    /**
     * Forces to always copy file. By default file is only copied when modified or the destination file does not exist.
     * @since 1.1.0
     */
    @Parameter(property = "directory.content.force", defaultValue = "false")
    protected boolean force;

    /**
     * The directory with source file.
     * @since 1.1.0
     */
    @Parameter(property = "directory.content.sourceDirectory", defaultValue = "${basedir}")
    protected File sourceDirectory;

    /**
     * The source file path which will be copied (examples: <code>filename.ext</code>,
     * <code>directory/filename.ext</code>).
     * @since 1.1.0
     */
    @Parameter(property = "directory.content.sourceFilePath", required = true)
    protected String sourceFilePath;

    /**
     * The destination directory for copied file.
     * @since 1.1.0
     */
    @Parameter(property = "directory.content.outputDirectory", defaultValue = "${project.build.directory}")
    protected File outputDirectory;

    /**
     * The destination file path (examples: <code>filename.ext</code>, <code>directory/filename.ext</code>).<br>
     * <b>Default value is</b>: the same as <a href="#sourceFilePath">source file path</a>.
     * @since 1.1.0
     */
    @Parameter(property = "directory.content.outputFilePath")
    protected String outputFilePath;

    private void logParameters() {
        if (!getLog().isDebugEnabled()) {
            return;
        }

        final ParametersLogBuilder logger = new ParametersLogBuilder(getLog());
        logger.append("skip", skip);
        logger.append("skipIfSourceFileDoesNotExist", skipIfSourceFileDoesNotExist);
        logger.append("force", force);
        logger.append("sourceDirectory", sourceDirectory);
        logger.append("sourceFilePath", sourceFilePath);
        logger.append("outputDirectory", outputDirectory);
        logger.append("outputFilePath", outputFilePath, new SimpleSanitizer(isOutputFilePathValid(), sourceFilePath));
        logger.debug();
    }

    private void calculateParameters() {
        if (!isOutputFilePathValid()) {
            outputFilePath = sourceFilePath;
        }
    }

    private boolean isOutputFilePathValid() {
        return outputFilePath != null && outputFilePath.length() > 0;
    }

    public void execute() throws MojoFailureException {
        logParameters();
        if (shouldSkipExecution()) {
            return;
        }
        calculateParameters();
        validateParameters();
        final File sourceFile = new File(sourceDirectory, sourceFilePath);
        final File outputFile = new File(outputDirectory, outputFilePath);
        if (shouldCopyFile(sourceFile, outputFile)) {
            copyFile(sourceFile, outputFile);
        }
    }

    private boolean shouldSkipExecution() {
        if (skip) {
            getLog().info("Skipping job execution");
            return true;
        }
        if (skipIfSourceFileDoesNotExist) {
            final File sourceFile = new File(sourceDirectory, sourceFilePath);
            if (!sourceFile.exists()) {
                getLog().info("Skipping job execution, because source file does not exist: " + sourceFile.getAbsolutePath());
                return true;
            }
        }
        return false;

    }

    private void validateParameters() throws MojoFailureException {
        if (!sourceDirectory.exists()) {
            throw new MojoFailureException("Source directory does not exist: " + sourceDirectory.getAbsolutePath());
        }
        final File file = new File(sourceDirectory, sourceFilePath);
        if (!file.exists()) {
            throw new MojoFailureException("Source file does not exist: " + file.getAbsolutePath());
        }
    }

    private boolean shouldCopyFile(final File sourceFile, final File outputFile) {
        if (!force && outputFile.exists() && sourceFile.lastModified() <= outputFile.lastModified()) {
            getLog().info(String.format("Skips copy file, because source (%s) is older than destination file (%s)",
                    sourceFile.getAbsolutePath(), outputFile.getAbsolutePath()));
            return false;
        }
        return true;
    }

    private void copyFile(final File sourceFile, final File outputFile) throws MojoFailureException {
        getLog().info(String.format("Copying %s to %s", sourceFile.getAbsolutePath(), outputFile.getAbsolutePath()));
        try {
            FileUtils.copyFile(sourceFile, outputFile);
        } catch (final IOException e) {
            final String message = String.format("Cannot copy %s to %s", sourceFile.getAbsolutePath(), outputFile.getAbsolutePath());
            throw new MojoFailureException(message, e);
        }
    }
}
