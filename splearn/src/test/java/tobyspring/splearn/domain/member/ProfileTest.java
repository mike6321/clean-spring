package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void url() {
        String address = "junwoochoi";
        Profile profile = new Profile(address);
        assertThat(profile.url()).isEqualTo("@" + address);
    }

}
