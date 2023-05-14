package org.dhbw.webapplicationgenerator.webclient.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    private Integer firstPart;
    private Integer secondPart;
    private Integer thirdPart;

    public boolean isSmallerThan(Version version) {
        if (this.firstPart < version.getFirstPart()) {
            return true;
        }
        if (this.firstPart > version.getFirstPart()) {
            return false;
        }
        if (this.secondPart < version.getSecondPart()) {
            return true;
        }
        if (this.secondPart > version.getSecondPart()) {
            return false;
        }
        return this.thirdPart < version.getThirdPart();
    }

    public boolean isGreaterThan(Version version) {
        if (this.firstPart > version.getFirstPart()) {
            return true;
        }
        if (this.firstPart < version.getFirstPart()) {
            return false;
        }
        if (this.secondPart > version.getSecondPart()) {
            return true;
        }
        if (this.secondPart < version.getSecondPart()) {
            return false;
        }
        return this.thirdPart > version.getThirdPart();
    }

}
