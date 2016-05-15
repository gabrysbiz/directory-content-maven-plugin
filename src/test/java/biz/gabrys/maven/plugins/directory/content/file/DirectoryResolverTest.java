package biz.gabrys.maven.plugins.directory.content.file;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public final class DirectoryResolverTest {

    @Test
    public void resolve_fileIsInRootDirectoryAndSeparatorIsEqualToDefault_returnEmptyPath() {
        final File file = new File("/root/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, File.separator);
        Assert.assertEquals("Resolved directory path.", "", path);
    }

    @Test
    public void resolve_fileIsInRootDirectoriesAndCustomSeparator_returnEmptyPath() {
        final File file = new File("/root/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, ";");
        Assert.assertEquals("Resolved directory path.", "", path);
    }

    @Test
    public void resolve_fileIsInSubdirectoriesAndSeparatorIsEqualToDefault_returnDirectoryPath() {
        final File file = new File("/root/directory1/directory2/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, File.separator);
        Assert.assertEquals("Resolved directory path.", "directory1" + File.separator + "directory2", path);
    }

    @Test
    public void resolve_fileIsInSubdirectoriesAndCustomSeparator_returnDirectoryPath() {
        final File file = new File("/root/directory1/directory2/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, ";");
        Assert.assertEquals("Resolved directory path.", "directory1;directory2", path);
    }
}
