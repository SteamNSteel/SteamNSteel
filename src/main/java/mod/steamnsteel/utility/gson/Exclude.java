package mod.steamnsteel.utility.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any field that has this annotation will not be serialised/deserialised if {@link AnnotationExlusion}
 * is added as an ExclusionStrategy;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Exclude
{
}
