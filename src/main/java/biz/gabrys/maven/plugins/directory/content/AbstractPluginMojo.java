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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import biz.gabrys.maven.plugin.util.io.FileScanner;
import biz.gabrys.maven.plugin.util.io.ScannerFactory;
import biz.gabrys.maven.plugin.util.io.ScannerPatternFormat;
import biz.gabrys.maven.plugin.util.timer.SystemTimer;
import biz.gabrys.maven.plugin.util.timer.Timer;

/**
 * Parent class for all plugin goals.
 * @since 1.0
 */
abstract class AbstractPluginMojo extends AbstractMojo {

    /**
     * Defines whether to skip the plugin execution.
     * @since 1.0
     */
    @Parameter(property = "directory.content.skip", defaultValue = "false")
    protected boolean skip;

    /**
     * Defines whether the plugin runs in verbose mode.<br>
     * <b>Notice</b>: always true in debug mode.
     * @since 1.0
     */
    @Parameter(property = "directory.content.verbose", defaultValue = "false")
    protected boolean verbose;

    /**
     * Forces to always process files. By default file is only processed when modified or the destination file does not
     * exist.
     * @since 1.0
     */
    @Parameter(property = "directory.content.force", defaultValue = "false")
    protected boolean force;

    /**
     * The directory with source files.
     * @since 1.0
     */
    @Parameter(property = "directory.content.sourceDirectory", defaultValue = "${project.build.sourceDirectory}")
    protected File sourceDirectory;

    /**
     * Defines inclusion and exclusion fileset patterns format. Available options:
     * <ul>
     * <li><b>ant</b> - <a href="http://ant.apache.org/">Ant</a>
     * <a href="http://ant.apache.org/manual/dirtasks.html#patterns">patterns</a></li>
     * <li><b>regex</b> - regular expressions (use '/' as path separator)</li>
     * </ul>
     * @since 1.0
     */
    @Parameter(property = "directory.content.filesetPatternFormat", defaultValue = "ant")
    protected String filesetPatternFormat;

    /**
     * List of files to include. Specified as fileset patterns which are relative to the
     * <a href="#sourceDirectory">source directory</a>. See <a href="#filesetPatternFormat">available fileset patterns
     * formats</a>.
     * @since 1.0
     */
    @Parameter(required = true)
    protected String[] includes;

    /**
     * List of files to exclude. Specified as fileset patterns which are relative to the
     * <a href="#sourceDirectory">source directory</a>. See <a href="#filesetPatternFormat">available fileset patterns
     * formats</a>.<br>
     * <b>Default value is</b>: <tt>[]</tt>.
     * @since 1.0
     */
    @Parameter
    protected String[] excludes = new String[0];

    private void logParameters() {
        if (getLog().isDebugEnabled()) {
            getLog().debug("Job parameters:");
            getLog().debug("\tskip = " + skip);
            getLog().debug(String.format("\tverbose = %s (calculated: true)", verbose));
            getLog().debug("\tsourceDirectory = " + sourceDirectory);
            getLog().debug("\tfilesetPatternFormat = " + filesetPatternFormat);
            getLog().debug("\tincludes = " + Arrays.toString(includes));
            getLog().debug("\texcludes = " + Arrays.toString(excludes));
            final Collection<String> parameters = new ArrayList<String>();
            fillParametersForLogger(parameters);
            for (final String parameter : parameters) {
                getLog().debug('\t' + parameter);
            }
            getLog().debug("");
        }
    }

    protected abstract void fillParametersForLogger(Collection<String> parameters);

    private void calculateParameters() {
        if (getLog().isDebugEnabled()) {
            verbose = true;
        }
    }

    public final void execute() throws MojoFailureException {
        final Timer timer = SystemTimer.getStartedTimer();
        logParameters();
        if (skip) {
            getLog().info("Skipping job execution");
            return;
        }
        calculateParameters();
        if (!sourceDirectory.exists()) {
            getLog().warn("Source directory does not exist: " + sourceDirectory.getAbsolutePath());
            return;
        }
        final Collection<File> files = getFiles();
        if (files.isEmpty()) {
            getLog().warn("Zero files to process");
            return;
        }
        execute(files);
        if (verbose) {
            getLog().info("Finished in " + timer.stop());
        }
    }

    private Collection<File> getFiles() {
        final ScannerPatternFormat patternFormat = ScannerPatternFormat.toPattern(filesetPatternFormat);
        final FileScanner scanner = new ScannerFactory().create(patternFormat, getLog());
        if (verbose) {
            getLog().info("Scanning directory for files...");
        }
        return scanner.getFiles(sourceDirectory, includes, excludes);
    }

    protected abstract void execute(Collection<File> files) throws MojoFailureException;
}
