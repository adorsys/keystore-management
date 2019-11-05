package de.adorsys.keymanagement.keyrotation;

import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;
import de.adorsys.keymanagement.keyrotation.config.properties.RotationConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(profiles = "custom-templates")
class CustomKeyTemplateTest extends BaseJdbcDbTest {

    @Autowired
    private RotationConfiguration conf;

    @Test
    void testKeyTemplatesApplied() {

        Secret secret = (Secret) conf.getKeyTemplate().get(KeyType.SECRET);
        assertThat(secret).extracting(Secret::getAlgo).isEqualTo("AES");
        assertThat(secret).extracting(Secret::getSize).isEqualTo(128);

        Encrypting encrypting = (Encrypting) conf.getKeyTemplate().get(KeyType.ENCRYPTING);
        assertThat(encrypting).extracting(Encrypting::getAlgo).isEqualTo("RSA");
        assertThat(encrypting).extracting(Encrypting::getSize).isEqualTo(1024);
        assertThat(encrypting).extracting(Encrypting::getSigAlgo).isEqualTo("SHA256withRSA");

        Signing signing = (Signing) conf.getKeyTemplate().get(KeyType.SIGNING);
        assertThat(signing).extracting(Signing::getAlgo).isEqualTo("DSA");
        assertThat(signing).extracting(Signing::getSize).isEqualTo(1024);
        assertThat(signing).extracting(Signing::getSigAlgo).isEqualTo("SHA256withDSA");
    }
}
