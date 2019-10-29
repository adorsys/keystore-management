package de.adorsys.keymanagement;

import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.view.AliasView;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.core.metadata.WithPersister;
import de.adorsys.keymanagement.juggler.services.BCJuggler;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.Security;
import java.util.Collections;
import java.util.function.Supplier;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.core.view.AliasViewImpl.A_ID;
import static org.assertj.core.api.Assertions.assertThat;

class ViewUpdatesTest {

    private BCJuggler juggler = DaggerBCJuggler.builder()
            .metadataPersister(new WithPersister())
            .metadataConfig(
                    MetadataPersistenceConfig.builder()
                            .metadataClass(EmptyMeta.class)
                            .build()
            )
            .build();

    @BeforeAll
    static void addBouncyCastle() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    @SneakyThrows
    void testMetadataIsRemovedWithKey() {
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .generatedEncryptionKey(
                        Encrypting.with().alias("TTT").metadata(new EmptyMeta()).build()
                )
                .build();
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        AliasView<Query<KeyAlias>> view = source.allAliases();

        // Key and its metadata
        assertThat(view.all()).hasSize(2);

        view.update(
                view.retrieve(equal(A_ID, "TTT")).toCollection(),
                Collections.emptyList()
        );

        assertThat(view.all()).hasSize(0);
        assertThat(ks.aliases().hasMoreElements()).isFalse();
    }

    @Test
    @SneakyThrows
    void testMetadataIsRemovedWithKeyById() {
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .generatedEncryptionKey(
                        Encrypting.with().alias("TTT").metadata(new EmptyMeta()).build()
                )
                .build();
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        AliasView<Query<KeyAlias>> view = source.allAliases();

        // Key and its metadata
        assertThat(view.all()).hasSize(2);

        view.removeById("TTT");

        assertThat(view.all()).hasSize(0);
        assertThat(ks.aliases().hasMoreElements()).isFalse();
    }

    @Test
    @SneakyThrows
    void testUpdateAddsMetadata() {
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .build();
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        AliasView<Query<KeyAlias>> view = source.allAliases();

        // Empty keystore
        assertThat(view.all()).hasSize(0);

        view.update(
                Collections.emptyList(),
                Collections.singleton(
                        juggler.generateKeys().secret(
                                Secret.with().alias("MMM").metadata(new EmptyMeta()).password("AA"::toCharArray).build()
                        )
                )
        );

        assertThat(view.all()).hasSize(2);
        assertThat(Collections.list(ks.aliases())).hasSize(2);
    }

    @Test
    @SneakyThrows
    void testSimultaneousAddAndRemoveWithMetadata() {
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .generatedEncryptionKey(
                        Encrypting.with().alias("OLD").metadata(new EmptyMeta()).build()
                )
                .build();

        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        AliasView<Query<KeyAlias>> view = source.aliases();

        // All entries visible
        assertThat(view.all()).hasSize(1);

        view.update(
                view.all(),
                Collections.singleton(
                        juggler.generateKeys().secret(
                                Secret.with().alias("NEW").metadata(new EmptyMeta()).password("AA"::toCharArray).build()
                        )
                )
        );

        // Only non-metadata entries visible
        assertThat(view.all()).hasSize(1);
        assertThat(Collections.list(ks.aliases())).hasSize(2);
        assertThat(Collections.list(ks.aliases())).startsWith("NEW").hasSize(2);
    }

    @Getter
    @RequiredArgsConstructor
    private static class EmptyMeta implements KeyMetadata {
    }
}
