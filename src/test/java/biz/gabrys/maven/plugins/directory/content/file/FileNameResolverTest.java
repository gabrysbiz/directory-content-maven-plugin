package biz.gabrys.maven.plugins.directory.content.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

public final class FileNameResolverTest {

    @Test
    public void resolveName_fileWithoutExtension_returnsFullName() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("name");

        final String result = resolver.resolveName(file);

        assertThat(result).isEqualTo("name");
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithoutExtension_returnsNull() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("name");

        final String result = resolver.resolveExtension(file);

        assertThat(result).isNull();
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveName_fileWithExtension_returnsNameWithoutExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("name.ext");

        final String result = resolver.resolveName(file);

        assertThat(result).isEqualTo("name");
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithExtension_returnsExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("name.ext");

        final String result = resolver.resolveExtension(file);

        assertThat(result).isEqualTo("ext");
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveName_fileWithExtensionWithManyDots_returnsNameWithoutExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("na.me.ext");

        final String result = resolver.resolveName(file);

        assertThat(result).isEqualTo("na.me");
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithExtensionWithManyDots_returnsExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("na.me.ext");

        final String result = resolver.resolveExtension(file);

        assertThat(result).isEqualTo("ext");
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveName_fileWithDotAtTheEnd_returnsNameWithoutLastDot() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("name.");

        final String result = resolver.resolveName(file);

        assertThat(result).isEqualTo("name");
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithDotAtTheEnd_returnsEmptyText() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = mock(File.class);
        when(file.getName()).thenReturn("name.");

        final String result = resolver.resolveExtension(file);

        assertThat(result).isEmpty();
        verify(file).getName();
        verifyNoMoreInteractions(file);
    }
}
