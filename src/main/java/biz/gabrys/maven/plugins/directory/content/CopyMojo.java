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

import biz.gabrys.maven.plugin.util.timer.SystemTimer;
import biz.gabrys.maven.plugin.util.timer.Timer;

/**
 * Copies files from source to output directory.
 * @since 1.0
 */
@Mojo(name = "copy", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class CopyMojo extends AbstractPluginMojo {

    /**
     * The destination directory for copied files.
     * @since 1.0
     */
    @Parameter(property = "directory.content.outputDirectory", defaultValue = "${project.build.directory}")
    protected File outputDirectory;

    @Override
    protected void fillParametersForLogger(final Collection<String> parameters) {
        parameters.add("outputDirectory = " + outputDirectory);
    }

    @Override
    protected void execute(final Collection<File> files) throws MojoFailureException {
        getLog().info(String.format("Coping %s file%s...", files.size(), files.size() != 1 ? "s" : ""));

        for (final File file : files) {
            copy(file, getDestination(file));
        }
    }

    private File getDestination(final File file) {
        final String path = file.getAbsolutePath().substring(sourceDirectory.getAbsolutePath().length());
        return new File(outputDirectory.getAbsolutePath() + File.separator + path);
    }

    private void copy(final File source, final File destination) throws MojoFailureException {
        if (!force && destination.exists() && source.lastModified() < destination.lastModified()) {
            if (verbose) {
                getLog().info("Skips copy file, because source is older than destination file: " + destination.getAbsolutePath());
            }
            return;
        }

        Timer timer = null;
        if (verbose) {
            getLog().info("Coping the file: " + source.getAbsolutePath());
            timer = SystemTimer.getStartedTimer();
        }
        try {
            FileUtils.copyFile(source, destination);
        } catch (final IOException e) {
            throw new MojoFailureException("Cannot copy file to: " + destination.getAbsolutePath(), e);
        }
        if (timer != null) {
            getLog().info("Finished in " + timer.stop());
        }
    }
}
