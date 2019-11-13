#  Project overview
This library allows to generate keys and keystores using fluent-like API instead of dealing with JCA intricacies.
Additionally to key persistence it provides capability of persisting key metadata directly within Java-KeyStore.
Querying keys and their metadata is done using CQEngine under the hood - this allows writing complex queries.
For example one can query for `key instanceof SecretKey`.

#  Problems solved with this library

-  AES,RSA,etc. fluent encrypting key generation.
-  RSA,DSA,etc. fluent signing key generation.
-  Fluent storing of key sets into keystore.
-  KeyStore querying for keys by alias, key type, key metadata, etc.
-  KeyStore manipulation - duplicating, changing key protection password, etc.
-  Key metadata persistence directly inside KeyStore.

# Using library

## Maven

Add dependency (Uses BouncyCastle security provider):
```xml
<dependency>
    <groupId>de.adorsys.keymanagement</groupId>
    <artifactId>juggler-bouncycastle</artifactId>
    <version>0.0.6</version>
</dependency>
```

#  API description

### API flow diagram

![API flow](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/adorsys/keystore-management/develop/docs/img/flow.puml&fmt=svg&vvv=5&sanitize=true)

### Getting access to services

All services are available through `Juggler` interface. To obtain instance of it one should call following:
```groovy
Juggler juggler = DaggerBCJuggler.builder().build();
```
This call will provide you with default Juggler implementation.

`Juggler` is composed of 5 services representing different kind of operations:
- `generateKeys()` to generate Secret/Private/Signing keys(or their set) from simple template
- `toKeystore()` to persist generated key set into keystore
- `readKeys()` to read keys from Java keystore and query them by alias/metadata/type/...
- `decode()` to decode key bytes read from keystore into i.e. String for PBE raw keys
- `serializeDeserialize()` to serialize/deserialize KeyStore to/from byte array.

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
[Example:Generate keystore](juggler/juggler-bouncycastle/src/test/java/de/adorsys/keymanagement/examples/NewKeyStoreTest.java#L29-L50)
```groovy
// Obtain Juggler service instance:
BCJuggler juggler = DaggerBCJuggler.builder().build();

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

### Change keystore password or clone it
[Example:Clone keystore and change key password](juggler/juggler-bouncycastle/src/test/java/de/adorsys/keymanagement/examples/CloneKeyStoreAndChangeKeyPasswordTest.java#L28-L59)
```groovy
// Obtain Juggler service instance:
BCJuggler juggler = DaggerBCJuggler.builder().build();

// We want our keystore to have:
KeySetTemplate template = KeySetTemplate.builder()
        .providedKey(ProvidedKey.with().alias("MY-KEY").key(stubSecretKey()).build()) // One provided key (i.e. existing) that has alias `MY-KEY`
        .generatedEncryptionKeys(Encrypting.with().prefix("ENC").build().repeat(10)) // Ten generated private keys (with certificates) that have alias `ENC` + random UUID
        .build();

// Provide key protection password:
Supplier<char[]> password = "PASSWORD!"::toCharArray;
// Generate key set
KeySet keySet = juggler.generateKeys().fromTemplate(template);
// Generate KeyStore with each key protected with `PASSWORD!` password
KeyStore store = juggler.toKeystore().generate(keySet, password);
// Clone generated KeyStore:
Supplier<char[]> newPassword = "NEW_PASSWORD!"::toCharArray;
// Create key set from old keystore that has new password `NEW_PASSWORD!`:
KeySet clonedSet = juggler.readKeys()
        .fromKeyStore(store, id -> password.get())
        .copyToKeySet(id -> newPassword.get());
// Generate cloned KeyStore with each key protected with `NEW_PASSWORD!` password (provided on key set)
KeyStore newKeystore = juggler.toKeystore().generate(clonedSet, () -> null);

// Validate old keystore has same key count as new keystore:
assertThat(countKeys(store)).isEqualTo(countKeys(newKeystore));
// Validate old keystore has key password `PASSWORD!`
assertThat(store.getKey("MY-KEY", "PASSWORD!".toCharArray())).isNotNull();
// Validate new keystore has key password `NEW_PASSWORD!`
assertThat(newKeystore.getKey("MY-KEY", "NEW_PASSWORD!".toCharArray())).isNotNull();
```

### Store your own char[] or String securely inside Java Keystore

It is possible your own char sequence in encrypted form inside Keystore using password-based-encryption. This way
you can store any data in form of SecretKey within java KeyStore.

[Example:Store your own char array securely in KeyStore](juggler/juggler-bouncycastle/src/test/java/de/adorsys/keymanagement/examples/GeneratePbeKeyTest.java#L24-L45)
```groovy
// Obtain Juggler service instance:
BCJuggler juggler = DaggerBCJuggler.builder().build();
// Generate PBE (password-based encryption) raw key (only transformed to be stored in keystore,
// encryption IS PROVIDED by keystore - i.e. BCFKS or UBER keystore provide it):
Supplier<char[]> keyPassword =  "WOW"::toCharArray;
ProvidedKey key = juggler.generateKeys().secretRaw(
        Pbe.with()
                .alias("AES-KEY") // with alias `AES-KEY` if we will save it to keystore from KeySet
                .data("MY SECRET DATA Тест!".toCharArray()) // This data will be encrypted inside KeyStore when stored
                .password(keyPassword) // Password that will be used to protect key in KeyStore
                .build()
);

// Send key to keystore
KeyStore ks = juggler.toKeystore().generate(KeySet.builder().key(key).build());

// Read key back
SecretKeySpec keyFromKeyStore = (SecretKeySpec) ks.getKey("AES-KEY", keyPassword.get());
// Note that BouncyCastle keys are encoded in PKCS12 byte format - UTF-16 big endian + 2 0's padding
assertThat(juggler.decode().decodeAsString(keyFromKeyStore.getEncoded())).isEqualTo("MY SECRET DATA Тест!");
```


### Generate secret key
[Example:Generate secret key](juggler/juggler-bouncycastle/src/test/java/de/adorsys/keymanagement/examples/GenerateSecretKeyTest.java#L18-L32)
```groovy
// Obtain Juggler service instance:
BCJuggler juggler = DaggerBCJuggler.builder().build();
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
[Example:Query keystore](juggler/juggler-bouncycastle/src/test/java/de/adorsys/keymanagement/examples/QueryKeyStoreTest.java#L27-L64)
```groovy
// Obtain Juggler service
BCJuggler juggler = DaggerBCJuggler.builder().build();


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
[Example:Save metadata to keystore](juggler/juggler-bouncycastle/src/test/java/de/adorsys/keymanagement/examples/PersistMetadataToKeyStoreTest.java#L33-L77)
```groovy
// Obtain Juggler service
BCJuggler juggler = DaggerBCJuggler.builder()
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
                        has(META), // Key has metadata
                        lessThan(
                                attribute(key -> ((KeyExpirationMetadata) key.getMeta()).getExpiresAfter()), // Key expiration date
                                Instant.now() // current date, so that if expiresAfter < now() key is expired
                        )
                )
        ).toCollection()
).hasSize(1);
```

### Update key in keystore based on its metadata
[Example:Rotate expired key in keystore](juggler/juggler-bouncycastle/src/test/java/de/adorsys/keymanagement/examples/RotateKeyBasedOnMetadataTest.java#L37-L89)
```groovy
// Obtain Juggler service
BCJuggler juggler = DaggerBCJuggler.builder()
        .metadataPersister(new WithPersister()) // enable metadata persistence
        .metadataConfig(
                MetadataPersistenceConfig.builder()
                        .metadataClass(KeyValidity.class) // define metadata class
                        .build()
        )
        .build();

// Key protection password:
Supplier<char[]> password = "PASSWORD!"::toCharArray;

// Lazy key template:
Function<Instant, Encrypting> keyTemplate = expiryDate -> Encrypting.with()
        .alias("ENC-KEY-1") // key with alias `ENC-KEY-1` in KeyStore// Associated metadata with this key, pretend it is `expired` key
        .metadata(new KeyValidity(expiryDate))
        .password(password)
        .build();

// Key set template that is going to be saved into KeyStore
KeySetTemplate template = KeySetTemplate.builder()
        // One private key that can be used for encryption:
        .generatedEncryptionKey(
                keyTemplate.apply(Instant.now().minusSeconds(10)) // Will pretend that key has expired
        )
        .build();

// Generate key set:
KeySet keySet = juggler.generateKeys().fromTemplate(template); // Key metadata will indicate that key has expired
// Generate new KeyStore, it will have metadata in it
KeyStore ks = juggler.toKeystore().generate(keySet, () -> null);
// Open KeyStore view to query it:
KeyStoreView source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
// Open alias view to query key alias by metadata
AliasView<Query<KeyAlias>> view = source.aliases();
// Find expired key:
KeyAlias expired = view.uniqueResult(KeyValidity.EXPIRED);
// replace expired key:
view.update(
        Collections.singleton(expired),
        Collections.singleton(
                juggler.generateKeys().encrypting(
                        keyTemplate.apply(Instant.now().plus(10, ChronoUnit.HOURS)) // Valid for 10 hours from now
                )
        )
);
// validate there is only one `ENC-KEY-1` key
assertThat(view.retrieve(equal(A_ID, "ENC-KEY-1")).toCollection()).hasSize(1);
// and this key is NOT expired
assertThat(view.retrieve(KeyValidity.EXPIRED).toCollection()).hasSize(0);
```

#  Project details
Main service provider - `Juggler` is built using Dagger2 framework. This allows user to re-compose this service
in his own project by providing replacing modules.
