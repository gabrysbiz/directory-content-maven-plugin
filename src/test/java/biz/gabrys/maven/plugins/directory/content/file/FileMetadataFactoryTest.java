package biz.gabrys.maven.plugins.directory.content.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public final class FileMetadataFactoryTest {

    @Test
    public void create_oneFile_returnMetadata() {
        final FileMetadataFactoryConfiguration configuration = new FileMetadataFactoryConfiguration();
        final File rootDirectory = new File("/root");
        configuration.setRootDirectory(rootDirectory);
        final String separator = "separator";
        configuration.setSeparator(separator);

        final File file = mock(File.class);
        final String fullName = "name.extension";
        when(file.getName()).thenReturn(fullName);

        final FileNameResolver fileNameResolver = mock(FileNameResolver.class);
        final String name = "name";
        when(fileNameResolver.resolveName(file)).thenReturn(name);
        final String extension = "extension";
        when(fileNameResolver.resolveExtension(file)).thenReturn(extension);

        final DirectoryResolver directoryResolver = mock(DirectoryResolver.class);
        final String directory = "directory";
        when(directoryResolver.resolve(file, rootDirectory, separator)).thenReturn(directory);

        final String fullPath = directory + separator + name + "." + extension;

        final long size = 1;
        when(file.length()).thenReturn(size);

        final FileMetadataFactory factory = spy(new FileMetadataFactory(configuration, fileNameResolver, directoryResolver));
        final FileMetadata metadata = factory.create(file);

        assertThat(metadata.getFullPath()).isEqualTo(fullPath);
        assertThat(metadata.getFullName()).isEqualTo(fullName);
        assertThat(metadata.getName()).isEqualTo(name);
        assertThat(metadata.getExtension()).isEqualTo(extension);
        assertThat(metadata.getDirectory()).isEqualTo(directory);
        assertThat(metadata.getSize()).isEqualTo(size);

        verify(file).getName();
        verify(factory).create(file);
        verify(fileNameResolver).resolveName(file);
        verify(fileNameResolver).resolveExtension(file);
        verify(directoryResolver).resolve(file, rootDirectory, separator);
        verifyNoMoreInteractions(factory, fileNameResolver, directoryResolver);
    }

    @Test
    public void create_twoFiles_returnMetadata() {
        final FileMetadataFactoryConfiguration configuration = new FileMetadataFactoryConfiguration();
        final File rootDirectory = new File("/root");
        configuration.setRootDirectory(rootDirectory);
        final String separator = "separator";
        configuration.setSeparator(separator);

        final File file1 = mock(File.class);
        final String fullName1 = "name1.extension1";
        when(file1.getName()).thenReturn(fullName1);
        final File file2 = mock(File.class);
        final String fullName2 = "name2.extension2";
        when(file2.getName()).thenReturn(fullName2);

        final FileNameResolver fileNameResolver = mock(FileNameResolver.class);
        final String name1 = "name1";
        when(fileNameResolver.resolveName(file1)).thenReturn(name1);
        final String extension1 = "extension1";
        when(fileNameResolver.resolveExtension(file1)).thenReturn(extension1);
        final String name2 = "name2";
        when(fileNameResolver.resolveName(file2)).thenReturn(name2);
        final String extension2 = "extension2";
        when(fileNameResolver.resolveExtension(file2)).thenReturn(extension2);

        final DirectoryResolver directoryResolver = mock(DirectoryResolver.class);
        final String directory1 = "directory1";
        when(directoryResolver.resolve(file1, rootDirectory, separator)).thenReturn(directory1);
        final String directory2 = "directory2";
        when(directoryResolver.resolve(file2, rootDirectory, separator)).thenReturn(directory2);

        final String fullPath1 = directory1 + separator + name1 + "." + extension1;
        final String fullPath2 = directory2 + separator + name2 + "." + extension2;

        final long size1 = 1;
        when(file1.length()).thenReturn(size1);
        final long size2 = 1;
        when(file2.length()).thenReturn(size2);

        final FileMetadataFactory factory = spy(new FileMetadataFactory(configuration, fileNameResolver, directoryResolver));
        final List<File> files = Arrays.asList(file1, file2);
        final List<FileMetadata> metadata = factory.create(files);

        FileMetadata fileMetadata = metadata.get(0);
        assertThat(fileMetadata.getFullPath()).isEqualTo(fullPath1);
        assertThat(fileMetadata.getFullName()).isEqualTo(fullName1);
        assertThat(fileMetadata.getName()).isEqualTo(name1);
        assertThat(fileMetadata.getExtension()).isEqualTo(extension1);
        assertThat(fileMetadata.getDirectory()).isEqualTo(directory1);
        assertThat(fileMetadata.getSize()).isEqualTo(size1);

        fileMetadata = metadata.get(1);
        assertThat(fileMetadata.getFullPath()).isEqualTo(fullPath2);
        assertThat(fileMetadata.getFullName()).isEqualTo(fullName2);
        assertThat(fileMetadata.getName()).isEqualTo(name2);
        assertThat(fileMetadata.getExtension()).isEqualTo(extension2);
        assertThat(fileMetadata.getDirectory()).isEqualTo(directory2);
        assertThat(fileMetadata.getSize()).isEqualTo(size2);

        verify(file1).getName();
        verify(file2).getName();
        verify(factory).create(files);
        verify(factory).create(file1);
        verify(fileNameResolver).resolveName(file1);
        verify(fileNameResolver).resolveExtension(file1);
        verify(directoryResolver).resolve(file1, rootDirectory, separator);
        verify(factory).create(file2);
        verify(fileNameResolver).resolveName(file2);
        verify(fileNameResolver).resolveExtension(file2);
        verify(directoryResolver).resolve(file2, rootDirectory, separator);
        verifyNoMoreInteractions(factory, fileNameResolver, directoryResolver);
    }
}
