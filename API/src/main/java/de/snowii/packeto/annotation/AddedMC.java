package de.snowii.packeto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This Function are only Available since this Minecraft Version
 */
@Documented
@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface AddedMC {
    String version();
}
