package biz.gabrys.maven.plugins.directory.content.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

public final class DirectoryResolverTest {

    @Test
    public void resolve_fileIsInRootDirectoryAndSeparatorIsEqualToDefault_returnsEmptyPath() {
        final File file = new File("/root/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, File.separator);
        assertThat(path).isEmpty();
    }

    @Test
    public void resolve_fileIsInRootDirectoriesAndCustomSeparator_returnsEmptyPath() {
        final File file = new File("/root/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, ";");
        assertThat(path).isEmpty();
    }

    @Test
    public void resolve_fileIsInSubdirectoriesAndSeparatorIsEqualToDefault_returnsDirectoryPath() {
        final File file = new File("/root/directory1/directory2/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, File.separator);
        assertThat(path).isEqualTo("directory1" + File.separator + "directory2");
    }

    @Test
    public void resolve_fileIsInSubdirectoriesAndCustomSeparator_returnsDirectoryPath() {
        final File file = new File("/root/directory1/directory2/file");
        final File rootDirectory = new File("/root");
        final String path = new DirectoryResolver().resolve(file, rootDirectory, ";");
        assertThat(path).isEqualTo("directory1;directory2");
    }
}
