#  Project overview
This library allows to generate keys and keystores using fluent-like API instead of dealing with JCA intricacies.
Additionally to key persistence it provides capability of persisting key metadata directly within Java-KeyStore.
Querying keys and their metadata is done using CQEngine under the hood - this allows writing complex queries.
For example one can query for `key instanceof SecretKey`.


#  API description
All services are available through `Juggler` interface. To obtain instance of it one should call following:
```groovy
Juggler juggler = DaggerJuggler.builder().build();
```
This call will provide you with default Juggler implementation.

`Juggler` is composed of 3 services representing different kind of operations:
- `generateKeys()` to generate Secret/Private/Signing keys(or their set) from simple template
- `toKeystore()` to persist generated key set into keystore
- `readKeys()` to read keys from Java keystore and query them by alias/metadata/type/...

## API examples
<!--
To update snippets you can use embed.sh
MacOS: Install gnused and gnugrep:
`brew install gnu-sed`
`brew install grep`

Example script usage:
./embed.sh Example README.md > README-tmp.md && mv README-tmp.md README.md

TODO: Migrate to AsciiDoc for automatic snippet embedding.
-->

### Generate keystore
[Example:Generate keystore](core/src/test/java/de/adorsys/keymanagement/examples/NewKeyStoreTest.java#L30-L51)
```groovy
// Obtain Juggler service instance:
Juggler juggler = DaggerJuggler.builder().build();

// We want our keystore to have:
KeySetTemplate template = KeySetTemplate.builder()
        .providedKey(ProvidedKey.with().alias("MY-KEY").key(stubSecretKey()).build()) // One provided key (i.e. existing) that has alias `MY-KEY`
        .generatedSecretKey(Secret.with().prefix("SEC").build()) // One generated secret key that has alias `SEC` + random UUID
        .generatedSigningKey(Signing.with().algo("DSA").alias("SIGN").build()) // One generated signing key that has alias `SIGN` + random UUID and uses DSA algorithm
        .generatedEncryptionKeys(Encrypting.with().prefix("ENC").build().repeat(10)) // Ten generated private keys (with certificates) that have alias `ENC` + random UUID
        .build();

// Provide key protection password:
Supplier<char[]> password = "PASSWORD!"::toCharArray;
// Generate key set
KeySet keySet = juggler.generateKeys().fromTemplate(template);
// Generate KeyStore
KeyStore store = juggler.toKeystore().generate(keySet, password);
// Validate that keystore has 13 keys:
// One provided, one secret, one signing, ten private
assertThat(countKeys(store)).isEqualTo(13);
```

### Generate secret key
[Example:Generate secret key](core/src/test/java/de/adorsys/keymanagement/examples/GenerateSecretKeyTest.java#L19-L33)
```groovy
// Obtain Juggler service instance:
Juggler juggler = DaggerJuggler.builder().build();
// Generate key:
Key key = juggler.generateKeys().secret(
        Secret.with()
                .alias("AES-KEY") // with alias `AES-KEY` if we will save it to keystore from KeySet
                .algo("AES") // for AES encryption
                .keySize(128) // for AES-128 encryption
                .build()
).getKey();

assertThat(key.getAlgorithm()).isEqualTo("AES");
assertThat(key.getEncoded()).hasSize(16); // 16 * 8 (sizeof byte) = 128 bits
```

### Open and analyze keystore
[Example:Query keystore](core/src/test/java/de/adorsys/keymanagement/examples/QueryKeyStoreTest.java#L28-L65)
```groovy
// Obtain Juggler service
Juggler juggler = DaggerJuggler.builder().build();


KeySetTemplate template = KeySetTemplate.builder()
        .generatedSecretKey(Secret.with().prefix("SEC").build()) // Secret key to be generated with name `SEC` + random UUID
        .generatedSigningKey(Signing.with().algo("DSA").alias("SIGN-1").build()) // DSA-based signing key with name `SIGN-1`
        .generatedEncryptionKey(Encrypting.with().alias("ENC-1").build()) // Private key with name `ENC-1`
        .generatedEncryptionKeys(Encrypting.with().prefix("GEN").build().repeat(10)) // Ten private keys with name `GEN` + random UUID
        .build();

// Generate key set from template:
KeySet keySet = juggler.generateKeys().fromTemplate(template);
// Key protection password:
Supplier<char[]> password = "PASSWORD!"::toCharArray;
// Create KeyStore
KeyStore store = juggler.toKeystore().generate(keySet, password);
// Open KeyStore-view to query it:
KeyStoreView source = juggler.readKeys().fromKeyStore(store, id -> password.get());
// Acquire Key-Entry view, so we can query for KeyEntry entities
EntryView<Query<KeyEntry>> entryView = source.entries();
// Query for fact that KeyStore has 13 keys in total:
assertThat(entryView.all()).hasSize(13);
// Query for fact that KeyStore has 1 key with name `SEC`
assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias = 'ENC-1'").toCollection()).hasSize(1);
// Query for fact that KeyStore has 10 keys with prefix `GEN`
assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias LIKE 'GEN%'").toCollection()).hasSize(10);
// Query for fact that KeyStore has 1 secret key:
assertThat(entryView.retrieve("SELECT * FROM keys WHERE is_secret = true").toCollection()).hasSize(1);

// Query for fact that KeyStore has 1 secret key:
assertThat(entryView.privateKeys()).hasSize(12);
// Query for fact that KeyStore has 1 secret key:
assertThat(entryView.secretKeys()).hasSize(1);
// Query for fact that KeyStore has 0 trusted certs:
assertThat(entryView.trustedCerts()).hasSize(0);
```

### Persist key with metadata into keystore
[Example:Save metadata to keystore](core/src/test/java/de/adorsys/keymanagement/examples/PersistMetadataToKeyStoreTest.java#L35-L79)
```groovy
// Obtain Juggler service
Juggler juggler = DaggerJuggler.builder()
        .metadataPersister(new WithPersister()) // enable metadata persistence
        .metadataConfig(
                MetadataPersistenceConfig.builder()
                        .metadataClass(KeyExpirationMetadata.class) // define metadata class
                        .build()
        )
        .build();

// Key set template that is going to be saved into KeyStore
KeySetTemplate template = KeySetTemplate.builder()
        // One private key that can be used for encryption:
        .generatedEncryptionKey(
                Encrypting.with()
                        .alias("ENC-KEY-1") // key with alias `ENC-KEY-1` in KeyStore
                        .metadata(new KeyExpirationMetadata(Instant.now())) // Associated metadata with this key, pretend it is `expired` key
                        .build()
        )
        .build();

// Generate key set:
KeySet keySet = juggler.generateKeys().fromTemplate(template);
// Key protection password:
Supplier<char[]> password = "PASSWORD!"::toCharArray;
// Generate new KeyStore, it will have metadata in it
KeyStore ks = juggler.toKeystore().generate(keySet, password);
// Open KeyStore view to query it:
KeyStoreView source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
// Open alias view to query key alias by metadata
AliasView<Query<KeyAlias>> view = source.aliases();
// Assert that key has been expired
assertThat(
        view.retrieve(
                and(
                        QueryFactory.has(META), // Key has metadata
                        lessThan(
                                attribute(key -> ((KeyExpirationMetadata) key.getMeta()).getExpiresAfter()), // Key expiration date
                                Instant.now() // current date, so that if expiresAfter < now() key is expired
                        )
                )
        ).toCollection()
).hasSize(1);
```

#  Project details
Main service provider - `Juggler` is built using Dagger2 framework. This allows user to re-compose this service
in his own project by providing replacing modules.
